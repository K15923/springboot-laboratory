package com.k.quartz.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.jcraft.jsch.*;
import com.k.quartz.config.Dbenv;
import com.k.quartz.config.Env;
import com.k.quartz.entity.Result;
import com.k.quartz.service.BackupRecoveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BackupRecoveryServiceImpl implements BackupRecoveryService {

    @Value("${backup.filePath}")
    private String FILE_PATH;

    @Value("${backup.remoteFolder}")
    private String remoteFilePath;

    @Value("${docker.container}")
    private String dockerContainer;


    // 记录当前任务状态
    private volatile int state = 0;

    @Resource
    private Env env;
    @Resource
    private Dbenv dbenv;

    @Override
    public Result backup(String backupType) {
        this.state = 1;
        // 异步执行备份任务
        CompletableFuture.runAsync(() -> {
            this.state = 5;
            //  todo 停其他服务
            // 先创建指定目录文件夹  2023-01-15_01：00：00_自动备份 此时进度 10%
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
            String format = df.format(LocalDateTime.now());
            String path = FILE_PATH + File.separator + format + "_" + backupType;
            String mysqlFilePath = path + File.separator + format + "_mysqlbackup.sql";
            String modelFilePath = path + File.separator + format + "_modelbackup.tar.gz";
            try {
                // 创建本地文件
                createDirectory(path);
                createFile(mysqlFilePath);
                createFile(modelFilePath);
                // sftp 远程创建目录文件
                JSch jsch = new JSch();
                Session session = jsch.getSession(env.getRemoteusername(), env.getRemotehost(),
                                                  Integer.valueOf(env.getRemotport()));
                session.setPassword(env.getRemotepassword());
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
                sftpChannel.connect();
                // 远程创建目录
                createRomoteDirectory(sftpChannel, FILE_PATH);
                createRomoteDirectory(sftpChannel, path);
                // 远程创建目录文件
                sftpChannel.put(mysqlFilePath, mysqlFilePath);
                sftpChannel.exit();
                session.disconnect();
            } catch (JSchException | SftpException e) {
                e.printStackTrace();
            }
            this.state = 10;
            // 远程调用mysqldump，生成backup文件 并copy到备份服务器
            String backupDBPath = backupDB(mysqlFilePath);
            this.state = 50;
            // 远程压缩文件并copy到备份服务器
            String backupMinioPath = backupMinio(path, format + "_modelbackup.tar.gz");
            this.state = 90;
            // 将备份记录下来 并将记录保存到文件保存到文件夹
            recordBackup(backupDBPath, backupMinioPath);
            this.state = 100;
            // 休眠5s，结束前台任务之后，将状态state置为0；
            try {
                TimeUnit.SECONDS.sleep(5);
                this.state = 0;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return Result.ok("备份成功");
    }

    private void createRomoteDirectory(ChannelSftp sftpChannel, String path) throws SftpException {
        try {
            sftpChannel.stat(path);
        } catch (Exception e) {
            // 如果文件夹不存在则创建文件夹
            sftpChannel.mkdir(path);
        }
    }

    private void createFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            log.info("文件已经存在");
        } else {
            try {
                if (file.createNewFile()) {
                    log.info("文件已经创建");
                } else {
                    log.error("文件创建失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            System.out.println("文件夹已经存在");
        } else {
            if (dir.mkdirs()) {
                System.out.println("文件夹已经创建");
            } else {
                System.out.println("文件夹创建失败");
            }
        }
    }


    @Override
    public Result recovery(String backupFile) {
        CompletableFuture.runAsync(() -> {
            this.state = 5;
            // todo 停止其他服务
            recoveryMinio(backupFile);
            this.state = 50;
            // 远程链接数据库，执行sql脚本恢复数据，恢复之前先备份到本地
            recoveryDB(backupFile);
            this.state = 100;
            // 休眠5s，结束前台任务之后，将状态state置为0；
            try {
                String dockerHost = "tcp://" + env.getRemotehost() + ":2375";
                restartContainer(dockerHost, dockerContainer);
            } catch (Exception e) {
                log.error("docker 重启失败!");
                throw new RuntimeException(e);
            }
            try {
                TimeUnit.SECONDS.sleep(5);
                this.state = 0;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return Result.ok("恢复成功");
    }

    public void restartContainer(String dockerHost, String containerName) {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(dockerHost)
                                                             .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();
        RestartContainerCmd restartCmd = dockerClient.restartContainerCmd(containerName);
        restartCmd.exec();
        log.info("docker 重启成功!");
    }


    @Override
    public Result queryStatus() {
        return Result.ok(state);
    }

    @Override
    public Result queryBackupList() {
        File file = new File(FILE_PATH);
        File[] files = file.listFiles();
        List<String> fileList = new ArrayList<>();
        Arrays.stream(files).sorted();
        for (File file1 : files) {
            if (file1.isDirectory()) {
                fileList.add(file1.getName());
            }
        }
        return Result.ok(fileList);
    }

    private String backupDB(String backupPath) {
        String remoteHost = dbenv.getHost();
        String remoteUser = dbenv.getUsername();
        String remotePassword = env.getRemotepassword();
        String remoteDatabase = dbenv.getDatabaseName();
        String mysqlPassword = dbenv.getPassword();
        try {
            // Create a new JSch instance
            JSch jsch = new JSch();

            // Connect to remote server
            Session session = jsch.getSession(remoteUser, remoteHost, 22);
            session.setPassword(remotePassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            // Execute mysqldump command remotely
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            String command = "mysqldump -u root -p'" + mysqlPassword + "' " + "--databases " + remoteDatabase + " > " +
                             backupPath;
            channelExec.setCommand(command);
            channelExec.connect();
            InputStream inputStream = channelExec.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            bufferedReader.close();
            channelExec.disconnect();
            // Copy dump file from remote server to local server using SFTP
            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.get(backupPath, backupPath);
            // 拷贝完成之后，删除远程服务器上的文件
            channelSftp.rm(backupPath);
            channelSftp.disconnect();
            // Disconnect from remote server
            session.disconnect();
        } catch (IOException | JSchException | SftpException e) {
            this.state = 0;
            throw new RuntimeException(e);
        }
        return remoteHost;
    }

    private String backupMinio(String modelFilePath, String zipFileName) {
        String remoteFolder = remoteFilePath + File.separator + "configure"; // 远程待压缩文件夹路径
        String remoteHost = env.getRemotehost();
        String remoteUser = env.getRemoteusername();
        String remotePassword = env.getRemotepassword();
        String localFolder = modelFilePath; // 本地保存文件的文件夹路径
        Session session = null;
        try {
            // Create a new JSch instance
            JSch jsch = new JSch();
            // Connect to remote server
            session = jsch.getSession(remoteUser, remoteHost, 22);
            session.setPassword(remotePassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            // Compress remote folder to tar.gz file
            String remoteTarFile = remoteFolder + ".tar";
            String remoteTarGzFile = remoteTarFile + ".gz";
            String command = "tar -cvf " + remoteTarFile + " " + remoteFolder + " && gzip -f " + remoteTarFile;
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.connect();
            InputStream inputStream = channelExec.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            bufferedReader.close();
            channelExec.disconnect();
            // Copy zip file from remote server to local server using SFTP
            String localZipFile = localFolder + File.separator + zipFileName;
            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.get(remoteTarGzFile, localZipFile);
            channelSftp.rm(remoteTarGzFile);
            channelSftp.disconnect();
            // Disconnect from remote server
            session.disconnect();

        } catch (JSchException | SftpException | IOException e) {
            this.state = 0;
            log.error("备份minio错误!" + e.toString());
            e.printStackTrace();
        } finally {
            // 关闭SSH会话
            this.state = 0;
            if (session != null) {
                session.disconnect();
            }
        }
        return "";
    }

    private void recordBackup(String backupDBPath, String backupMinioPath) {

    }

    private void recoveryMinio(String backupFile) {
        // backupFile : 2023-04-18_211317_手动备份
        // 2023-04-20_055836_modelbackup.tar.gz
        String[] s = backupFile.split("_");
        String modelName = s[0] + "_" + s[1] + "_modelbackup.tar.gz";
        String localFilePath = FILE_PATH + File.separator + backupFile + File.separator + modelName;
        // String remoteFilePath01 = this.remoteFilePath + File.separator + "configure.tar.gz";
        String remoteHost = env.getRemotehost();
        String remoteUser = env.getRemoteusername();
        String remotePassword = env.getRemotepassword();
        String minIOPath = remoteFilePath + File.separator + "configure";
        // String minIOBackupFile = remoteFilePath + File.separator + "configure.tar.gz";
        try {
            // 创建JSch对象
            JSch jsch = new JSch();
            // 连接远程主机
            Session session = jsch.getSession(remoteUser, remoteHost, 22);
            session.setPassword(remotePassword);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            // 上传本地文件到远程服务器
            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            File localFile = new File(localFilePath);
            InputStream inputStream = new FileInputStream(localFile);
            channelSftp.put(inputStream, File.separator + "configure.tar.gz");
            inputStream.close();
            // 递归删除文件
            if (minIOPath != null && !minIOPath.isEmpty() && exists(channelSftp, minIOPath)) {
                deleteFolder(channelSftp, minIOPath);
            }
            // 解压远程文件到指定目录
            String extractCommand = "tar -xzf " + File.separator + "configure.tar.gz" + " -C " + "/";
            // Extract remote tar.gz file to remote folder
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(extractCommand);
            InputStream inputStreamLog = channelExec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamLog));
            channelExec.connect();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            if (channelExec.getExitStatus() == 0) {
                System.out.println("The remote tar.gz file has been extracted to the remote folder.");
            } else {
                System.out.println("An error occurred while extracting the remote tar.gz file.");
            }

            channelExec.disconnect();
            channelSftp.rm(File.separator + "configure.tar.gz");
            channelSftp.disconnect();
            // 关闭会话
            session.disconnect();
            System.out.println("File transfer and unzip completed successfully!");
        } catch (Exception e) {
            this.state = 0;
            e.printStackTrace();
        }
    }

    private boolean exists(ChannelSftp channel, String path) {
        try {
            SftpATTRS attrs = channel.lstat(path);
            return attrs.isDir();
        } catch (SftpException e) {
            return false;
        }
    }

    private void deleteFolder(ChannelSftp channel, String folderPath) throws SftpException {
        channel.cd(folderPath);
        Vector<ChannelSftp.LsEntry> list = channel.ls(".");
        if (list != null && !list.isEmpty()) {
            for (ChannelSftp.LsEntry entry : list) {
                String filename = entry.getFilename();
                if (!filename.equals(".") && !filename.equals("..")) {
                    if (entry.getAttrs().isDir()) {
                        deleteFolder(channel, folderPath + "/" + filename);
                    } else {
                        channel.rm(folderPath + "/" + filename);
                    }
                }
            }
        }
        channel.rmdir(folderPath);
    }

    private void recoveryDB(String backupFile) {
        // 备份到本地
        // backupFile : 2023-04-18_211317_手动备份
        // 2023-04-20_055836_mysqlbackup.sql
        // backupDB("temp");
        // 执行SQL脚本
        String[] s = backupFile.split("_");
        String sqlName = s[0] + "_" + s[1] + "_mysqlbackup.sql";
        String localFilePath = FILE_PATH + File.separator + backupFile + File.separator + sqlName;
        uploadFile(localFilePath, localFilePath, env.getRemoteusername(), env.getRemotepassword(), env.getRemotehost());
        restoreDB(localFilePath, dbenv.getDatabaseName(), dbenv.getUsername(), dbenv.getPassword(), dbenv.getHost());
    }

    /**
     * 上传文件到远程服务器
     *
     * @param localFilePath  本地文件路径
     * @param remoteFilePath 远程文件路径
     * @param user           登录用户名
     * @param password       登录密码
     * @param host           远程主机地址
     */
    public void uploadFile(String localFilePath, String remoteFilePath, String user, String password, String host) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(user, host, 22);

            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.put(localFilePath, remoteFilePath);
            channelSftp.exit();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            this.state = 0;
            throw new RuntimeException(e);
        }
    }

    /**
     * 还原MySQL备份文件
     *
     * @param filePath 备份文件的完整路径
     * @param database 数据库名称
     * @param user     登录用户名
     * @param password 登录密码
     * @param host     MySQL服务器地址
     */
    public void restoreDB(String filePath, String database, String user, String password, String host) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(user, host, 22);

            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            InputStream stream = channelExec.getInputStream();
            channelExec.setCommand("mysql -u " + user + " -p" + password + " " + " < " + filePath);
            channelExec.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

            channelExec.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            this.state = 0;
            throw new RuntimeException(e);
        }
    }
}

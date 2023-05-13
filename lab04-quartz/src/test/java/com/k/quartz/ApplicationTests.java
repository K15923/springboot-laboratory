package com.k.quartz;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.jcraft.jsch.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

@SpringBootTest
class ApplicationTests {


    @Test
    void contextLoads() {
        String remoteFolderPath = "/root/environment/instruct/minio/configure";
        String host = "192.168.153.10";
        String username = "root";
        String password = "123456";

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channel = null;

        try {
            session = jsch.getSession(username, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            if (remoteFolderPath != null && !remoteFolderPath.isEmpty() && exists(channel, remoteFolderPath)) {
                deleteFolder(channel, remoteFolderPath);
            } else {
                System.out.println("The remote folder doesn't exist or is empty.");
            }

            System.out.println("ccs");
        } catch (JSchException ex) {
            ex.printStackTrace();
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
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

    @Test
    public void RemoteTarGzExtractor() {
        String remoteFilePath = "/configure.tar.gz";
        String remoteDestFolderPath = "/";
        String server = "192.168.153.10";
        String user = "root";
        String password = "123456";
        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession(user, server, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("tar -xzf " + remoteFilePath + " -C " + remoteDestFolderPath);

            InputStream inputStream = channel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            channel.connect();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            if (channel.getExitStatus() == 0) {
                System.out.println("The remote tar.gz file has been extracted to the remote folder.");
            } else {
                System.out.println("An error occurred while extracting the remote tar.gz file.");
            }
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | IOException ex) {
            ex.printStackTrace();
        }
    }


    @Test
    public void t() {
        DockerClientConfig config =
                DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("tcp://localhost:2375").build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();
        RestartContainerCmd restartCmd = dockerClient.restartContainerCmd("redis");
        restartCmd.exec();

    }

}

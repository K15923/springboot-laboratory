package com.k.lab05minio; /**
 * @author k 2023/5/17 15:40
 */

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MinioBatchDownloadExample {

    public static void main(String[] args) {
        // 连接到MinIO服务器
        MinioClient minioClient = MinioClient.builder().endpoint("http://10.122.82.44:9000").credentials(
                "IT3YnrZmskDoaTij", "DcpmHUnoLrdpSPjZU2r7NzBpwOzJIzF0").build();

        // 设置要下载的桶和对象前缀
        String bucketName = "s3bucket";
        String prefix = "ModelFile/Lightweight/f55d9395-f29f-46b0-bd96-caf2a1846a83/";

        // 列出所有以指定前缀开头的对象
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(
                prefix).recursive(true).build());
        for (Result<Item> result : results) {
            try {
                String objectName = result.get().objectName();
                // 执行批量下载
                // 构造下载目标路径
                String destinationPath = "D:\\\\abcsdf\\" + objectName;
                String replace = destinationPath.replace("/", File.separator);
                File file = new File(replace);
                // 创建目录（如果不存在）
                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    boolean created = parentDir.mkdirs();
                    if (!created) {
                        System.out.println("创建目录失败");
                        return;
                    }
                }
                // file.createNewFile();
                // 下载对象

                minioClient.downloadObject(DownloadObjectArgs.builder().bucket(bucketName).object(objectName)
                                                                 .filename(destinationPath).build());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Batch download completed.");
    }
}
package com.bwie.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.UploadFileRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * guohaotian@163.com
 * OSS文件上传接口
 */

@Component
@Slf4j
public class OSSFileUtil {

    static String endpoint = "oss-cn-beijing.aliyuncs.com";
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    static String accessKeyId = "LTAI5tGqzhhmGpBRgLieQkRY";
    static String accessKeySecret = "YsUEbgEnrQC5hg7rb8LF3RVVFLPpLO";
    static String bucket = "dsh-server-oss";
    static String ossUrl = "https://dsh-server-oss.oss-cn-beijing.aliyuncs.com/";
    /**
     *	MultipartFile类型的文件上传ַ
     * @param file
     * @return
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        oss.putObject(bucket, filename, file.getInputStream());
        return ossUrl+filename;
    }
    /**
     *	OSS断点续传
     * @param file
     * @return
     * @throws IOException
     */
    public String uploadFileByBreakingPoint(MultipartFile file) throws IOException {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {

            ObjectMetadata meta = new ObjectMetadata();
            // 指定上传的内容类型。
           // meta.setContentType("text/plain");

            // 文件上传时设置访问权限ACL。
            // meta.setObjectAcl(CannedAccessControlList.Private);

            // 通过UploadFileRequest设置多个参数。
            // 依次填写Bucket名称（例如examplebucket）以及Object完整路径（例如exampledir/exampleobject.txt），Object完整路径中不能包含Bucket名称。
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucket,file.getOriginalFilename());
            File onFile= new File("d:/"+file.getOriginalFilename());
            copyInputStreamToFile(file.getInputStream(),onFile);
            // 通过UploadFileRequest设置单个参数。
            // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
            uploadFileRequest.setUploadFile(onFile.getAbsolutePath());
            // 指定上传并发线程数，默认值为1。
            uploadFileRequest.setTaskNum(5);
            // 指定上传的分片大小，单位为字节，取值范围为100 KB~5 GB。默认值为100 KB。
            uploadFileRequest.setPartSize(1 * 1024 * 1024);
            // 开启断点续传，默认关闭。
            uploadFileRequest.setEnableCheckpoint(true);
            // 记录本地分片上传结果的文件。上传过程中的进度信息会保存在该文件中，如果某一分片上传失败，再次上传时会根据文件中记录的点继续上传。上传完成后，该文件会被删除。
            // 如果未设置该值，默认与待上传的本地文件同路径，名称为${uploadFile}.ucp。
            //uploadFileRequest.setCheckpointFile(onFile.getAbsolutePath());
            // 文件的元数据。
            uploadFileRequest.setObjectMetadata(meta);
            // 设置上传回调，参数为Callback类型。
            //uploadFileRequest.setCallback("yourCallbackEvent");

            // 断点续传上传。
            ossClient.uploadFile(uploadFileRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (Throwable ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            // 关闭OSSClient。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return ossUrl+file.getOriginalFilename();
    }



    /**
     * 流输出到文件的方法
     */
    private static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }
}

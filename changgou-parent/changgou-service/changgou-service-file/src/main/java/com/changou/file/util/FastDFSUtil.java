package com.changou.file.util;

/**
 * @author 矢量
 * @date 2020/2/8-15:40
 */

import com.changou.file.pojo.FastDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

/**
 * 实现FastDFS文件管理
 */
public class FastDFSUtil {

    /**
     * 加载track的连接信息
     */
    static {
        String fileName = new ClassPathResource("fdsf_clinet.conf").getPath();
        try {
            ClientGlobal.init(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     */
    public static void upload(FastDFSFile fastDFSFile) throws Exception{
        // 附加参数 获取文件的作者
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author", fastDFSFile.getAuthor());
        // 创建一个Tracker访问的客户端对象 TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        // 通过TrackerClient访问TrackerServer服务,获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        // 通过TrackServer的连接信息可以获取到Storage的连接信息，创建StorageClient对象存储Storage的连接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);
        // 通过StorageClient访问Storage,实现文件上传，并且获取文件上传后的存储信息
        /**
         * 1、上传文件的字节数组
         * 2、文件的扩展名
         * 3、附加参数
         */
        storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);
    }
}

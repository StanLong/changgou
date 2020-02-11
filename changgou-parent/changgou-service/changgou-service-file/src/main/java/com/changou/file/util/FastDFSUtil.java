package com.changou.file.util;

/**
 * @author 矢量
 * @date 2020/2/8-15:40
 */

import com.changou.file.pojo.FastDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
     * 获取 trackerServer
     * @return
     */
    private static TrackerServer getTrackerServer() throws Exception{
        // 创建一个TrackerClient对象，通过TrackerClient对象访问TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        // 通过TrackerClient获取TrackerServer的连接对象
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    private static StorageClient getStorageClient(TrackerServer trackerServer){
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    /**
     * 文件上传
     */
    public static String[] upload(FastDFSFile fastDFSFile) throws Exception{
        // 附加参数 获取文件的作者
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author", fastDFSFile.getAuthor());
        // 通过TrackServer的连接信息可以获取到Storage的连接信息，创建StorageClient对象存储Storage的连接信息
        StorageClient storageClient = getStorageClient(getTrackerServer());
        // 通过StorageClient访问Storage,实现文件上传，并且获取文件上传后的存储信息
        /**
         * 1、上传文件的字节数组
         * 2、文件的扩展名
         * 3、附加参数
         *
         * uploads[]:
         *  uploads[0]:文件上传所存储的storage的组的名字 group1
         *  uploads[1]:文件存储到storage上的名字
         */
        return storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);
    }

    /**
     * 获取文件信息
     * @param groupName 文件的组名
     * @param remoteFileName 文件的存储路径名字
     */
    public static FileInfo getFile(String groupName, String remoteFileName) throws Exception{
        // 通过 TrackerServer 获取Stroage信息，创建StorageClient对象存储Storage信息
        StorageClient storageClient = getStorageClient(getTrackerServer());
        // 获取文件信息
        return storageClient.get_file_info(groupName, remoteFileName);
    }

    /**
     * 文件下载
     * @param groupName
     * @param remoteFileName
     * @return
     * @throws Exception
     */
    public static InputStream downFile(String groupName, String remoteFileName) throws Exception{
        // 通过 TrackerServer 获取Stroage信息，创建StorageClient对象存储Storage信息
        StorageClient storageClient = getStorageClient(getTrackerServer());
        byte[] buffer = storageClient.download_file(groupName, remoteFileName);
        return new ByteArrayInputStream(buffer);
    }

    /**
     * 删除文件
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName, String remoteFileName) throws Exception{
        // 通过 TrackerServer 获取Stroage信息，创建StorageClient对象存储Storage信息
        StorageClient storageClient = getStorageClient(getTrackerServer());
        // 删除文件
        storageClient.delete_file(groupName, remoteFileName);
    }

    /**
     * 获取Storage信息
     */
    public static StorageServer getStorage() throws Exception{
        // 创建一个TrackerClient对象，通过TrackerClient对象访问TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        // 通过TrackerClient获取TrackerServer的连接对象
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getStoreStorage(trackerServer);
    }

    /**
     * 获取Storage的ip和端口信息
     */
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName) throws Exception{
        // 创建一个TrackerClient对象，通过TrackerClient对象访问TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        // 通过TrackerClient获取TrackerServer的连接对象
        TrackerServer trackerServer = trackerClient.getConnection();
        // 获取Storage的ip和端口信息
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    /**
     * 获取Track信息
     */
    public static String getTrackInfo() throws Exception{
        // Tracker的ip,Http端口
        String ip = getTrackerServer().getInetSocketAddress().getHostString();
        int tracker_http_port = ClientGlobal.getG_tracker_http_port();
        String url = "http://"+ip+":"+tracker_http_port;
        return url;
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        // 测试 getFile 方法
        // FileInfo fileInfo = getFile("group1","M00/00/00/wKjrFV5CR0qALxFwAACrk2W_ngI563.jpg");
        // System.out.println(fileInfo.getSourceIpAddr());

        //测试downFile方法
        //InputStream is = downFile("group1","M00/00/00/wKjrFV5CR0qALxFwAACrk2W_ngI563.jpg");
        //将文件写入本地磁盘
        //FileOutputStream os = new FileOutputStream("D:/StanLong/1.jpg");
        // 定义一个缓冲区
        // byte[] buffer = new byte[1024];
        // while (is.read(buffer) != -1){
        //     os.write(buffer);
        // }
        // os.flush();
        // os.close();
        // is.close();

        // 测试 deleteFile 方法
        //deleteFile("group1","M00/00/00/wKjrFV5CR0qALxFwAACrk2W_ngI563.jpg");

        // 测试 getStorage 方法
        // StorageServer storageServer = getStorage();
        // System.out.println(storageServer.getStorePathIndex());
        // System.out.println(storageServer.getInetSocketAddress().getHostString()); // IP信息

        // 测试 getServerInfo 方法
        // ServerInfo[] groups = getServerInfo("group1","M00/00/00/wKjrFV5BVHqAUzwYAAB5eHlqKkE988.jpg");
        // for(ServerInfo group : groups){
        //     System.out.println(group.getIpAddr());
        //     System.out.println(group.getPort());
        // }

        // 测试 getTrackInfo 方法
        System.out.println(getTrackInfo());
    }
}

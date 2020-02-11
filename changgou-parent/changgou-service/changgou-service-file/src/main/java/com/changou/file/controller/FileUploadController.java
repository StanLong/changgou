package com.changou.file.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changou.file.pojo.FastDFSFile;
import com.changou.file.util.FastDFSUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 矢量
 * @date 2020/2/8-15:59
 */
@RestController
@RequestMapping(value="/upload")
@CrossOrigin
public class FileUploadController {

    @PostMapping
    public Result upload(@RequestParam(value = "file")MultipartFile file) throws Exception{
        // 封装文件信息
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(), // 文件名字
                file.getBytes(),   //文件字节数组
                StringUtils.getFilenameExtension(file.getOriginalFilename()) // 文件扩展名
        );

        String uploads[] = FastDFSUtil.upload(fastDFSFile);
        // 拼接访问地址
        //String url = "http://192.168.235.21:8080/" + uploads[0] + "/" + uploads[1];
        String url = FastDFSUtil.getTrackInfo()+ "/" + uploads[0] + "/" + uploads[1];
        return new Result(true, StatusCode.OK, "上传成功", url);
    }
}

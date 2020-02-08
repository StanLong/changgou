package com.changgou.goods.handler;

import com.changgou.entity.StatusCode;
import com.changgou.entity.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 矢量
 * @date 2020/2/7-17:52
 */
@ControllerAdvice // 全局捕获异常类，只要作用在@RequestMapping上，所有的异常都会捕获
public class BaseExceptionHandler {

    /**
     * 全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }
}
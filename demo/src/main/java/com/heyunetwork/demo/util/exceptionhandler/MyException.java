package com.heyunetwork.demo.util.exceptionhandler;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//自定义异常应该 并继承于java.lang.RuntimeException（表示可处理可不处理）
//这样在抛出异常时就无需增加额外 throws 或 try{}catch(){} d
public class MyException extends RuntimeException {

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "异常信息")
    private String message;
}
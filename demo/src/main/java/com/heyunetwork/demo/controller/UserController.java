package com.heyunetwork.demo.controller;


import com.heyunetwork.demo.util.ResponseResult.ResultCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * vue-admin-template 的模拟登录接口
 */
@RestController
@RequestMapping("vue-admin-template/user")
@CrossOrigin
public class UserController {

    @PostMapping("/login")
    public CommonResponse login(){
        return CommonResponse.ok().data("token","admin");
    }

    @GetMapping("/info")
    public CommonResponse info(){
        return CommonResponse.ok()
                .data("roles","[admin]")
                .data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

}
@Setter
@Getter
class CommonResponse {

    private Boolean success;
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<String, Object>();

    private CommonResponse(){}

    public static CommonResponse ok(){
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setSuccess(true);
        commonResponse.setCode(ResultCode.SUCCESS);
        commonResponse.setMessage("成功");
        return commonResponse;
    }

    public static CommonResponse error(){
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setSuccess(false);
        commonResponse.setCode(ResultCode.ERROR);
        commonResponse.setMessage("失败");
        return commonResponse;
    }

    public CommonResponse success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public CommonResponse message(String message){
        this.setMessage(message);
        return this;
    }

    public CommonResponse code(Integer code){
        this.setCode(code);
        return this;
    }

    public CommonResponse data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public CommonResponse data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

}

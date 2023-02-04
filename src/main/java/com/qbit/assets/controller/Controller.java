package com.qbit.assets.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author martinjiang
 * @description Controller
 * @date 2023/2/5 01:26
 */
@RestController
public class Controller {
    //    http://localhost:8080/hello
    @RequestMapping("/hello")
    public String hello() {
        //调用业务，接受前端参数
        return "hello,springboot";
    }
}

package com.qbit.assets.controller;


import com.qbit.assets.common.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author martinjiang
 */
@Api(tags = "首页模块")
@RestController
@Slf4j
@RequestMapping("/api/doc")
public class DocApiController {
    @Resource
    private RedisUtil redisUtil;

    @ApiImplicitParam(name = "name", value = "姓名", required = true)
    @ApiOperation(value = "向客人问好")
    @GetMapping("/sayHi")
    public ResponseEntity<String> sayHi(@RequestParam(value = "name") String name) {
        log.info("shdhdsjhdhshsgdshs" + name);
        return ResponseEntity.ok("Hi:" + name);
    }

    @ApiImplicitParam(name = "name", value = "姓名", required = true)
    @ApiOperation(value = "getRedis")
    @GetMapping("/getRedis")
    public ResponseEntity<String> getRedis(@RequestParam(value = "name") String name) {
        String a = redisUtil.getAndSet("redisTest", name);
        return ResponseEntity.ok("Hi:" + name);
    }

}

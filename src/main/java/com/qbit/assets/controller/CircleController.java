package com.qbit.assets.controller;

import com.qbit.assets.common.utils.JsonUtil;
import com.qbit.assets.thirdparty.internal.circle.service.CircleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author martinjiang
 * @description CircleController
 * @date 2023/2/5 13:59
 */
@Api(tags = "circle 模块")
@RestController
@Slf4j
@RequestMapping("/api/circle")
public class CircleController {
    @Resource
    private CircleService circleService;

    @ApiOperation(value = "健康检查")
    @GetMapping("health")
    public boolean health() {
        return circleService.health();
    }

    @PostMapping("notifications")
    public String notifications(@RequestBody() String body) {
        log.info("circle notifications origin params: {}", body);
        Map<String, Object> params = JsonUtil.parse(body);
        circleService.notifications(params);
        return "ok";
    }
    
}

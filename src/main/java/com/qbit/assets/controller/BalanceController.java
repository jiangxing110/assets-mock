package com.qbit.assets.controller;

import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.service.BalanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author martinjiang
 */
@Api(tags = "balance")
@RestController
@Slf4j
@RequestMapping("/api/balance")
public class BalanceController {

    @Resource
    private BalanceService balanceService;

    @ApiOperation(value = "余额")
    @GetMapping
    public List<Balance> list() {
        return balanceService.list();
    }
}

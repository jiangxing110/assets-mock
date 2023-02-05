package com.qbit.assets.controller;

import com.qbit.assets.service.PlatformSubAccountService;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateDepositAddressDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateSubAccountDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.SubAccountDepositDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.SubAccountApiKeyVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.SubAccountDepositAddressVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.SubAccountDepositVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.SubAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author martinjiang
 * @description OkxController
 * @date 2023/2/5 13:59
 */
@Api(tags = "okx 模块")
@RestController
@Slf4j
@RequestMapping("")
public class OkxController {
    private final static String PREFIX = "/api/v5/broker/nd/";
    private final static String BROKER = "/api/v5/asset/broker/nd/";

    @Resource
    private PlatformSubAccountService subAccountService;

    @ApiOperation(value = "创建子账户")
    @GetMapping(PREFIX + "create-subaccount")
    public ResponseEntity<SubAccountVO> createSubAccount(CreateSubAccountDTO body) {
        SubAccountVO subAccountVO = subAccountService.createSubAccount(body);
        return ResponseEntity.ok(subAccountVO);
    }

    @ApiOperation(value = "创建子账户apikey")
    @PostMapping(PREFIX + "subaccount/apikey")
    public ResponseEntity<SubAccountApiKeyVO> createSubAccountApiKey(SubAccountApiKeyVO body) {
        SubAccountApiKeyVO subAccountApiKeyVO = subAccountService.createSubAccountApiKey(body);
        return ResponseEntity.ok(subAccountApiKeyVO);
    }

    @ApiOperation(value = "创建子账户充值地址")
    @PostMapping(BROKER + "subaccount-deposit-address")
    public ResponseEntity<SubAccountDepositAddressVO> createDepositAddress(CreateDepositAddressDTO body) {
        SubAccountDepositAddressVO subAccountDepositAddressVO = subAccountService.createDepositAddress(body);
        return ResponseEntity.ok(subAccountDepositAddressVO);
    }


    @ApiOperation(value = "获取子账户充值地址")
    @GetMapping(PREFIX + "subaccount-deposit-address")
    public ResponseEntity<List<SubAccountDepositAddressVO>> createDepositAddress(String subAcct, String ccy) {
        List<SubAccountDepositAddressVO> addresses = subAccountService.getDepositAddresses(subAcct, ccy);
        return ResponseEntity.ok(addresses);
    }


    @ApiOperation(value = "获取子账户充值记录")
    @GetMapping(BROKER + "subaccount-deposit-history")
    public ResponseEntity<List<SubAccountDepositVO>> subAccountDepositHistory(SubAccountDepositDTO body) {
        List<SubAccountDepositVO> subAccountDepositVos = subAccountService.subAccountDepositHistory(body);
        return ResponseEntity.ok(subAccountDepositVos);
    }

}

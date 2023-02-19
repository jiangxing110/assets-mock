package com.qbit.assets.controller;


import com.qbit.assets.common.enums.CryptoAssetsTransferStatus;
import com.qbit.assets.common.utils.R;
import com.qbit.assets.domain.dto.AssetExchangeDto;
import com.qbit.assets.domain.dto.AssetTransferDto;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.domain.vo.AssetsTransferVo;
import com.qbit.assets.service.CryptoAssetsExchangeService;
import com.qbit.assets.service.CryptoAssetsTransferService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author martinjiang
 */
@RestController
@RequestMapping("api/assets/transfers")
public class TransferController {

    @Resource
    private CryptoAssetsTransferService transferService;
    @Resource
    private CryptoAssetsExchangeService exchangeService;

    @ApiOperation(value = "充值交易创建")
    @PostMapping("/deposit")
    public R deposit(@RequestBody AssetTransferDto body) {
        CryptoAssetsTransfer depositVo = transferService.deposit(body);
        return R.ok(depositVo);
    }

    @ApiOperation(value = "出金交易创建")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody AssetTransferDto body) {
        CryptoAssetsTransfer withdrawVo = transferService.withdraw(body);
        return R.ok(withdrawVo);
    }

    @ApiOperation(value = "交易审核")
    @PostMapping("review")
    public R review(@RequestParam(value = "transferId") String transferId, @RequestParam(value = "status") CryptoAssetsTransferStatus status) {
        CryptoAssetsTransfer reviewVo = transferService.review(transferId, status);
        return R.ok(reviewVo);
    }

    @ApiOperation(value = "交易")
    @PostMapping("trade")
    public R exchange(@RequestBody AssetExchangeDto body) {
        AssetsTransferVo exchangeVo = exchangeService.trade(body);
        return R.ok(exchangeVo);
    }

}

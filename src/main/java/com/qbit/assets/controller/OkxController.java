package com.qbit.assets.controller;

import com.qbit.assets.service.BalanceService;
import com.qbit.assets.service.EstimateQuotesService;
import com.qbit.assets.service.PlatformSubAccountService;
import com.qbit.assets.service.TransfersService;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.*;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.*;
import com.qbit.assets.thirdparty.internal.okx.service.OkxAssetsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final static String ASSETS = "/api/v5/asset/";
    private final static String CONVERT = "/api/v5/asset/convert/";
    private final static String MARKET = "/api/v5/market/";

    @Resource
    private PlatformSubAccountService subAccountService;
    @Resource
    private OkxAssetsService okxAssetsService;
    @Resource
    private BalanceService balanceService;
    @Resource
    private EstimateQuotesService estimateQuotesService;
    @Resource
    private TransfersService transfersService;


    @ApiOperation(value = "获取币种列表")
    @GetMapping(ASSETS + "getCurrencies")
    public ResponseEntity<List<AssetsCurrencyVO>> getCurrencies(@RequestParam(value = "ccy", required = false) String ccy) {
        List<AssetsCurrencyVO> currencies = okxAssetsService.getCurrencies(ccy);
        return ResponseEntity.ok(currencies);
    }


    @ApiOperation(value = "获取资金账户所有资产列表，查询各币种的余额")
    @GetMapping(ASSETS + "getBalances")
    public ResponseEntity<List<AssetsBalanceVO>> getBalances(@RequestParam(value = "ccy", required = false) String... ccy) {
        List<AssetsBalanceVO> assetsBalances = balanceService.getOkxBalances(ccy, "OkxWallet", "12306");
        return ResponseEntity.ok(assetsBalances);
    }

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

    @ApiOperation(value = "获取闪兑币对信息")
    @GetMapping(CONVERT + "currency-pair")
    public ResponseEntity<ConvertCurrencyPairVO> getCurrencyPair(@RequestParam(value = "fromCcy") String fromCcy, @RequestParam(value = "toCcy") String toCcy) {
        ConvertCurrencyPairVO convertCurrencyPairVO = subAccountService.getCurrencyPair(fromCcy, toCcy);
        return ResponseEntity.ok(convertCurrencyPairVO);
    }


    @ApiOperation(value = "闪兑预估询价")
    @PostMapping(CONVERT + "estimate-quote")
    public ResponseEntity<ConvertEstimateQuoteVO> getEstimateQuote(@RequestBody ConvertEstimateQuoteDTO body) {
        ConvertEstimateQuoteVO estimateQuote = estimateQuotesService.getEstimateQuote(body);
        return ResponseEntity.ok(estimateQuote);
    }

    @ApiOperation(value = "闪兑交易")
    @PostMapping(CONVERT + "trade")
    public ResponseEntity<ConvertTradeVO> trade(@RequestBody ConvertTradeDTO body) {
        ConvertTradeVO estimateQuote = transfersService.trade(body);
        return ResponseEntity.ok(estimateQuote);
    }


    @ApiOperation(value = "获取闪兑交易历史")
    @GetMapping(CONVERT + "history")
    public ResponseEntity<List<ConvertTradeVO>> getHistory(@RequestParam Map<String, Object> params) {
        List<ConvertTradeVO> trades = transfersService.getHistory(params);
        return ResponseEntity.ok(trades);
    }


    @ApiOperation(value = "获取产品行情信息")
    @GetMapping(MARKET + "ticker")
    public ResponseEntity<List<MarketTickerVO>> getTickers(@RequestParam("instType") String instType) {
        Map map = new HashMap();
        map.put("instType", instType);
        List<MarketTickerVO> trades = transfersService.getTickers(map);
        return ResponseEntity.ok(trades);
    }


    @ApiOperation(value = "获取单个产品行情信息")
    @GetMapping(MARKET + "getTicker")
    public ResponseEntity<List<MarketTickerVO>> getTicker(@RequestParam("instId") String instId) {
        Map map = new HashMap();
        map.put("instId", instId);
        List<MarketTickerVO> tickerVO = transfersService.getTickers(map);
        return ResponseEntity.ok(tickerVO);
    }


/*
    @ApiOperation(value = "资金划转")
    @PostMapping(ASSETS + "transfer")
    public ResponseEntity<AssetsTransferVO> transfer(CreateAssetsTransferDTO body) {
        AssetsTransferVO assetsBalances = subAccountService.transfer(body);
        return ResponseEntity.ok(assetsBalances);
    }

    @ApiOperation(value = "提现")
    @PostMapping(ASSETS + "withdrawal")
    public ResponseEntity<WithdrawalVO> withdrawal(WithdrawalDTO body) {
        WithdrawalVO withdrawalVO = subAccountService.withdrawal(body);
        return ResponseEntity.ok(withdrawalVO);
    }

    @ApiOperation(value = "提现列表")
    @GetMapping(ASSETS + "withdrawal")
    public ResponseEntity<List<WithdrawalHistoryVO>> getWithdrawals(Map<String, Object> params) {
        List<WithdrawalHistoryVO> withdrawalHistorys = subAccountService.getWithdrawals(body);
        return ResponseEntity.ok(withdrawalHistorys);
    }

    @ApiOperation(value = "查询当前币种的充值地址")
    @GetMapping(ASSETS + "withdrawal")
    public ResponseEntity<List<WithdrawalHistoryVO>> getDepositAddress(Map<String, Object> params) {
        List<AssetsDepositAddressVo> assetsDepositAddresss = subAccountService.getDepositAddress(body);
        return ResponseEntity.ok(assetsDepositAddresss);
    }
   */


}

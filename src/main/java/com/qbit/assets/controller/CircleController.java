package com.qbit.assets.controller;

import com.qbit.assets.common.utils.R;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.domain.vo.AccountBalanceVO;
import com.qbit.assets.service.*;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.AddressDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.BankWireDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.PayoutDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.PayoutPageDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.*;
import com.qbit.assets.thirdparty.internal.circle.service.CircleSDKService;
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
 * @description CircleController
 * @date 2023/2/5 13:59
 */
@Api(tags = "circle 模块")
@RestController
@Slf4j
@RequestMapping("")
public class CircleController {
    @Resource
    private CircleSDKService circleSDKService;
    @Resource
    private PayeesService payeesService;
    @Resource
    private BalanceService balanceService;
    @Resource
    private AddressesService addressesService;
    @Resource
    private AssetsOkxService assetsOkxService;
    @Resource
    private CircleService circleService;


    @ApiOperation(value = "健康检查")
    @GetMapping("/ping")
    public Map<String, String> health() {
        Map<String, String> map = new HashMap<>();
        map.put("message", "pong");
        return map;
    }

    @ApiOperation(value = "获取circle配置")
    @GetMapping("/v1/configuration")
    public ResponseEntity<ConfigurationVO> getConfiguration() {
        ConfigurationVO configurationVO = new ConfigurationVO();
        PaymentsVO payments = new PaymentsVO();
        payments.setMasterWalletId(circleSDKService.getMasterWalletId());
        configurationVO.setPayments(payments);
        return ResponseEntity.ok(configurationVO);
    }

    @ApiOperation(value = "获取主账户余额")
    @GetMapping("/v1/balance")
    public ResponseEntity<AccountBalanceVO> getBalances() {
        AccountBalanceVO balances = balanceService.getCircleBalances();
        return ResponseEntity.ok(balances);
    }

    @ApiOperation(value = "创建circle钱包列表")
    @PostMapping("/v1/wallets /" + "{walletId}" + "/addresses")
    public ResponseEntity<AddressVO> createAddress(@PathVariable("walletId") String walletId, @RequestBody AddressDTO body) {
        AddressVO address = addressesService.createAddress(walletId, body);
        return ResponseEntity.ok(address);
    }

    @ApiOperation(value = "获取circle地址列表")
    @GetMapping("/v1/wallets /" + "{walletId}" + "/addresses")
    public ResponseEntity<List<AddressVO>> getAddresses(@PathVariable("walletId") String walletId) {
        List<AddressVO> address = addressesService.getAddresses(walletId);
        return ResponseEntity.ok(address);
    }

    @ApiOperation(value = "ciricle链上充值")
    @PostMapping("/chain/deposit")
    public R deposit(@RequestBody TransferVO body) {
        CryptoAssetsTransfer depositVo = circleService.chainDeposit(body);
        return R.ok(depositVo);
    }

    @ApiOperation(value = "ciricle链上提现")
    @PostMapping("/chain/withdraw")
    public R withdraw(@RequestBody TransferVO body) {
        CryptoAssetsTransfer withdrawVo = circleService.chainWithdraw(body);
        return R.ok(withdrawVo);
    }

    @ApiOperation(value = "创建电汇银行账户")
    @PostMapping("/v1/banks/wires")
    public ResponseEntity<BankWireVO> wires(@RequestBody BankWireDTO body) {
        BankWireVO wires = payeesService.wires(body);
        return ResponseEntity.ok(wires);
    }

    @ApiOperation(value = "银行电汇详情")
    @GetMapping("/v1/banks/wires/" + "{bankAccountId}" + "/instructions")
    public ResponseEntity<BankWireVO> instructions(@PathVariable("bankAccountId") String bankAccountId) {
        BankWireVO wireVO = payeesService.instructions(bankAccountId);
        return ResponseEntity.ok(wireVO);
    }

    @ApiOperation(value = "创建payout")
    @PostMapping("/v1/payouts")
    public ResponseEntity<PayoutVO> payouts(@RequestBody PayoutDTO body) {
        PayoutVO payoutVO = assetsOkxService.payouts(body);
        return ResponseEntity.ok(payoutVO);
    }

    @ApiOperation(value = "获取payout 详情")
    @GetMapping("/v1/payouts/" + "{id}")
    public ResponseEntity<PayoutVO> payouts(@PathVariable("id") String id) {
        PayoutVO payoutVO = assetsOkxService.getPayout(id);
        return ResponseEntity.ok(payoutVO);
    }

    @ApiOperation(value = "查询payout 列表")
    @GetMapping("/v1/payouts")
    public ResponseEntity<List<PayoutVO>> payouts(PayoutPageDTO pageDTO) {
        List<PayoutVO> payouts = assetsOkxService.payoutList(pageDTO);
        return ResponseEntity.ok(payouts);
    }


}

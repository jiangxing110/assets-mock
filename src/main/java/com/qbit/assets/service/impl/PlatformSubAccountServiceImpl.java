package com.qbit.assets.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.enums.*;
import com.qbit.assets.common.utils.OkxUtil;
import com.qbit.assets.domain.entity.*;
import com.qbit.assets.mapper.AddressesMapper;
import com.qbit.assets.mapper.PlatformSubAccountMapper;
import com.qbit.assets.service.BalanceService;
import com.qbit.assets.service.CryptoAssetsTransactionService;
import com.qbit.assets.service.CurrenciesPairsService;
import com.qbit.assets.service.PlatformSubAccountService;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleTransactionStatusEnum;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleWalletTypeEnum;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateDepositAddressDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateSubAccountDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.SubAccountDepositDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.*;
import com.qbit.assets.thirdparty.internal.okx.service.OkxBrokerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
@Service
public class PlatformSubAccountServiceImpl extends ServiceImpl<PlatformSubAccountMapper, PlatformSubAccount> implements PlatformSubAccountService {

    @Resource
    private OkxBrokerService okxBrokerService;
    @Resource
    private PlatformSubAccountMapper subAccountMapper;
    @Resource
    private AddressesMapper addressesMapper;
    @Resource
    private CryptoAssetsTransactionService cryptoAssetsTransactionService;
    @Resource
    private CurrenciesPairsService currenciesPairsService;
    @Resource
    private BalanceService balanceService;

    /**
     * 创建子账户
     *
     * @param body
     * @return
     */
    @Override
    public SubAccountVO createSubAccount(CreateSubAccountDTO body) {
        SubAccountVO subAccountVO = okxBrokerService.createSubAccount(body);
        PlatformSubAccount subAccount = new PlatformSubAccount();
        BeanUtils.copyProperties(subAccountVO, subAccount);
        subAccount.setPlatform("OKX");
        subAccount.setId(UUID.randomUUID().toString());
        subAccount.setVersion(1);
        subAccountMapper.insert(subAccount);
        return subAccountVO;
    }

    /**
     * 创建子账户apikey
     *
     * @param body
     * @return
     */
    @Override
    public SubAccountApiKeyVO createSubAccountApiKey(SubAccountApiKeyVO body) {
        LambdaQueryWrapper<PlatformSubAccount> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(PlatformSubAccount::getSubAcct, body.getSubAcct());
        queryWrapper.last("Limit 1");
        PlatformSubAccount subAccount = subAccountMapper.selectOne(queryWrapper);
        SubAccountApiKeyVO subAccountApiKeyVO = okxBrokerService.createSubAccountApiKey(body);
        if (subAccount != null) {
            subAccount.setApiKey(subAccountApiKeyVO.getApiKey());
            subAccount.setSecretKey(subAccountApiKeyVO.getSecretKey());
            subAccount.setPassPhrase(subAccountApiKeyVO.getPassphrase());
            subAccount.setPerm(subAccountApiKeyVO.getPerm());
            subAccountMapper.updateById(subAccount);
        }
        return subAccountApiKeyVO;
    }

    /**
     * 创建子账户充值地址
     *
     * @param body
     * @return
     */
    @Override
    public SubAccountDepositAddressVO createDepositAddress(CreateDepositAddressDTO body) {
        SubAccountDepositAddressVO subAccountDepositAddressVO = new SubAccountDepositAddressVO();
        Balance balance = balanceService.getCurrcyBalance(SpecialUUID.NullUUID.value, WalletTypeEnum.OkxWallet, CryptoConversionCurrencyEnum.getItem(body.getCcy()));
        Addresse addresses = new Addresse();
        String chain = body.getChain();
        String[] chains = chain.split("-");
        chain = chains[1];
        ChainType chainType = OkxUtil.convertChain(chain);
        addresses.setAccountId(balance.getAccountId());
        addresses.setAddress(UUID.randomUUID().toString());
        addresses.setChain(chainType);
        addresses.setCurrency(body.getCcy());
        addresses.setWalletId(balance.getId());
        addresses.setPlatform(CryptoAssetsPlatform.OKX);
        addresses.setSubAccount(body.getSubAcct());
        addressesMapper.insert(addresses);
        BeanUtils.copyProperties(addresses, subAccountDepositAddressVO);
        return subAccountDepositAddressVO;
    }

    /**
     * 获取子账户充值地址
     *
     * @param subAcct
     * @param ccy
     * @return
     */
    @Override
    public List<SubAccountDepositAddressVO> getDepositAddresses(String subAcct, String ccy) {
        List<SubAccountDepositAddressVO> list = new ArrayList<>();
        LambdaQueryWrapper<Addresse> addressesLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressesLambdaQueryWrapper.eq(Addresse::getPlatform, CryptoAssetsPlatform.OKX);
        addressesLambdaQueryWrapper.eq(Addresse::getAccountId, SpecialUUID.NullUUID.value);
        addressesLambdaQueryWrapper.last("limit 20");
        List<Addresse> addresses = addressesMapper.selectList(addressesLambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(addresses)) {
            for (Addresse address : addresses) {
                SubAccountDepositAddressVO addressVO = new SubAccountDepositAddressVO();
                addressVO.setAddr(address.getAddress());
                addressVO.setTs("1597026383085");
                addressVO.setMemo("");
                String chain = OkxUtil.convertChain(address.getChain());
                addressVO.setCcy(address.getCurrency());
                addressVO.setChain(address.getCurrency() + "-" + chain);
                addressVO.setPmtId("");
                list.add(addressVO);
            }
        }
        return list;
    }


    /**
     * 获取子账户子账户充值记录
     */
    @Override
    public List<SubAccountDepositVO> subAccountDepositHistory(SubAccountDepositDTO body) {
        List<SubAccountDepositVO> subAccountDepositVOS = new ArrayList<>();
        LambdaQueryWrapper<CryptoAssetsTransaction> transactionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        transactionsLambdaQueryWrapper.eq(CryptoAssetsTransaction::getPlatform, CryptoAssetsPlatform.OKX);
        transactionsLambdaQueryWrapper.eq(CryptoAssetsTransaction::getSourceType, CircleWalletTypeEnum.BLOCKCHAIN);
        transactionsLambdaQueryWrapper.eq(CryptoAssetsTransaction::getDestinationType, CircleWalletTypeEnum.WALLET);
        transactionsLambdaQueryWrapper.eq(CryptoAssetsTransaction::getStatus, CircleTransactionStatusEnum.COMPLETE);
        List<CryptoAssetsTransaction> list = cryptoAssetsTransactionService.list(transactionsLambdaQueryWrapper);
        for (CryptoAssetsTransaction platTransactions : list) {
            SubAccountDepositVO subAccountDepositVO = new SubAccountDepositVO();
            subAccountDepositVO.setAmt(platTransactions.getAmount().toString());
            subAccountDepositVO.setTxId(platTransactions.getTransactionHash());
            subAccountDepositVO.setActualDepBlkConfirm("11");
            subAccountDepositVO.setCcy(platTransactions.getCurrency().getValue());
            String chain = OkxUtil.convertChain(platTransactions.getChain());
            subAccountDepositVO.setChain(platTransactions.getCurrency() + "-" + chain);
            subAccountDepositVO.setFrom(platTransactions.getSourceAddress());
            subAccountDepositVO.setTo(platTransactions.getDestinationAddress());
            subAccountDepositVO.setTs(platTransactions.getCreateTime().getTime() + "");
            subAccountDepositVO.setState("2");
            subAccountDepositVO.setDepId(UUID.randomUUID().toString());
            //TODO 子账户
            LambdaQueryWrapper<Addresse> addresseLambdaQueryWrapper = new LambdaQueryWrapper<>();
            addresseLambdaQueryWrapper.eq(Addresse::getAddress, platTransactions.getSourceAddress());
            Addresse addresse = addressesMapper.selectOne(addresseLambdaQueryWrapper);
            if (addresse != null) {
                subAccountDepositVO.setSubAcct(addresse.getSubAccount());
            }
            subAccountDepositVOS.add(subAccountDepositVO);
        }
        return subAccountDepositVOS;
    }

    /**
     * 获取币兑
     *
     * @param fromCcy
     * @param toCcy
     * @return
     */
    @Override
    public ConvertCurrencyPairVO getCurrencyPair(String fromCcy, String toCcy) {
        ConvertCurrencyPairVO convertCurrencyPairVO = new ConvertCurrencyPairVO();
        LambdaQueryWrapper<CurrenciesPairs> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CurrenciesPairs::getBaseCurrency, fromCcy, toCcy);
        wrapper.in(CurrenciesPairs::getQuoteCurrency, fromCcy, toCcy);
        CurrenciesPairs pair = currenciesPairsService.getOne(wrapper);
        if (pair != null) {
            convertCurrencyPairVO.setInstId(pair.getSymbol());
            convertCurrencyPairVO.setBaseCcy(pair.getBaseCurrency());
            convertCurrencyPairVO.setBaseCcyMax(pair.getBaseMax().toString());
            convertCurrencyPairVO.setBaseCcyMin(pair.getBaseMin().toString());
            convertCurrencyPairVO.setQuoteCcy(pair.getQuoteCurrency());
            convertCurrencyPairVO.setQuoteCcyMax(pair.getQuoteMax().toString());
            convertCurrencyPairVO.setQuoteCcyMin(pair.getQuoteMin().toString());
        }
        return convertCurrencyPairVO;
    }
}

package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.common.enums.CryptoAssetsPlatform;
import com.qbit.assets.domain.entity.Addresses;
import com.qbit.assets.domain.entity.PlatTransactions;
import com.qbit.assets.domain.entity.PlatformSubAccount;
import com.qbit.assets.mapper.AddressesMapper;
import com.qbit.assets.mapper.PlatformSubAccountMapper;
import com.qbit.assets.service.PlatTransactionsService;
import com.qbit.assets.service.PlatformSubAccountService;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateDepositAddressDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.CreateSubAccountDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.SubAccountDepositDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.SubAccountApiKeyVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.SubAccountDepositAddressVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.SubAccountDepositVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.SubAccountVO;
import com.qbit.assets.thirdparty.internal.okx.service.OkxBrokerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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
    private PlatTransactionsService platTransactionsService;

    @Override
    public SubAccountVO createSubAccount(CreateSubAccountDTO body) {
        SubAccountVO subAccountVO = okxBrokerService.createSubAccount(body);
        PlatformSubAccount subAccount = new PlatformSubAccount();
        BeanUtils.copyProperties(subAccountVO, subAccount);
        subAccount.setPlatform("OKX");
        subAccount.setId(UUID.randomUUID().toString());
        subAccount.setCreateTime(new Date());
        subAccount.setUpdateTime(new Date());
        subAccount.setVersion(1);
        subAccountMapper.insert(subAccount);
        return subAccountVO;
    }

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

    @Override
    public SubAccountDepositAddressVO createDepositAddress(CreateDepositAddressDTO body) {
        SubAccountDepositAddressVO subAccountDepositAddressVO = new SubAccountDepositAddressVO();
        Addresses addresses = new Addresses();
        addresses.setAccountId("12306");
        addresses.setAddress(UUID.randomUUID().toString());
        addresses.setChain(ChainType.ALGO.getName());
        addresses.setCurrency(body.getCcy());
        addresses.setWalletId("12306");
        addressesMapper.insert(addresses);

        BeanUtils.copyProperties(addresses, subAccountDepositAddressVO);
        return subAccountDepositAddressVO;
    }

    @Override
    public List<SubAccountDepositAddressVO> getDepositAddresses(String subAcct, String ccy) {
        List<SubAccountDepositAddressVO> addressVOS = new ArrayList<>();
        SubAccountDepositAddressVO addressVO = new SubAccountDepositAddressVO();
        addressVO.setAddr("地址1");
        addressVO.setTs("1597026383085");
        addressVO.setMemo("");
        addressVO.setCcy("USDT");
        addressVO.setChain("TRC-20");
        addressVO.setPmtId("");
        addressVOS.add(addressVO);
        return addressVOS;
    }

    @Override
    public List<SubAccountDepositVO> subAccountDepositHistory(SubAccountDepositDTO body) {
        List<SubAccountDepositVO> subAccountDepositVOS = new ArrayList<>();
        LambdaQueryWrapper<PlatTransactions> transactionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        transactionsLambdaQueryWrapper.eq(PlatTransactions::getPlatform, CryptoAssetsPlatform.OKX);
        List<PlatTransactions> list = platTransactionsService.list(transactionsLambdaQueryWrapper);
        for (PlatTransactions platTransactions : list) {
            SubAccountDepositVO subAccountDepositVO = new SubAccountDepositVO();
            subAccountDepositVO.setAmt(platTransactions.getAmount().toString());
            subAccountDepositVO.setTxId(platTransactions.getTransactionHash());
            subAccountDepositVO.setActualDepBlkConfirm("11");
            subAccountDepositVO.setCcy(platTransactions.getCurrency().getValue());
            subAccountDepositVO.setChain(platTransactions.getChain().getValue());
            subAccountDepositVO.setFrom("sdjksjkk");
            subAccountDepositVO.setTo("ghsdjkjhgfhjkjhgfghywewyy");
            subAccountDepositVO.setTs("1597026383085");
            subAccountDepositVO.setState("2");
            subAccountDepositVO.setDepId("hgshdhshd");
            subAccountDepositVO.setSubAcct("hgshdhshd");
        }
        return null;
    }
}

package com.qbit.assets.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.enums.*;
import com.qbit.assets.domain.entity.Addresse;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.mapper.AddressesMapper;
import com.qbit.assets.service.AddressesService;
import com.qbit.assets.service.BalanceService;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.AddressDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.AddressVO;
import com.qbit.assets.thirdparty.internal.circle.service.CircleSDKService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
@Service
@Slf4j
public class AddressesServiceImpl extends ServiceImpl<AddressesMapper, Addresse> implements AddressesService {

    @Resource
    private CircleSDKService sdk;
    @Resource
    private BalanceService balanceService;

    /**
     * 根据钱包ID获取地址
     */
    @Override
    public List<AddressVO> getAddresses(String walletId) {
        List<AddressVO> addressVOS = new ArrayList<>();
        LambdaQueryWrapper<Addresse> addressesLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressesLambdaQueryWrapper.eq(Addresse::getPlatform, CryptoAssetsPlatform.CIRCLE);
        List<Addresse> addresses = this.list(addressesLambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(addresses)) {
            addressVOS = addresses.stream().map(e -> {
                AddressVO addressVO = new AddressVO();
                addressVO.setAddress(e.getAddress());
                addressVO.setCurrency(CryptoConversionCurrencyEnum.USD);
                addressVO.setAddressTag(e.getAddressTag());
                addressVO.setSelected(Boolean.TRUE);
                return addressVO;
            }).collect(Collectors.toList());
        }
        return addressVOS;
    }

    /**
     * 创建钱包地址
     */
    @Override
    public AddressVO createAddress(String walletId, AddressDTO body) {
        AddressVO addressVO = new AddressVO();
        Addresse addresses = new Addresse();
        addresses.setAccountId(SpecialUUID.NullUUID.value());
        Balance balance = balanceService.getCurrcyBalance(SpecialUUID.NullUUID.value(), WalletTypeEnum.CircleWallet, body.getCurrency());
        addresses.setWalletId(balance.getId());
        addresses.setChain(body.getChain());
        addresses.setCurrency(body.getCurrency().getValue());
        AddressVO vo = sdk.createAddress(walletId, body);
        addresses.setAddress(vo.getAddress());
        addresses.setAddressTag(vo.getAddressTag());
        addresses.setPlatform(CryptoAssetsPlatform.CIRCLE);
        this.save(addresses);
        BeanUtils.copyProperties(addresses, addressVO);
        return addressVO;
    }


    /**
     * 更加地址获取钱包
     */
    @Override
    public Balance getBalanceByAddress(ChainType chain, String destinationAddress) {
        Balance balance = new Balance();
        LambdaQueryWrapper<Addresse> addressesLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressesLambdaQueryWrapper.eq(Addresse::getAddress, destinationAddress);
        addressesLambdaQueryWrapper.eq(Addresse::getChain, chain);
        addressesLambdaQueryWrapper.last("limit 1");
        List<Addresse> addresses = this.list(addressesLambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(addresses)) {
            balance = balanceService.getById(addresses.get(0).getWalletId());
        }
        return balance;
    }
}

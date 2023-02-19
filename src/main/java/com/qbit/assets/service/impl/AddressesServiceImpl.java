package com.qbit.assets.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.common.enums.SpecialUUID;
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

    @Override
    public List<AddressVO> getAddresses(String walletId) {
        List<AddressVO> addressVOS = new ArrayList<>();
        LambdaQueryWrapper<Addresse> addressesLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressesLambdaQueryWrapper.eq(Addresse::getWalletId, walletId);
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

    @Override
    public AddressVO createAddress(String walletId, AddressDTO body) {
        AddressVO addressVO = new AddressVO();
        Addresse addresses = new Addresse();
        addresses.setAccountId(SpecialUUID.NullUUID.value());
        addresses.setWalletId(walletId);
        addresses.setChain(body.getChain().getToken());
        addresses.setCurrency(body.getCurrency().getValue());
        AddressVO vo = sdk.createAddress(walletId, body);
        addresses.setAddress(vo.getAddress());
        addresses.setAddressTag(vo.getAddressTag());
        this.save(addresses);
        BeanUtils.copyProperties(addresses, addressVO);
        return addressVO;
    }

    /**
     * @param chain
     * @param destinationAddress
     * @return
     */
    @Override
    public Balance getBalanceByAddress(ChainType chain, String destinationAddress) {
        Balance balance = new Balance();
        LambdaQueryWrapper<Addresse> addressesLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressesLambdaQueryWrapper.eq(Addresse::getAddress, destinationAddress);
        addressesLambdaQueryWrapper.eq(Addresse::getChain, chain);
        addressesLambdaQueryWrapper.last("limit 1");
        Addresse addresses = this.getOne(addressesLambdaQueryWrapper);
        if (addresses != null) {
            balance = balanceService.getById(addresses.getWalletId());
        }
        return balance;
    }
}

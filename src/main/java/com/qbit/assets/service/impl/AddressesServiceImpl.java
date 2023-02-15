package com.qbit.assets.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.common.enums.SpecialUUID;
import com.qbit.assets.domain.entity.Addresses;
import com.qbit.assets.mapper.AddressesMapper;
import com.qbit.assets.service.AddressesService;
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
public class AddressesServiceImpl extends ServiceImpl<AddressesMapper, Addresses> implements AddressesService {

    @Resource
    private CircleSDKService sdk;


    @Override
    public List<AddressVO> getAddresses(String walletId) {
        List<AddressVO> addressVOS = new ArrayList<>();
        LambdaQueryWrapper<Addresses> addressesLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressesLambdaQueryWrapper.eq(Addresses::getWalletId, walletId);
        List<Addresses> addresses = this.list(addressesLambdaQueryWrapper);
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
        Addresses addresses = new Addresses();
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
}

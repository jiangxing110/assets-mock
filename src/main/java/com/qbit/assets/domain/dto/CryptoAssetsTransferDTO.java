package com.qbit.assets.domain.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * @author LiTao litaoh@aliyun.com
 */
@Data
public class CryptoAssetsTransferDTO implements Serializable {
    private String fromBalanceId;

    private String toAddress;

    private Double amount;

    private String transferId;

}

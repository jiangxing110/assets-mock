package com.qbit.assets.domain.dto;

import com.qbit.assets.common.enums.CryptoAssetsTransferStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * @author martinjiang
 */
@Data
public class ReviewTransferDTO implements Serializable {
    private String transferId;

    private CryptoAssetsTransferStatus status;

}

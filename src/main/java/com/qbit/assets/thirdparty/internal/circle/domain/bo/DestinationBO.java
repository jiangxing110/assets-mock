package com.qbit.assets.thirdparty.internal.circle.domain.bo;


import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleWalletTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author litao
 */
@Data
public class DestinationBO implements Serializable {
    private String id;

    private CircleWalletTypeEnum type;

    private String address;

    private ChainType chain;

    private String name;
}

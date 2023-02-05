package com.qbit.assets.thirdparty.internal.circle.domain.bo;


import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleWalletTypeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author litao
 */
@Data
public class SourceBO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private CircleWalletTypeEnum type;

    private ChainType chain;

    private List<IdentityBO> identities;
}

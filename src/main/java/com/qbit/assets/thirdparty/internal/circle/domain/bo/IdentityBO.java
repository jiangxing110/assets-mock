package com.qbit.assets.thirdparty.internal.circle.domain.bo;


import com.qbit.assets.domain.bo.AddressBO;
import com.qbit.assets.thirdparty.internal.circle.enums.IdentityType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author litao
 */
@Data
public class IdentityBO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private IdentityType type;

    private String name;

    private List<AddressBO> addresses;
}

package com.qbit.assets.domain.bo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author litao
 */
@Data
public class AddressBO implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 城市
     */
    private String city;

    /**
     * 国家
     */
    private String country;

    /**
     * line1
     */
    @JsonAlias(value = {"line1", "addressLine1"})
    private String line1;

    /**
     * line2(可选)
     */
    @JsonAlias(value = {"line2", "addressLine2"})
    private String line2;

    /**
     * 区
     */
    @JsonAlias(value = {"district", "state", "county", "province", "region"})
    private String district;

    /**
     * 地址的邮政编码/邮政编码。
     */
    private String postalCode;
}

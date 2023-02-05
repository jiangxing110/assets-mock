package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.domain.base.BaseV2;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_user")
public class User extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户密码
     */
    private String passWord;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

    private String status;

    private String userType;

    /**
     * 手机号的前面的区号
     */
    private String phonePreFix;

    /**
     * lastName
     */
    private String lastName;

    /**
     * firstName
     */
    private String firstName;


}

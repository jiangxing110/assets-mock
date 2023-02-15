package com.qbit.assets.config.mybatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author martinjiang
 * @
 */
@Component
public class QbitMetaObjectHandler implements MetaObjectHandler {
    /**
     * 新增时写入
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        this.strictInsertFill(metaObject, "createTime", Date.class, date);
        this.strictInsertFill(metaObject, "updateTime", Date.class, date);
        this.strictInsertFill(metaObject, "version", Integer.class, 1);
    }

    /**
     * 修改时写入
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }
}

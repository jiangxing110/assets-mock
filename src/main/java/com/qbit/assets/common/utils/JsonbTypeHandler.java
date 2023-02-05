package com.qbit.assets.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author martinjiang
 */
@MappedTypes({Object.class})
public class JsonbTypeHandler extends BaseTypeHandler<Object> {
    //报错的话，请将pom文件的引入graphql的地方<version></version>标签注释掉
    private final PGobject jsonObject = new PGobject();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        if (ps != null) {
            jsonObject.setType("jsonb");
            if (parameter instanceof String) {
                jsonObject.setValue((String) parameter);
            } else {
                jsonObject.setValue(JsonUtil.toJSONString(parameter));
            }
            ps.setObject(i, jsonObject);
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            String msg = (String) JSON.parse(rs.getString(columnName));
            return JsonUtil.parse((msg));
        } catch (Exception e) {
            return rs.getObject(columnName);
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getObject(columnIndex);
    }
}

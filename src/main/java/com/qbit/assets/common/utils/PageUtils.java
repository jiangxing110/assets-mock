package com.qbit.assets.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author litao
 */
@Getter
@Setter
public class PageUtils<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private int total;
    /**
     * 每页记录数
     */
    private int limit;
    /**
     * 列表数据
     */
    private List<T> list;

    public PageUtils(List<T> list, int total, int limit) {
        this.list = list;
        this.total = total;
        this.limit = limit;
    }

    public PageUtils(IPage<T> page) {
        this.list = page.getRecords();
        this.total = (int) page.getTotal();
        this.limit = (int) page.getSize();
    }

    public static <T> List<T> convert(List<?> records, Class<T> clazz) {
        List<T> data = new ArrayList<>(records.size());
        for (Object record : records) {
            T vo;
            try {
                vo = clazz.getConstructor().newInstance();
                BeanUtils.copyProperties(record, vo);
                data.add(vo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static <T> PageUtils<T> convert(IPage<?> page, Class<T> clazz) {
        List<?> records = page.getRecords();
        List<T> data = convert(records, clazz);
        return new PageUtils<>(data, (int) page.getTotal(), (int) page.getSize());
    }
}

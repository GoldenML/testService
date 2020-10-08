package com.epoch.test.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanghui
 * @param <T>
 */
@Data
public class DataResult<T> {
    private T data;
    private Boolean success;
    private String msg;
    private Integer ret;
    private List<T> resultList = new ArrayList<>();
    private Integer totalCount;
}

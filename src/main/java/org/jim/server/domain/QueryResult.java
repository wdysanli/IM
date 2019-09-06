package org.jim.server.domain;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryResult<T> {
    //数据列表
    private List<T> list;
    //数据总数
    private long total;

}

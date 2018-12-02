package cn.com.nex.monitor.webapp.common.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class TableData<T> extends CommonBean {
    private int total;
    private List<T> rows;
}

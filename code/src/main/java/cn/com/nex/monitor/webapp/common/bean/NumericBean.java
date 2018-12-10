package cn.com.nex.monitor.webapp.common.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NumericBean extends CommonBean {
    private int number;
}

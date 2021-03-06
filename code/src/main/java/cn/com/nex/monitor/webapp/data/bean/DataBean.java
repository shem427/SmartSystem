package cn.com.nex.monitor.webapp.data.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class DataBean extends CommonBean {
    private String msg;
    private String iD;
    private double value;
    private double power;
    private Date uploadTime;
}

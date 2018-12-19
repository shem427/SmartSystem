package cn.com.nex.monitor.webapp.dashboard.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UnitDataBean extends CommonBean {
    private String unidId;
    private List<Integer> unitDatas;
    private int normalCount = 0;
    private int warningCount = 0;
    private int errorCount = 0;
}

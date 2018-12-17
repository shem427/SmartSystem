package cn.com.nex.monitor.webapp.dashboard.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DashboardUnitBean extends CommonBean {
    private String unitId;
    private String unitName;
    private String remark;
    private String parentId;
    private int unitStatus;

    public String getStatusCls() {
        if (unitStatus == 1) {
            return "Warning";
        } else if (unitStatus == 2) {
            return "Error";
        } else {
            return "Normal";
        }
    }
}

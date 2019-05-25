package cn.com.nex.monitor.webapp.dashboard.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DashboardUnitBean extends CommonBean implements Comparable<DashboardUnitBean> {
    private String unitId;
    private String unitName;
    private String remark;
    private String parentId;
    private boolean isLeaf;
    private int unitType;
    private int unitStatus;

    private int normalCount;
    private int warnCount;
    private int errorCount;

    public String getStatusCls() {
        if (unitStatus == 1) {
            return "thumbnail status-icon warning";
        } else if (unitStatus == 2) {
            return "thumbnail status-icon error";
        } else {
            return "thumbnail status-icon normal";
        }
    }

    public String getStatusDisplay() {
        if (unitStatus == 1) {
            return "Warning";
        } else if (unitStatus == 2) {
            return "Error";
        } else {
            return "Normal";
        }
    }

    @Override
    public int compareTo(DashboardUnitBean o) {
        return this.unitId.compareTo(o.unitId);
    }
}

package cn.com.nex.monitor.webapp.unit.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonTreeBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UnitBean extends CommonTreeBean {
    /** 备注 */
    private String unitRemark;
    /** 管理者ID List */
    private List<String> managerIdList;
}

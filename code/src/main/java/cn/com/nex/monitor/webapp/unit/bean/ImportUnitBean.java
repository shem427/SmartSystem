package cn.com.nex.monitor.webapp.unit.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ImportUnitBean extends UnitBean {
    private String parentUnitName;
    private String type;
}

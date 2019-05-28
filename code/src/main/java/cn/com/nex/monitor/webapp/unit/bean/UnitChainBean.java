package cn.com.nex.monitor.webapp.unit.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UnitChainBean extends UnitBean {
    private String fullPath;
    private String idChain;
}

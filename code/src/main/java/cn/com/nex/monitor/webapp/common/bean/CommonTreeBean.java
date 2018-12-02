package cn.com.nex.monitor.webapp.common.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommonTreeBean extends CommonBean {
    private String id;
    private String pId;
    private String name;

    private Boolean isParent;
}

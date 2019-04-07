package cn.com.nex.monitor.webapp.status.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MapMarkerBean extends CommonBean {
    private String hospitalId;
    private String hospitalName;
    private double lat;
    private double lng;
}

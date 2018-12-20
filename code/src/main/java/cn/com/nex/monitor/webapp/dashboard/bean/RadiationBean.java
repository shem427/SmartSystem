package cn.com.nex.monitor.webapp.dashboard.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RadiationBean extends CommonBean {
    private int radNo;
    private int radValue;
}

package cn.com.nex.monitor.webapp.warn.bean;

import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class UnitWarnBean extends CommonBean {
    private int warnId;
    private String unitName;
    private String unitPath;
    private String unitId;
    private int unitStatus;
    private Date notifyTime;

    public String getNotifyTimeDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(notifyTime);
    }
}

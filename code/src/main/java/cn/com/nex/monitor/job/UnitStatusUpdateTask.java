package cn.com.nex.monitor.job;

import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.common.constant.MonitorConstant;
import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import cn.com.nex.monitor.webapp.dashboard.service.DashboardService;
import cn.com.nex.monitor.webapp.unit.service.UnitService;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import cn.com.nex.monitor.webapp.warn.bean.UnitWarnBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UnitStatusUpdateTask {
    private static final Logger LOG = LoggerFactory.getLogger(UnitStatusUpdateTask.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private MailHelper mailHelper;

    @Scheduled(cron = "${cn.com.nex.monitor.job.cron}")
    public void updateUnitStatus() {
        LOG.info(messageService.getMessage(MonitorConstant.UNIT_STATUS_JOB_START));
        try {
            dashboardService.updateUnitStatus();
            List<DashboardUnitBean> unitList = dashboardService.getNotifyUnitList();
            for (DashboardUnitBean unit : unitList) {
                List<UserBean> userList = unitService.getUnitManagers(unit.getUnitId());
                if (userList == null) {
                    continue;
                }
                List<String> addrList = new ArrayList<>();
                for (UserBean user : userList) {
                    if (user.getMailAddress() != null) {
                        addrList.add(user.getMailAddress());
                    }
                }
                if (!addrList.isEmpty()) {
                    mailHelper.send(unit, addrList);
                }
                UnitWarnBean warnBean = new UnitWarnBean();
                warnBean.setUnitId(unit.getUnitId());
                warnBean.setUnitStatus(unit.getUnitStatus());
                dashboardService.addUnitWarn(warnBean);
            }
        } catch (Exception e) {
            LOG.error(messageService.getMessage(MonitorConstant.UNIT_STATUS_JOB_ERROR), e);
            // ignore.
        }

        LOG.info(messageService.getMessage(MonitorConstant.UNIT_STATUS_JOB_END));
    }
}

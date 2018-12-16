package cn.com.nex.monitor.job;

import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.common.constant.MonitorConstant;
import cn.com.nex.monitor.webapp.dashboard.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UnitStatusUpdateTask {
    private static final Logger LOG = LoggerFactory.getLogger(UnitStatusUpdateTask.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private DashboardService dashboardService;

    @Scheduled(cron = "${cn.com.nex.monitor.job.cron}")
    public void updateUnitStatus() {
        LOG.info(messageService.getMessage(MonitorConstant.UNIT_STATUS_JOB_START));
        dashboardService.updateUnitStatus();
        // TODO: mail sending
        LOG.info(messageService.getMessage(MonitorConstant.UNIT_STATUS_JOB_END));
    }
}

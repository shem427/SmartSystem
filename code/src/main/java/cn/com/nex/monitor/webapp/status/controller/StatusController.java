package cn.com.nex.monitor.webapp.status.controller;

import cn.com.nex.monitor.webapp.setting.controller.SettingController;
import cn.com.nex.monitor.webapp.setting.service.SettingService;
import cn.com.nex.monitor.webapp.status.service.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/status")
public class StatusController {
    /**
     * LOG
     */
    private static final Logger LOG = LoggerFactory.getLogger(StatusController.class);

    @Autowired
    private StatusService statusService;

    @GetMapping(value = "/index")
    public ModelAndView page() {
        Map<String, Object> model = new HashMap<>();
        int normalLightCount = statusService.getNormalLightCount();
        int abnormalLightCount = statusService.getAbnormalLightCount();
        int activeSensorCount = statusService.getActiveSensorCount();
        int inactiveSensorCount = statusService.getInactiveSensorCount();

        model.put("normalLight", normalLightCount);
        model.put("abnormalLight", abnormalLightCount);
        model.put("activeSensor", activeSensorCount);
        model.put("inactiveSensor", inactiveSensorCount);

        return new ModelAndView("status/page", model);
    }
}

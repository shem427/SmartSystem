package cn.com.nex.monitor.webapp.status.controller;

import cn.com.nex.monitor.webapp.setting.controller.SettingController;
import cn.com.nex.monitor.webapp.setting.service.SettingService;
import cn.com.nex.monitor.webapp.status.bean.MapMarkerBean;
import cn.com.nex.monitor.webapp.status.service.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Value("${cn.com.nex.monitor.map.key}")
    private String mapKey;

    @Value("${cn.com.nex.monitor.map.ck}")
    private String mapCK;

    @Value("${cn.com.nex.monitor.top.unit}")
    private String topUnitId;

    @GetMapping(value = "/index")
    public ModelAndView page(String pUnitId) {
        String parentUnitId = null;
        if (pUnitId == null) {
            parentUnitId = topUnitId;
        } else {
            parentUnitId = pUnitId;
        }
        Map<String, Object> model = new HashMap<>();
        int normalLightCount = statusService.getNormalLightCount(parentUnitId);
        int abnormalLightCount = statusService.getAbnormalLightCount(parentUnitId);
        int[] counts = statusService.countSensor(parentUnitId);


        model.put("normalLight", normalLightCount);
        model.put("abnormalLight", abnormalLightCount);
        model.put("activeSensor", counts[0]);
        model.put("inactiveSensor", counts[1]);

        model.put("mapKey", mapKey);
        model.put("mapCK", mapCK);

        return new ModelAndView("status/page", model);
    }

    @GetMapping(value = "/markHospital")
    @ResponseBody
    public List<MapMarkerBean> markHospital() {
        return statusService.getAllHospitalLocation();
    }
}

package cn.com.nex.monitor.webapp.status.controller;

import cn.com.nex.monitor.webapp.setting.controller.SettingController;
import cn.com.nex.monitor.webapp.setting.service.SettingService;
import cn.com.nex.monitor.webapp.status.bean.MapMarkerBean;
import cn.com.nex.monitor.webapp.status.bean.StatusBean;
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
    public ModelAndView page() {
        Map<String, Object> model = new HashMap<>();
        model.put("mapKey", mapKey);
        model.put("mapCK", mapCK);

        return new ModelAndView("status/page", model);
    }

    @GetMapping(value = "/markHospital")
    @ResponseBody
    public List<MapMarkerBean> markHospital() {
        return statusService.getAllHospitalLocation();
    }

    @GetMapping(value = "/getStatus")
    @ResponseBody
    public StatusBean getStatus(String pUnitId) {
        String parentUnitId = null;
        if (pUnitId == null) {
            parentUnitId = topUnitId;
        } else {
            parentUnitId = pUnitId;
        }
        int normalLight = statusService.getNormalLightCount(parentUnitId);
        int warningLight = statusService.getWarningLightCount(parentUnitId);
        int errorLight = statusService.getErrorLightCount(parentUnitId);

        int[] sensors = statusService.countSensor(parentUnitId);

        StatusBean status = new StatusBean();
        status.setActiveSensor(sensors[0]);
        status.setInactiveSensor(sensors[1]);
        status.setErrorLight(errorLight);
        status.setNormalLight(normalLight);
        status.setWarningLight(warningLight);
//        status.setActiveSensor(52);
//        status.setInactiveSensor(5);
//        status.setErrorLight(4);
//        status.setNormalLight(40);
//        status.setWarningLight(13);

        return status;
    }
}

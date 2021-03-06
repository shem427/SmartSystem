package cn.com.nex.monitor.webapp.status.controller;

import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.common.util.MonitorUtil;
import cn.com.nex.monitor.webapp.sensor.bean.SensorBean;
import cn.com.nex.monitor.webapp.status.bean.MapMarkerBean;
import cn.com.nex.monitor.webapp.status.bean.StatusBean;
import cn.com.nex.monitor.webapp.status.service.StatusService;
import cn.com.nex.monitor.webapp.unit.bean.UnitChainBean;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @Autowired
    private MessageService messageService;

    @Value("${cn.com.nex.monitor.map.key}")
    private String mapKey;

    @Value("${cn.com.nex.monitor.map.ck}")
    private String mapCK;

    @Value("${cn.com.nex.monitor.top.unit}")
    private String topUnitId;

    @GetMapping(value = "/hospitalIndex")
    public ModelAndView hospitalIndex() {
        Map<String, Object> model = new HashMap<>();
        model.put("mapKey", mapKey);
        model.put("mapCK", mapCK);

        return new ModelAndView("status/hospitalPage", model);
    }

    @GetMapping(value = "/statusDetail")
    public ModelAndView unitStatusDetail(String unitId, String detailType) {
        String detailTitle = messageService.getMessage("mr.status.type." + detailType);

        Map<String, Object> model = new HashMap<>();
        model.put("detailTitle", detailTitle);
        model.put("unitId", unitId);
        model.put("detailType", detailType);

        String viewName;
        if ("normal".equalsIgnoreCase(detailType)
                || "abnormal".equalsIgnoreCase(detailType)) {
            viewName = "status/unitStatusDetailModal";
        } else {
            viewName = "status/sensorStatusDetailModal";
        }

        return new ModelAndView(viewName, model);
    }

    @GetMapping(value = "/getUnitStatusDetail")
    @ResponseBody
    public TableData<UnitChainBean> getUnitStatusDetail(SearchParam param, String unitId, String detailType) {
        return statusService.getUnitStatusDetail(param, unitId, detailType);
    }

    @GetMapping(value = "/getSensorStatusDetail")
    @ResponseBody
    public TableData<SensorBean> getSensorStatusDetail(SearchParam param, String unitId, String detailType) {
        return statusService.getSensorStatusDetail(param, unitId, detailType);
    }

    @GetMapping(value = "/statusIndex")
    public String statusIndex() {
        return "status/statusPage";
    }

    @GetMapping(value = "/markHospital")
    @ResponseBody
    public List<MapMarkerBean> markHospital() {
        return statusService.getAllHospitalLocation();
    }

    @GetMapping(value = "/getStatus")
    @ResponseBody
    public StatusBean getStatus(String pUnitId) {
        StatusBean status = statusService.getUnitInfo(pUnitId);

        int normalLight = statusService.getNormalLightCount(pUnitId);
        int warningLight = statusService.getWarningLightCount(pUnitId);
        int errorLight = statusService.getErrorLightCount(pUnitId);

        int[] sensors = statusService.countSensor(pUnitId);

        status.setActiveSensor(sensors[0]);
        status.setInactiveSensor(sensors[1]);
        status.setErrorLight(errorLight);
        status.setNormalLight(normalLight);
        status.setWarningLight(warningLight);

        return status;
    }

    @GetMapping(value = "/getUnitStatus")
    @ResponseBody
    public StatusBean getUnitStatus(String unitId) {
        StatusBean bean;
        try {
            if (unitId == null) {
                bean = new StatusBean();
                return bean;
            } else {
                UserBean user = MonitorUtil.getUserFromSecurity();
                bean = statusService.getUnitStatus(unitId, user.getUserId());
            }
        } catch (Exception e) {
            bean = new StatusBean();
            MonitorUtil.handleException(e, bean, messageService);
        }
        return bean;
    }
}

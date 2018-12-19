package cn.com.nex.monitor.webapp.sensor.controller;

import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import cn.com.nex.monitor.webapp.common.bean.NumericBean;
import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.common.constant.MonitorConstant;
import cn.com.nex.monitor.webapp.common.util.MonitorUtil;
import cn.com.nex.monitor.webapp.sensor.bean.SensorBean;
import cn.com.nex.monitor.webapp.sensor.service.SensorService;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/sensor")
public class SensorController {
    /** LOG */
    private static final Logger LOG = LoggerFactory.getLogger(SensorController.class);

    @Autowired
    private SensorService sensorService;

    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/index")
    @PreAuthorize("hasRole('ADMIN')")
    public String page() {
        return "sensor/page";
    }

    @GetMapping(value = "/sensorModal")
    public String sensorModal(Model model, String sensorId) {
        if (sensorId != null) {
            model.addAttribute("isCreate", false);
            SensorBean sensorBean = sensorService.getSensorById(sensorId);
            if (sensorBean != null) {
                model.addAttribute("sensorId", sensorBean.getSensorId());
                model.addAttribute("sensorName", sensorBean.getSensorName());
                model.addAttribute("sensorModel", sensorBean.getSensorModel());
                model.addAttribute("sensorSn", sensorBean.getSensorSn());
                model.addAttribute("sensorRemark", sensorBean.getSensorRemark());
                model.addAttribute("unitId", sensorBean.getUnitId());
                model.addAttribute("unitFullPath", sensorBean.getUnitFullPath());
            } else {
                throw new IllegalArgumentException(sensorId + " do not exist!");
            }
        }

        return "sensor/sensorUpdateModal";
    }

    @ResponseBody
    @GetMapping(value = "/getSensors")
    @PreAuthorize("hasRole('ADMIN')")
    public TableData<SensorBean> getSensors(SearchParam param, String sensorNameLike, String sensorModelLike) {
        TableData<SensorBean> tableData = new TableData<>();
        try {
            tableData = sensorService.searchSensor(param, sensorNameLike, sensorModelLike);
        } catch(Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            tableData.setStatus(CommonBean.Status.ERROR);
            tableData.setMessage(message);
            LOG.error(message, e);
        }

        return tableData;
    }

    @ResponseBody
    @PostMapping(value = "/deleteSensors")
    @PreAuthorize("hasRole('ADMIN')")
    public NumericBean deleteSensors(@RequestBody List<String> sensorIds) {
        NumericBean bean = new NumericBean();

        try {
            int count = sensorService.deleteSensor(sensorIds);
            bean.setNumber(count);
        } catch (Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            bean.setStatus(CommonBean.Status.ERROR);
            bean.setMessage(message);
            LOG.error(message, e);
        }
        return bean;
    }

    @ResponseBody
    @PostMapping(value = "/saveSensor")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonBean saveSensor(SensorBean sensor, boolean isCreate) {
        CommonBean bean = new CommonBean();
        try {
            if (isCreate) {
                sensorService.addSensor(sensor);
            } else {
                sensorService.updateSensor(sensor);
            }
        } catch (Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            bean.setStatus(CommonBean.Status.ERROR);
            bean.setMessage(message);
            LOG.error(message, e);
        }
        return bean;
    }
}

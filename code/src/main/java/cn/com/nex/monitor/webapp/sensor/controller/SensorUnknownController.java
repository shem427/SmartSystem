package cn.com.nex.monitor.webapp.sensor.controller;

import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.common.constant.MonitorConstant;
import cn.com.nex.monitor.webapp.data.service.DataService;
import cn.com.nex.monitor.webapp.sensor.bean.SensorUnknowBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/sensorUnknown")
public class SensorUnknownController {
    /** LOG */
    private static final Logger LOG = LoggerFactory.getLogger(SensorController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private DataService dataService;

    @GetMapping(value = "/index")
    @PreAuthorize("hasRole('ADMIN')")
    public String page() {
        return "sensorUnknown/page";
    }

    @ResponseBody
    @GetMapping(value = "/getSensors")
    @PreAuthorize("hasRole('ADMIN')")
    public TableData<SensorUnknowBean> getSensors(SearchParam param) {
        TableData<SensorUnknowBean> tableData;
        try {
            tableData = dataService.getUnknownSensors(param);
        } catch(Exception e) {
            tableData = new TableData<>();
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            tableData.setStatus(CommonBean.Status.ERROR);
            tableData.setMessage(message);
            LOG.error(message, e);
        }

        return tableData;
    }

    @GetMapping(value = "/sensorModal")
    @PreAuthorize("hasRole('ADMIN')")
    public String sensorModal(Model model, String radiationModelId) {
        model.addAttribute("radiationModelId", radiationModelId);
        return "sensorUnknown/sensorUpdateModal";
    }

    @ResponseBody
    @PostMapping(value = "/moveData")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonBean moveData(String radiationModelId) {
        CommonBean bean = new CommonBean();
        try {
            dataService.moveData(radiationModelId);
        } catch (Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            bean.setStatus(CommonBean.Status.ERROR);
            bean.setMessage(message);
            LOG.error(message, e);
        }
        return bean;
    }
}

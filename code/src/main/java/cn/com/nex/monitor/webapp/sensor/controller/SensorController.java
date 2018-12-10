package cn.com.nex.monitor.webapp.sensor.controller;

import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.sensor.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String page() {
        return "sensor/page";
    }
}

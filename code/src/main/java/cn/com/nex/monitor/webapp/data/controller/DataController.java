package cn.com.nex.monitor.webapp.data.controller;

import cn.com.nex.monitor.webapp.dashboard.controller.DashboardController;
import cn.com.nex.monitor.webapp.data.bean.DataBean;
import cn.com.nex.monitor.webapp.data.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/data")
public class DataController {
    /** LOG */
    private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DataService dataService;

    @ResponseBody
    @PostMapping(value = "/setData")
    public String setData(DataBean data) {
        return null;
    }
}

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
    private static final String MSG_PREFIX = "$$$";
    private static final String MSG_SUFFIX = "===";

    @Autowired
    private DataService dataService;

    @PostMapping(value = "/upload")
    public String uploadData(DataBean data) {
        boolean valid = isMsgValid(data.getMsg());
        if (!valid) {
            throw new IllegalArgumentException("msg not correct. msg=" + data.getMsg());
        }
        dataService.uploadData(data);

        return "sucess";
    }

    private boolean isMsgValid(String msg) {
        return msg.startsWith(MSG_PREFIX) && msg.endsWith(MSG_SUFFIX);
    }
}

package cn.com.nex.monitor.webapp.data.controller;

import cn.com.nex.monitor.webapp.dashboard.controller.DashboardController;
import cn.com.nex.monitor.webapp.data.bean.DataBean;
import cn.com.nex.monitor.webapp.data.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/data")
public class DataController {
    /** LOG */
    private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);
    private static final String MSG_PREFIX = "$$$";
    private static final String MSG_SUFFIX = "===";

    private static final String MSG_KEY = "msg";
    private static final String ID_KEY = "ID";
    private static final String VALUE_KEY = "value";

    @Autowired
    private DataService dataService;

    @PostMapping(value = "/upload")
    public String uploadData(@RequestBody Map<String,String> params) {
        String msg = params.get(MSG_KEY);
        String id = params.get(ID_KEY);
        String value = params.get(VALUE_KEY);

        LOG.info("msg=" + msg + "|ID=" + id + "|value="+value);

        if (msg == null) {
            throw new IllegalArgumentException("msg not found.");
        }
        if (id == null) {
            throw new IllegalArgumentException("ID not found.");
        }
        if (!isMsgValid(msg)) {
            throw new IllegalArgumentException("msg not correct. msg=" + msg);
        }

        DataBean bean = new DataBean();
        bean.setID(id);
        bean.setMsg(msg);
        bean.setValue(Integer.valueOf(value));

        dataService.uploadData(bean);

        return "sucess";
    }

    private boolean isMsgValid(String msg) {
        if (msg == null) {
            return false;
        }
        return msg.startsWith(MSG_PREFIX) && msg.endsWith(MSG_SUFFIX);
    }
}

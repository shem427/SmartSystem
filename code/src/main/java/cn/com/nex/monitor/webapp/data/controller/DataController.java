package cn.com.nex.monitor.webapp.data.controller;

import cn.com.nex.monitor.webapp.data.bean.DataBean;
import cn.com.nex.monitor.webapp.data.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/data")
public class DataController {
    /** LOG */
    private static final Logger LOG = LoggerFactory.getLogger(DataController.class);
    private static final String MSG_PREFIX = "$$$";
    private static final String MSG_SUFFIX = "===";

    private static final String MSG_KEY = "msg";
    private static final String ID_KEY = "ID";
    private static final String VALUE_KEY = "value";
    private static final String POWER_KEY = "power";

    @Autowired
    private DataService dataService;

    @PostMapping(value = "/upload")
    public String uploadData(@RequestBody Map<String, String> params) {
        String msg = params.get(MSG_KEY);
        String id = params.get(ID_KEY);
        String value = params.get(VALUE_KEY);
        String power = params.get(POWER_KEY);

        LOG.info("msg=" + msg + "|ID=" + id + "|value="+value + "|power=" + power);

        if (msg == null) {
            throw new IllegalArgumentException("msg not found.");
        }
        if (id == null) {
            throw new IllegalArgumentException("ID not found.");
        }
        if (!isMsgValid(msg)) {
            throw new IllegalArgumentException("msg not correct. msg=" + msg);
        }

        double realValue = getValue(value);
        if (realValue <= 0.0d) {
            LOG.warn("msg=" + msg + "|ID=" + id + "|value="+value + "|power=" + power + "; not valid. data not saved.");
        } else {
            DataBean bean = new DataBean();
            bean.setID(id);
            bean.setMsg(msg);
            bean.setValue(realValue);
            bean.setPower(getPower(power));
            bean.setUploadTime(new Date());

            dataService.uploadData(bean);
        }

        return "sucess";
    }

    private boolean isMsgValid(String msg) {
        if (msg == null) {
            return false;
        }
        return msg.startsWith(MSG_PREFIX) && msg.endsWith(MSG_SUFFIX);
    }

    private double getPower(String power) {
        if (power == null) return -1d;
        String tmp = power.trim();
        if (power.endsWith("V") || power.endsWith("v")) {
            tmp = tmp.substring(0, tmp.length() - 1);
        }
        return Double.valueOf(tmp);
    }

    private double getValue(String value) {
        if (value == null) {
            return 0;
        }
        String[] t = value.split(",");
        if (t.length != 2) {
            return 0;
        }
        long h = Long.parseLong(t[0], 16);
        long l = Long.parseLong(t[1], 16);
        if (h == 0L && l == 0L) {
            return 0.0d;
        }

        double realValue = 0.0362d * (510 * ((h * 16 + l) * (3.3 / 4096) / 2) / 0.8 - 75.5) + 0.5481;
        if (realValue <= 0.0d) {
            return 0.0d;
        } else {
            return realValue;
        }
    }
}

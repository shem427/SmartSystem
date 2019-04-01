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

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private HttpServletRequest request;

//    @PostMapping(value = "/upload")
//    public String uploadData(DataBean data) {
//        boolean valid = isMsgValid(data.getMsg());
//        if (!valid) {
//            throw new IllegalArgumentException("msg not correct. msg=" + data.getMsg());
//        }
//        dataService.uploadData(data);
//
//        return "sucess";
//    }
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

//    @PostMapping(value = "/upload")
//    public String uploadData(String data) {
//        LOG.info("receive data=" + data);
//        Gson gson = new Gson();
//        DataBean bean = gson.fromJson(data, DataBean.class);
//
//        boolean valid = isMsgValid(bean.getMsg());
//        if (!valid) {
//            throw new IllegalArgumentException("msg not correct. msg=" + bean.getMsg());
//        }
//        dataService.uploadData(bean);
//
//        return "sucess";
//    }

//    @PostMapping(value = "/upload")
//    public String uploadData() {
//        Map<String, String[]> paramMap = request.getParameterMap();
//        Iterator<String> it = paramMap.keySet().iterator();
//        String[] values;
//        String json = null;
//        while (it.hasNext()) {
//            String key = it.next();
//            LOG.info("request param: key=" + key + "|value=" + paramMap.get(key)[0]);
//            if (key.startsWith(REQ_PREFIX)) {
//                values = paramMap.get(key);
//                if(values.length == 0) {
//                    continue;
//                }
//                if (values[0] == null || values[0].trim().length() == 0) {
//                    json = key;
//                } else {
//                    json = key + "=" + values[0].trim();
//                }
//            }
//        }
//        if (json == null) {
//            throw new IllegalArgumentException("msg not correct.");
//        }
//        LOG.info("receive data=" + json);
//        Gson gson = new Gson();
//        DataBean bean = gson.fromJson(json, DataBean.class);
//        boolean valid = isMsgValid(bean.getMsg());
//        if (!valid) {
//            throw new IllegalArgumentException("msg not correct. msg=" + bean.getMsg());
//        }
//        dataService.uploadData(bean);
//
//        return "sucess";
//    }
}

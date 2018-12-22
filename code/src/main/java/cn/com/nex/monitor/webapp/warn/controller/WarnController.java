package cn.com.nex.monitor.webapp.warn.controller;

import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.common.constant.MonitorConstant;
import cn.com.nex.monitor.webapp.common.util.MonitorUtil;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import cn.com.nex.monitor.webapp.warn.bean.UnitWarnBean;
import cn.com.nex.monitor.webapp.warn.service.WarnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/warn")
public class WarnController {
    /**
     * LOG
     */
    private static Logger LOG = LoggerFactory.getLogger(WarnController.class);

    @Autowired
    private WarnService warnService;

    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/index")
    public String index() {
        return "warn/page";
    }

    @GetMapping(value = "/getWarns")
    @ResponseBody
    public TableData<UnitWarnBean> getWarns(SearchParam searchParam, String begin, String end) {
        Date beginDate = null;
        Date endDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            if (!StringUtils.isEmpty(begin)) {
                beginDate = sdf.parse(begin + " 00:00:00.000");
            }
            if (!StringUtils.isEmpty(end)) {
                endDate = sdf.parse(end + " 23:59:59.000");
            }
            UserBean user = MonitorUtil.getUserFromSecurity();
            return warnService.getWarns(searchParam, user.getUserId(), beginDate, endDate);
        } catch (Exception e) {
            TableData<UnitWarnBean> bean = new TableData<>();
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            bean.setStatus(CommonBean.Status.ERROR);
            bean.setMessage(message);
            LOG.error(message, e);
            return bean;
        }
    }
}

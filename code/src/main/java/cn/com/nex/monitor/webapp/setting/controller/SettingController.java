package cn.com.nex.monitor.webapp.setting.controller;

import cn.com.nex.monitor.webapp.common.CommonBean;
import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.common.MonitorConstant;
import cn.com.nex.monitor.webapp.common.MonitorUtil;
import cn.com.nex.monitor.webapp.setting.bean.ChangePassword;
import cn.com.nex.monitor.webapp.setting.service.SettingService;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/setting")
public class SettingController {

    /**
     * LOG
     */
    private static Logger LOG = LoggerFactory.getLogger(SettingController.class);

    @Autowired
    private SettingService settingService;

    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/changePassword")
    public String changePasswordPage() {
        return "setting/changePassword";
    }

    @GetMapping(value = "/updateProfile")
    public ModelAndView updateProfilePage() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        UserBean user = (UserBean) auth.getPrincipal();

        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        return new ModelAndView("setting/updateProfile", model);
    }

    @PostMapping(value = "/changePassword")
    @ResponseBody
    public CommonBean changePassword(ChangePassword cpBean) {
        CommonBean bean = new CommonBean();
        UserBean user = MonitorUtil.getUserFromSecurity();
        try {
            boolean valid = settingService.checkOldPassword(user.getUserId(), cpBean.getOldPassword());
            if (!valid) {
                bean.setStatus(CommonBean.Status.WARNING);
                bean.setMessage(messageService.getMessage("mr.change.password.invalid.old"));
                return bean;
            }
            String password = settingService.changePassword(user.getUserId(), cpBean.getNewPassword());
            user.setPassword(password);
        } catch (Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            LOG.error(message, e);
            bean.setStatus(CommonBean.Status.ERROR);
            bean.setMessage(message);
        }

        return bean;
    }

    @PostMapping(value = "/updateProfile")
    @ResponseBody
    public CommonBean updateProfile(UserBean profile) {
        CommonBean bean = new CommonBean();
        try {
            settingService.updateProfile(profile);
            UserBean user = MonitorUtil.getUserFromSecurity();
            user.setMailAddress(profile.getMailAddress());
            user.setName(profile.getName());
            user.setPhoneNumber(profile.getPhoneNumber());
        } catch (Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            LOG.error(message, e);
            bean.setStatus(CommonBean.Status.ERROR);
            bean.setMessage(message);
        }

        return bean;
    }
}

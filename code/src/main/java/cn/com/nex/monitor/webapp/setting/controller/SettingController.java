package cn.com.nex.monitor.webapp.setting.controller;

import cn.com.nex.monitor.webapp.common.CommonBean;
import cn.com.nex.monitor.webapp.setting.bean.ChangePasswordBean;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping(value = "/setting")
public class SettingController {

    @GetMapping(value = "/changePassword")
    public String page() {
        return "setting/changePassword";
    }

    @ResponseBody
    @PostMapping(value = "/savePassword")
    public CommonBean savePassword(Authentication authentication, ChangePasswordBean changePasswordBean) {
        UserBean user = (UserBean) authentication.getPrincipal();
        CommonBean retBean = new CommonBean();

        return retBean;
    }
}

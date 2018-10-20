package cn.com.nex.monitor.webapp.user.controller;

import cn.com.nex.monitor.webapp.common.CommonBean;
import cn.com.nex.monitor.webapp.dashboard.bean.DashboardBean;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private static List<UserBean> userList = null;

    @GetMapping(value = "/index")
    public String page() {
        return "user/page";
    }

    @GetMapping(value = "/userModal")
    public String userModal(Model model, String userId) {
        boolean found = false;
        if (userId != null) {
            model.addAttribute("isCreate", false);
            for (UserBean user : userList) {
                if (user.getUserId().equals(userId)) {
                    model.addAttribute("id", user.getUserId());
                    model.addAttribute("name", user.getName());
                    model.addAttribute("mail", user.getMailAddress());
                    found = true;
                    break;
                }
            }
            if (!found) {
                model.addAttribute("id", "");
                model.addAttribute("name", "");
                model.addAttribute("mail", "");
            }
        } else {
            model.addAttribute("id", "");
            model.addAttribute("name", "");
            model.addAttribute("mail", "");
            model.addAttribute("isCreate", true);
        }

        return "user/userModal";
    }

    @ResponseBody
    @GetMapping(value = "/getUsers")
    public List<UserBean> getUsers() {
        if (userList == null) {
            userList = new ArrayList<>();
            UserBean bean;
            // 1
            for (int i = 0; i < 10; i++) {
                String indx = String.valueOf(i);
                bean = new UserBean();
                bean.setUserId(StringUtils.leftPad(indx, 6, '0'));
                bean.setName("user-" + StringUtils.leftPad(indx, 3, '0'));
                userList.add(bean);
            }
        }
        return userList;
    }

    @ResponseBody
    @PostMapping(value = "/saveUser")
    public CommonBean saveUser(UserBean user, boolean isCreate) {
        if (isCreate) {
            userList.add(user);
        } else {
            for (UserBean bean : userList) {
                if (bean.getUserId().equals(user.getUserId())) {
                    bean.setName(user.getName());
                    bean.setMailAddress(user.getMailAddress());
                    break;
                }
            }
        }
        return new CommonBean();
    }

    @GetMapping(value = "/export")
    public ResponseEntity<FileSystemResource> export() {
        return null;
    }
}

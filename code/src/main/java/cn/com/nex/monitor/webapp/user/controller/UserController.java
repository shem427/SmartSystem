package cn.com.nex.monitor.webapp.user.controller;

import cn.com.nex.monitor.webapp.common.*;
import cn.com.nex.monitor.webapp.common.bean.NumericBean;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import cn.com.nex.monitor.webapp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/index")
    public String page() {
        return "user/page";
    }

    @GetMapping(value = "/userModal")
    public String userModal(Model model, String userId) {
        if (userId != null) {
            model.addAttribute("isCreate", false);
            UserBean userBean = userService.getUserById(userId);
            if (userBean != null) {
                model.addAttribute("id", userBean.getUserId());
                model.addAttribute("name", userBean.getName());
                model.addAttribute("mail", userBean.getMailAddress());
            } else {
                throw new UsernameNotFoundException(userId + " do not exist!");
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
    public TableData<UserBean> getUsers(SearchParam param, String userIdLike, String nameLike) {
        TableData<UserBean> tableData = new TableData<>();
        try {
            tableData = userService.searchUser(param, userIdLike, nameLike);
        } catch(Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            tableData.setStatus(CommonBean.Status.ERROR);
            tableData.setMessage(message);
        }

        return tableData;
    }

    @ResponseBody
    @PostMapping(value = "/deleteUsers")
    public NumericBean deleteUsers(@RequestBody List<String> userIds) {
        NumericBean bean = new NumericBean();
        try {
            int count = userService.deleteUser(userIds);
            bean.setNumber(count);
        } catch (Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            bean.setStatus(CommonBean.Status.ERROR);
            bean.setMessage(message);
        }
        return bean;
    }
}

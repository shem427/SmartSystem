package cn.com.nex.monitor.webapp.user.controller;

import cn.com.nex.monitor.webapp.common.*;
import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import cn.com.nex.monitor.webapp.common.bean.NumericBean;
import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.common.constant.MonitorConstant;
import cn.com.nex.monitor.webapp.common.util.MonitorUtil;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import cn.com.nex.monitor.webapp.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    /**
     * LOG
     */
    private static Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @Value("${cn.com.nex.monitor.roles}")
    private String allRoles;

    @GetMapping(value = "/index")
    public String page() {
        return "user/page";
    }

    @GetMapping(value = "/userModal")
    public String userModal(Model model, String userId) {
        model.addAttribute("allRoles", getUserRoleMap());
        if (userId != null) {
            model.addAttribute("isCreate", false);
            model.addAttribute("isSelf", MonitorUtil.getUserFromSecurity().getUserId().equals(userId));
            UserBean userBean = userService.getUserById(userId);
            if (userBean != null) {
                model.addAttribute("id", userBean.getUserId());
                model.addAttribute("name", userBean.getName());
                model.addAttribute("mail", userBean.getMailAddress());
                model.addAttribute("phone", userBean.getPhoneNumber());
                model.addAttribute("userRoles", getUserRoleList(userBean.getUserRoles()));
            } else {
                throw new UsernameNotFoundException(userId + " do not exist!");
            }
        } else {
            model.addAttribute("id", "");
            model.addAttribute("name", "");
            model.addAttribute("mail", "");
            model.addAttribute("phone", "");
            model.addAttribute("userRoles", new ArrayList<String>());
            model.addAttribute("isCreate", true);
        }

        return "user/userUpdateModal";
    }

    @GetMapping(value = "/userSelectModal")
    public String userSelectModal() {
        return "user/userSelectModal";
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
            LOG.error(message, e);
        }

        return tableData;
    }

    @ResponseBody
    @PostMapping(value = "/deleteUsers")
    public NumericBean deleteUsers(@RequestBody List<String> userIds) {
        NumericBean bean = new NumericBean();
        UserBean self = MonitorUtil.getUserFromSecurity();
        if (userIds.contains(self.getUserId())) {
            String message = messageService.getMessage(MonitorConstant.CAN_NOT_DELETE_SELF);
            bean.setStatus(CommonBean.Status.WARNING);
            bean.setMessage(message);
            LOG.warn(message);
            return bean;
        }

        try {
            int count = userService.deleteUser(userIds);
            bean.setNumber(count);
        } catch (Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            bean.setStatus(CommonBean.Status.ERROR);
            bean.setMessage(message);
            LOG.error(message, e);
        }
        return bean;
    }

    @ResponseBody
    @PostMapping(value = "/saveUser")
    public CommonBean saveUser(UserBean user, boolean isCreate) {
        CommonBean bean = new CommonBean();
        try {
            if (isCreate) {
                userService.addUser(user);
            } else {
                userService.updateUser(user);
                UserBean self = MonitorUtil.getUserFromSecurity();
                if (user.getUserId().equals(self.getUserId())) {
                    self.setMailAddress(user.getMailAddress());
                    self.setPhoneNumber(user.getPhoneNumber());
                    self.setName(user.getName());
                    self.setUserRoles(user.getUserRoles());
                }
            }
        } catch (Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            bean.setStatus(CommonBean.Status.ERROR);
            bean.setMessage(message);
            LOG.error(message, e);
        }
        return bean;
    }

    private List<String> getUserRoleList(String roleStr) {
        if (StringUtils.isEmpty(roleStr)) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(roleStr.split(MonitorConstant.ROLE_SEPERATOR));
        }
    }

    private Map<String, String> getUserRoleMap() {
        String[] allRoleIdArray = allRoles.split(MonitorConstant.ROLE_SEPERATOR);
        Map<String, String> roleMap = new HashMap<>();

        for (String role : allRoleIdArray) {
            String code = "mr.role." + role;
            roleMap.put(role, messageService.getMessage(code));
        }

        return roleMap;
    }
}

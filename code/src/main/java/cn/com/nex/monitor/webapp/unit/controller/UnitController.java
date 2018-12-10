package cn.com.nex.monitor.webapp.unit.controller;

import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import cn.com.nex.monitor.webapp.common.constant.MonitorConstant;
import cn.com.nex.monitor.webapp.unit.bean.UnitBean;
import cn.com.nex.monitor.webapp.unit.service.UnitService;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/unit")
public class UnitController {
    /** LOG */
    private static final Logger LOG = LoggerFactory.getLogger(UnitController.class);

    @Autowired
    private UnitService unitService;

    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/index")
    public String page() {
        return "unit/page";
    }

    /**
     * 获取下位组织信息。
     * @param id ID
     * @return 下位组织信息
     */
    @GetMapping(value = "/subUnit")
    @ResponseBody
    public List<UnitBean> getSubUnit(String id) {
        return unitService.getSubUnit(id);
    }

    @GetMapping(value = "/addPage")
    public ModelAndView getAddPage(String parentId, String parentName) {
        Map<String, Object> model = new HashMap<>();
        model.put("parentId", parentId);
        model.put("parentName", parentName);

        return new ModelAndView("unit/unitUpdateModal", model);
    }

    @GetMapping(value = "/editPage")
    public ModelAndView getEditPage(String parentId,
                                    String parentName,
                                    String unitId,
                                    String unitName,
                                    String unitRemark) {
        Map<String, Object> model = new HashMap<>();
        List<UserBean> managers = unitService.getUnitManagers(unitId);

        model.put("parentId", parentId);
        model.put("parentName", parentName);
        model.put("unitId", unitId);
        model.put("unitName", unitName);
        model.put("unitRemark", unitRemark);
        model.put("managers", managers);

        return new ModelAndView("unit/unitUpdateModal", model);
    }

    @PostMapping(value = "/saveUnit")
    @ResponseBody
    public CommonBean saveUnit(UnitBean unit) {

        CommonBean bean = new CommonBean();
        try {
            String unitId = unit.getId();
            if (unitId != null) {
                // edit
                unitService.edit(unit);
            } else {
                // add
                unitService.add(unit);
            }
        } catch (Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            LOG.error(message, e);
            bean.setStatus(CommonBean.Status.ERROR);
            bean.setMessage(message);
        }

        return bean;
    }

    @PostMapping(value = "deleteUnit")
    @ResponseBody
    public CommonBean deleteUnit(String unitId) {

        CommonBean retBean = new CommonBean();
        try {
            boolean hasChildren = unitService.hasChildren(unitId);
            if (hasChildren) {
                // 有下位组织，不能删除
                String message = messageService.getMessage("mr.delete.unit.hasChildren");
                retBean.setStatus(CommonBean.Status.WARNING);
                retBean.setMessage(message);
            } else {
                unitService.delete(unitId);
            }
        } catch (Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            LOG.error(message, e);
            retBean.setStatus(CommonBean.Status.ERROR);
            retBean.setMessage(message);
        }
        return retBean;
    }

    @GetMapping(value = "getManagers")
    @ResponseBody
    public List<UserBean> getUnitManagers(String unitId) {
        List<UserBean> managerList = unitService.getUnitManagers(unitId);
        return managerList;
    }
}

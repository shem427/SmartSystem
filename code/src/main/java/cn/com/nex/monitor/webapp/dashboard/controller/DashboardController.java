package cn.com.nex.monitor.webapp.dashboard.controller;

import cn.com.nex.monitor.job.UnitStatusUpdateTask;
import cn.com.nex.monitor.webapp.common.MessageService;
import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.common.constant.DBConstant;
import cn.com.nex.monitor.webapp.common.constant.MonitorConstant;
import cn.com.nex.monitor.webapp.common.util.MonitorUtil;
import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import cn.com.nex.monitor.webapp.dashboard.bean.RadiationBean;
import cn.com.nex.monitor.webapp.dashboard.bean.UnitDataBean;
import cn.com.nex.monitor.webapp.dashboard.service.DashboardService;
import cn.com.nex.monitor.webapp.setting.bean.ThresholdBean;
import cn.com.nex.monitor.webapp.setting.service.SettingService;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController {
    /** LOG */
    private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);

    @Value("${cn.com.nex.monitor.top.unit}")
    private String topUnitId;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/index")
    public ModelAndView page(String parentId, Integer level) {
        String pId;
        if (parentId == null) {
            pId = topUnitId;
        } else {
            pId = parentId;
        }

        if (level == null) {
            level = 0;
        }
        boolean isParent = dashboardService.isParentUnit(pId);
        Map<String, Object> model = new HashMap<>();
        if (isParent) {
            return showUnitStatus(model, pId, level);
        } else {
            return showUnitDetail(model, pId, level);
        }
    }

    @GetMapping(value = "/upIndex")
    public ModelAndView upIndexpage(String currentId, Integer level) {
        String parentId = dashboardService.getParentIdByUnitId(currentId);
        Map<String, Object> model = new HashMap<>();
        return showUnitStatus(model, parentId, level);
    }

    private ModelAndView showUnitStatus(Map<String, Object> model, String parentId, int level) {
        model.put("level", level);
        model.put("parentId", parentId);
        if (UnitStatusUpdateTask.isUpdating) {
            return new ModelAndView("dashboard/unitStatusUpdating", model);
        } else {
            UserBean user = MonitorUtil.getUserFromSecurity();
            Collection<DashboardUnitBean> dashboardUnitList = dashboardService
                    .getUnitListByManagerAndParent(user.getUserId(), parentId);
            model.put("units", dashboardUnitList);

            return new ModelAndView("dashboard/page", model);
        }
    }

    private ModelAndView showUnitDetail(Map<String, Object> model, String unitId, Integer level) {
        model.put("unitId", unitId);
        model.put("level", level);
        return new ModelAndView("dashboard/unitDataGraphic", model);
    }

    @ResponseBody
    @GetMapping(value = "/getUnitDatas")
    public UnitDataBean getUnitRadiationData(String unitId) {
        UnitDataBean bean = new UnitDataBean();
        int normal = 0;
        int warning = 0;
        int error = 0;
        try {
            List<Integer> datas = dashboardService.getUnitRadiationData(unitId);
            ThresholdBean threshold = settingService.getThreshold(DBConstant.RADIATION_THRESHOLD);
            bean.setUnidId(unitId);
            bean.setUnitDatas(datas);
            for (int v : datas) {
                if (v >= threshold.getNorml()) {
                    normal++;
                } else if (v < threshold.getWarn()) {
                    error++;
                } else {
                    warning++;
                }
            }
            bean.setNormalCount(normal);
            bean.setWarningCount(warning);
            bean.setErrorCount(error);
        } catch (Exception e) {
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            bean.setStatus(CommonBean.Status.ERROR);
            bean.setMessage(message);
            LOG.error(message, e);
        }

        return bean;
    }

    @GetMapping(value = "/getRadiationData")
    @ResponseBody
    public TableData<RadiationBean> getRadiationData(SearchParam searchParam, String unitId) {
        TableData<RadiationBean> tableData;
        try {
            tableData = dashboardService.getRadiationData(searchParam, unitId);
        } catch (Exception e) {
            tableData = new TableData<>();
            String message = messageService.getMessage(MonitorConstant.LOG_ERROR);
            tableData.setStatus(CommonBean.Status.ERROR);
            tableData.setMessage(message);
            LOG.error(message, e);
        }

        return tableData;
    }
}

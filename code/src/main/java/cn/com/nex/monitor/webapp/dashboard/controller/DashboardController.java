package cn.com.nex.monitor.webapp.dashboard.controller;

import cn.com.nex.monitor.job.UnitStatusUpdateTask;
import cn.com.nex.monitor.webapp.common.util.MonitorUtil;
import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import cn.com.nex.monitor.webapp.dashboard.service.DashboardService;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController {
    @Value("${cn.com.nex.monitor.top.unit}")
    private String topUnitId;

    @Autowired
    private DashboardService dashboardService;

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
            return showUnitDetail();
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

    private ModelAndView showUnitDetail() {
        return new ModelAndView("dashboard/unitDataGraphic");
    }

    /*@ResponseBody
    @GetMapping(value = "/getData")
    public DashboardBean getDashboardData() {
        DashboardBean bean = new DashboardBean();

        bean.getRadiationDatas().add(10.4d);
        bean.getRadiationDatas().add(14.3d);
        bean.getRadiationDatas().add(10.5d);
        bean.getRadiationDatas().add(28.7d);
        bean.getRadiationDatas().add(28.4d);

        bean.getTemperatureDatas().add(22.5d);
        bean.getTemperatureDatas().add(1.2d);
        bean.getTemperatureDatas().add(33.6d);
        bean.getTemperatureDatas().add(68.4d);
        bean.getTemperatureDatas().add(39.1d);

        bean.getHumidityDatas().add(7.8d);
        bean.getHumidityDatas().add(42.8d);
        bean.getHumidityDatas().add(8.3d);
        bean.getHumidityDatas().add(0.9d);
        bean.getHumidityDatas().add(0.2d);

        bean.getPm25Datas().add(16.6d);
        bean.getPm25Datas().add(60.3d);
        bean.getPm25Datas().add(2.1d);
        bean.getPm25Datas().add(42.6d);
        bean.getPm25Datas().add(37.5d);

        return bean;
    }*/
}

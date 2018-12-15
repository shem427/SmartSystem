package cn.com.nex.monitor.webapp.dashboard.controller;

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

import java.util.HashMap;
import java.util.List;
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
        Map<String, Object> model = new HashMap<>();
        UserBean user = MonitorUtil.getUserFromSecurity();

        List<DashboardUnitBean> dashboardUnitList =  dashboardService
                .getUnitListByManagerAndParent(user.getUserId(), pId);
        model.put("units", dashboardUnitList);
        model.put("level", level);

        return new ModelAndView("dashboard/page", model);
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

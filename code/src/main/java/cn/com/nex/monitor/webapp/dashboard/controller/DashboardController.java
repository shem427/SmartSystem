package cn.com.nex.monitor.webapp.dashboard.controller;

import cn.com.nex.monitor.webapp.dashboard.bean.DashboardBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController {

    @GetMapping(value = "/index")
    public String page() {
        return "dashboard/page";
    }

    @ResponseBody
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
    }
}

package cn.com.nex.monitor.webapp.dashboard.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DashboardBean {
    private List<Double> radiationDatas = new ArrayList<>();
    private List<Double> temperatureDatas = new ArrayList<>();
    private List<Double> humidityDatas = new ArrayList<>();
    private List<Double> pm25Datas = new ArrayList<>();
}

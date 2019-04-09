package cn.com.nex.monitor.webapp.status.service;

import cn.com.nex.monitor.webapp.common.util.MonitorUtil;
import cn.com.nex.monitor.webapp.sensor.dao.SensorDao;
import cn.com.nex.monitor.webapp.status.bean.MapMarkerBean;
import cn.com.nex.monitor.webapp.unit.bean.UnitBean;
import cn.com.nex.monitor.webapp.unit.dao.UnitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusService {
    @Autowired
private UnitDao unitDao;
    @Autowired
    private SensorDao sensorDao;

    @Value("${cn.com.nex.monitor.top.unit}")
    private String topUnitId;

    @Value("${cn.com.nex.monitor.map.key}")
    private String mapKey;

    @Value("${cn.com.nex.monitor.map.ck}")
    private String mapCK;

    public int getNormalLightCount(String parentUnitId) {
        return unitDao.countUnitByStatus(0, parentUnitId) + unitDao.countUnitByStatus(-1, parentUnitId);
    }

    public int getWarningLightCount(String parentUnitId) {
        return unitDao.countUnitByStatus(1, parentUnitId);
    }

    public int getErrorLightCount(String parentUnitId) {
        return unitDao.countUnitByStatus(2, parentUnitId);
    }

    public int[] countSensor(String parentUnitId) {
        return sensorDao.countSensor(parentUnitId);
    }

    public List<MapMarkerBean> getAllHospitalLocation() {
        List<MapMarkerBean> markerList = new ArrayList<>();
        List<UnitBean> unitList = unitDao.getUnitByParentId(topUnitId);
        for (UnitBean unit : unitList) {
            double[] locations = MonitorUtil.getLatLngByAddress(unit.getName(), mapKey, mapCK);
            if (locations != null) {
                MapMarkerBean bean = new MapMarkerBean();
                bean.setHospitalId(unit.getId());
                bean.setHospitalName(unit.getName());
                bean.setLat(locations[0]);
                bean.setLng(locations[1]);

                markerList.add(bean);
            }
        }

        return markerList;
    }

    public UnitBean getUnitInfo(String parentUnitId) {
        return unitDao.get(parentUnitId);
    }
}

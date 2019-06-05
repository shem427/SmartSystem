package cn.com.nex.monitor.webapp.status.service;

import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.common.util.MonitorUtil;
import cn.com.nex.monitor.webapp.sensor.bean.SensorBean;
import cn.com.nex.monitor.webapp.sensor.dao.SensorDao;
import cn.com.nex.monitor.webapp.status.bean.MapMarkerBean;
import cn.com.nex.monitor.webapp.status.bean.StatusBean;
import cn.com.nex.monitor.webapp.status.dao.StatusDao;
import cn.com.nex.monitor.webapp.unit.bean.UnitBean;
import cn.com.nex.monitor.webapp.unit.bean.UnitChainBean;
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
    @Autowired
    private StatusDao statusDao;

    @Value("${cn.com.nex.monitor.entity.unit.type}")
    private String entityUnitTypes;

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
        List<Integer> entityUnitTypeList = new ArrayList<>();
        String[] typeArray = entityUnitTypes.split(",");
        for (int i = 0; i < typeArray.length; i++) {
            int type = Integer.valueOf(typeArray[i]);
            entityUnitTypeList.add(type);
        }
        List<MapMarkerBean> markerList = new ArrayList<>();
        List<UnitChainBean> unitList = unitDao.getUnitByType(entityUnitTypeList);
        for (UnitChainBean unit : unitList) {
            double[] locations = MonitorUtil.getLatLngByAddress(getAddressFromPath(unit.getFullPath()), mapKey, mapCK);
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

    public StatusBean getUnitInfo(String parentUnitId) {
        return statusDao.getUnitInfo(parentUnitId);
    }

    public StatusBean getUnitStatus(String unitId, String userId) {
        return statusDao.getUnitStatus(unitId, userId);
    }

    public TableData<UnitChainBean> getUnitStatusDetail(SearchParam param, String unitId, String detailType) {
        List<UnitChainBean> datas = statusDao.listUnitDetail(param, unitId, detailType);
        int total = statusDao.countUnitDetail(unitId, detailType);

        TableData<UnitChainBean> tableData = new TableData<>();
        tableData.setRows(datas);
        tableData.setTotal(total);

        return tableData;
    }

    public TableData<SensorBean> getSensorStatusDetail(SearchParam param, String unitId, String detailType) {
        String unitPath = unitDao.getUnitFullPath(unitId);
        List<SensorBean> rows = statusDao.listSensorDetail(param, unitPath, detailType);
        int total = statusDao.countSensorDetail(unitPath, detailType);

        TableData<SensorBean> tableData = new TableData<>();
        tableData.setRows(rows);
        tableData.setTotal(total);

        return tableData;
    }

    private String getAddressFromPath(String path) {
        String[] pathArray = path.split("/");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < pathArray.length; i++) {
            sb.append(pathArray[i]);
        }
        return sb.toString();
    }
}

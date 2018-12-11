package cn.com.nex.monitor.webapp.sensor.service;

import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.sensor.bean.SensorBean;
import cn.com.nex.monitor.webapp.sensor.dao.SensorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SensorService {
    @Autowired
    private SensorDao sensorDao;

    public TableData<SensorBean> searchSensor(SearchParam param, String sensorNameLike, String sensorModelLike) {
        TableData<SensorBean> usersData = new TableData<>();
        List<SensorBean> sensorList = sensorDao.searchSensor(param, sensorNameLike, sensorModelLike);
        int count = sensorDao.count(sensorNameLike, sensorModelLike);

        usersData.setTotal(count);
        usersData.setRows(sensorList);

        return usersData;
    }

    public SensorBean getSensorById(String sensorId) {
        return sensorDao.get(sensorId);
    }

    @Transactional
    public int deleteSensor(List<String> sensorIds) {
        int count = 0;
        if (sensorIds == null || sensorIds.isEmpty()) {
            return count;
        }
        for (String id : sensorIds) {
            count += sensorDao.deleteSensor(id);
        }
        return count;
    }

    @Transactional
    public int addSensor(SensorBean sensor) {
        return sensorDao.addSensor(sensor);
    }

    @Transactional
    public int updateSensor(SensorBean sensor) {
        return sensorDao.updateSensor(sensor);
    }
}

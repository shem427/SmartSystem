package cn.com.nex.monitor.webapp.status.service;

import cn.com.nex.monitor.webapp.sensor.dao.SensorDao;
import cn.com.nex.monitor.webapp.unit.dao.UnitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
    @Autowired
    private UnitDao unitDao;
    @Autowired
    private SensorDao sensorDao;

    public int getNormalLightCount() {
        return 0;
    }

    public int getAbnormalLightCount() {
        return 0;
    }

    public int getActiveSensorCount() {
        return 0;
    }

    public int getInactiveSensorCount() {
        return 0;
    }
}

package cn.com.nex.monitor.webapp.dashboard.service;

import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import cn.com.nex.monitor.webapp.dashboard.dao.DashboardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    @Autowired
    private DashboardDao dashboardDao;

    public List<DashboardUnitBean> getUnitListByManagerAndParent(String userId, String parentId) {
        return dashboardDao.getUnitListByManagerAndParent(userId, parentId);
    }
}

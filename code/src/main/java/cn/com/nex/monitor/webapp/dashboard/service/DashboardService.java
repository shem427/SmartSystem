package cn.com.nex.monitor.webapp.dashboard.service;

import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import cn.com.nex.monitor.webapp.dashboard.bean.RadiationBean;
import cn.com.nex.monitor.webapp.dashboard.dao.DashboardDao;
import cn.com.nex.monitor.webapp.unit.dao.UnitDao;
import cn.com.nex.monitor.webapp.warn.bean.UnitWarnBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class DashboardService {
    @Autowired
    private DashboardDao dashboardDao;

    @Autowired
    private UnitDao unitDao;

    public Collection<DashboardUnitBean> getUnitListByManagerAndParent(String userId, String parentId) {
        return dashboardDao.getUnitListByManagerAndParent(userId, parentId);
    }

    @Transactional
    public void updateUnitStatus() {
        dashboardDao.updateUnitStatus();
    }

    public List<DashboardUnitBean> getNotifyUnitList() {
        return dashboardDao.getNotifyUnitList();
    }

    @Transactional
    public void addUnitWarn(UnitWarnBean bean) {
        dashboardDao.addUnitWarn(bean);
    }

    public String getParentIdByUnitId(String unitId) {
        return dashboardDao.getParentIdByUnitId(unitId);
    }

    public boolean isParentUnit(String unitId) {
        return dashboardDao.isParentUnit(unitId);
    }

    public List<Integer> getUnitRadiationData(String unitId) {
        return dashboardDao.getUnitRadiationData(unitId);
    }

    public TableData<RadiationBean> getRadiationData(SearchParam searchParam, String unitId) {
        int total = dashboardDao.countRadiationData(unitId);
        List<RadiationBean> list = dashboardDao.getRadiationData(searchParam, unitId);
        TableData<RadiationBean> tableData = new TableData<RadiationBean>();

        tableData.setRows(list);
        tableData.setTotal(total);

        return tableData;
    }

    public String getUnitPath(String unitId) {
        return unitDao.getUnitFullPath(unitId);
    }
}

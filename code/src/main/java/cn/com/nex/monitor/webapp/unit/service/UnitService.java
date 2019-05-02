package cn.com.nex.monitor.webapp.unit.service;

import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import cn.com.nex.monitor.webapp.dashboard.dao.DashboardDao;
import cn.com.nex.monitor.webapp.unit.bean.UnitBean;
import cn.com.nex.monitor.webapp.unit.dao.UnitDao;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Service
public class UnitService {

    @Autowired
    private UnitDao unitDao;

    @Autowired
    private DashboardDao dashboardDao;

    public List<UnitBean> getSubUnit(String unitId) {
        return unitDao.getUnitByParentId(unitId);
    }

    public List<UserBean> getUnitManagers(String unitId) {
        if (unitId == null) {
            return null;
        }
        return unitDao.getManagers(unitId);
    }

    public List<UnitBean> getSubUnitByUser(String userId, String parentUnitId) {
        List<UnitBean> beanList = new ArrayList<>();
        Collection<DashboardUnitBean> dashUnitList = dashboardDao.getUnitListByManagerAndParent(userId, parentUnitId);
        Iterator<DashboardUnitBean> it = dashUnitList.iterator();
        while (it.hasNext()) {
            UnitBean unit = new UnitBean();
            DashboardUnitBean dashBean = it.next();
            unit.setUnitRemark(dashBean.getRemark());
            unit.setId(dashBean.getUnitId());
            unit.setName(dashBean.getUnitName());
            unit.setPId(parentUnitId);
            unit.setIsParent(!dashBean.isLeaf());

            beanList.add(unit);
        }
        return beanList;
    }

    @Transactional
    public void add(UnitBean unit) {
        // insert unit and return unit id.
        String unitId = unitDao.add(unit);
        // save unit_manager.
        saveUnitManagers(unitId, unit.getManagerIdList());
    }

    @Transactional
    public int edit(UnitBean unit) {
        // save unit.
        int ret = unitDao.edit(unit);
        // save unit_manager.
        saveUnitManagers(unit.getId(), unit.getManagerIdList());

        return ret;
    }

    @Transactional
    public int delete(String unitId) {
        if (unitId == null) {
            return 0;
        }
        return unitDao.delete(unitId);
    }

    public boolean hasChildren(String unitId) {
        List<UnitBean> subList = unitDao.getUnitByParentId(unitId);
        return subList != null && !subList.isEmpty();
    }

    public String getUnitFullPath(String unitId) {
        return unitDao.getUnitFullPath(unitId);
    }

    private void saveUnitManagers(String unitId, List<String> userIdList) {
        unitDao.deleteManagers(unitId);
        unitDao.saveManagers(unitId, userIdList);
    }
}

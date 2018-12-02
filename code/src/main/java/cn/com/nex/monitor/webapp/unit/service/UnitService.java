package cn.com.nex.monitor.webapp.unit.service;

import cn.com.nex.monitor.webapp.unit.bean.UnitBean;
import cn.com.nex.monitor.webapp.unit.dao.UnitDao;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UnitService {

    @Autowired
    private UnitDao unitDao;

    public List<UnitBean> getSubUnit(String unitId) {
        return unitDao.getUnitByParentId(unitId);
    }

    public List<UserBean> getUnitManagers(String unitId) {
        if (unitId == null) {
            return null;
        }
        return unitDao.getManagers(unitId);
    }

    @Transactional
    public int add(UnitBean unit) {
        // insert unit and return unit id.
        int count = unitDao.add(unit);
        // save unit_manager.
        saveUnitManagers(unit.getId(), unit.getManagerIdList());

        return count;
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

    private void saveUnitManagers(String unitId, List<String> userIdList) {
        unitDao.deleteManagers(unitId);
        unitDao.saveManagers(unitId, userIdList);
    }
}

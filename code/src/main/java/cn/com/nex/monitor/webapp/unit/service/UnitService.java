package cn.com.nex.monitor.webapp.unit.service;

import cn.com.nex.monitor.webapp.common.constant.MonitorConstant;
import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import cn.com.nex.monitor.webapp.dashboard.dao.DashboardDao;
import cn.com.nex.monitor.webapp.unit.bean.ImportUnitBean;
import cn.com.nex.monitor.webapp.unit.bean.UnitBean;
import cn.com.nex.monitor.webapp.unit.dao.UnitDao;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.lang.model.type.ArrayType;
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
        // check name duplicated
        if (unitNameDuplicated(unit, true)) {
            throw new IllegalArgumentException("组织名重复，请修改！");
        }
        // insert unit and return unit id.
        String unitId = unitDao.add(unit);
        // save unit_manager.
        saveUnitManagers(unitId, unit.getManagerIdList());
    }

    @Transactional
    public int edit(UnitBean unit) {
        // check name duplicated
        if (unitNameDuplicated(unit, false)) {
            throw new IllegalArgumentException("组织名重复，请修改！");
        }
        // save unit.
        int ret = unitDao.edit(unit);
        // save unit_manager.
        saveUnitManagers(unit.getId(), unit.getManagerIdList());

        return ret;
    }

    private boolean unitNameDuplicated(UnitBean unit, boolean isAdd) {
        if (isAdd) {
            return unitDao.unitNameDuplicated(null, unit.getName());
        } else {
            return unitDao.unitNameDuplicated(unit.getId(), unit.getName());
        }
    }

    @Transactional
    public int delete(List<String> unitIdList) {
        if (unitIdList == null || unitIdList.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (String unitId : unitIdList) {
            count += unitDao.delete(unitId);
        }
        return count;
    }

    public List<String> getChildrenIdList(String unitId) {
        List<UnitBean> subList = unitDao.getUnitByParentId(unitId);
        List<String> idList = new ArrayList<>();
        subList.forEach(unit -> {
            idList.add(unit.getId());
        });
        return idList;
    }

    public String getUnitFullPath(String unitId) {
        return unitDao.getUnitFullPath(unitId);
    }

    private void saveUnitManagers(String unitId, List<String> userIdList) {
        unitDao.deleteManagers(unitId);
        unitDao.saveManagers(unitId, userIdList);
    }

    public void importUnit(List<ImportUnitBean> unitList, List<String> msgList) {
        int size = unitList.size();
        List<ImportUnitBean> newUnitList = new ArrayList<>();
        for (ImportUnitBean unit : unitList) {
            // 获取父组织
            UnitBean parent = unitDao.getUnitByName(unit.getParentUnitName());
            if (parent == null) {
                newUnitList.add(unit);
                continue;
            } else {
                unit.setPId(parent.getId());
            }
            Integer unitType = parseUnitType(unit.getType());
            if (unitType == null) {
                msgList.add("组织类型：" + unit.getType() + "不正确，无法导入，该件忽略！");
                continue;
            } else {
                unit.setUnitType(unitType);
            }
            // check unit name exists.
            UnitBean self = unitDao.getUnitByName(unit.getName());
            if (self != null) {
                msgList.add("组织名：" + unit.getName() + "已经存在，无法导入，该件忽略！");
                continue;
            }

            // leaf
            unit.setIsParent(unitType.intValue() != 9);
            unitDao.add(unit);
        }
        if (size == newUnitList.size() || newUnitList.isEmpty()) {
            return;
        } else {
            importUnit(newUnitList, msgList);
        }
    }

    private Integer parseUnitType(String type) {
        return MonitorConstant.UNIT_TYPE_MAP.get(type);
    }
}

package cn.com.nex.monitor.webapp.dashboard.dao;

import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.util.MonitorUtil;
import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import cn.com.nex.monitor.webapp.dashboard.bean.RadiationBean;
import cn.com.nex.monitor.webapp.warn.bean.UnitWarnBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.*;

@Component
public class DashboardDao {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public void updateUnitStatus() {
        resetUnitStatus();
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("updateUnitStatus").withoutProcedureColumnMetaDataAccess();
        simpleJdbcCall.execute();
    }

    public Collection<DashboardUnitBean> getUnitListByManagerAndParent(String userId, String parentId) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("getUnitByManagerParentId").withoutProcedureColumnMetaDataAccess();
        simpleJdbcCall.addDeclaredParameter(new SqlParameter("userId", Types.CHAR));
        simpleJdbcCall.addDeclaredParameter(new SqlParameter("unitParentId", Types.CHAR));

        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("userId", userId);
        inParamMap.put("unitParentId", parentId);
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);

        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

        Set<DashboardUnitBean> beanSet = new TreeSet<>();
        Iterator<Map.Entry<String, Object>> it = simpleJdbcCallResult.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            if (key.startsWith("#result-set")) {
                List<Map<String, Object>> value = (List<Map<String, Object>>) entry.getValue();
                if (value == null || value.isEmpty()) {
                    continue;
                }
                for (Map<String, Object> item : value) {
                    DashboardUnitBean dashUnit = initDashUnitBean(item);
                    beanSet.add(dashUnit);
                }
            }
        }
        return beanSet;
    }

    public void addUnitWarn(UnitWarnBean bean) {
        String sql = "INSERT INTO `UNIT_WARN` (`UNIT_ID`,`UNIT_STATUS`, `NOTIFY_TIME`) VALUES (?,?,?)";
        jdbcTemplate.update(sql, bean.getUnitId(), bean.getUnitStatus(), new Date());
    }

    public List<DashboardUnitBean> getNotifyUnitList() {
        String sql = "SELECT `UNIT_ID`, `UNIT_NAME`, `UNIT_STATUS` FROM `UNIT` WHERE ACTIVE=true AND LEAF=true AND UNIT_STATUS > 0";
        return jdbcTemplate.query(sql, rs -> {
            List<DashboardUnitBean> beanList = new ArrayList<>();
            while (rs.next()) {
                DashboardUnitBean bean = new DashboardUnitBean();
                bean.setUnitId(rs.getString("UNIT_ID"));
                bean.setUnitName(rs.getString("UNIT_NAME"));
                bean.setUnitStatus(rs.getInt("UNIT_STATUS"));
                beanList.add(bean);
            }
            return beanList;
        });
    }

    public boolean isParentUnit(String unitId) {
        String sql = "SELECT COUNT(1) AS CONT FROM `UNIT` WHERE `PARENT_ID`=?";
        int count = jdbcTemplate.query(sql, new String[] {unitId}, rs -> {
            if (rs.next()) {
                return rs.getInt("CONT");
            }
            return 0;
        });
        return count > 0;
    }

    public List<Double> getUnitRadiationData(String unitId) {
        String sql = "SELECT `RAD_VALUE` FROM `RADIATION` WHERE `UNIT_ID`=? ORDER BY `RAD_ID` DESC LIMIT 500";
        return jdbcTemplate.query(sql, new String[] {unitId}, rs -> {
            List<Double> retList = new ArrayList<>();
            while (rs.next()) {
                retList.add(rs.getDouble("RAD_VALUE"));
            }
            return retList;
        });
    }

    public List<RadiationBean> getRadiationData(SearchParam searchParam, String unitId) {
        String sql = "SELECT (@i :=@i + 1) AS NO, `RAD_VALUE`, `UPLOAD_TIME` FROM `RADIATION`, (SELECT @i := ?) AS it WHERE `UNIT_ID`=?";
        int from = 0;
        if (searchParam != null) {
            sql = sql + " " + searchParam.toSQL();
            from = searchParam.getOffset();
        }
        return jdbcTemplate.query(sql, new Object[] {from, unitId}, rs -> {
            List<RadiationBean> list = new ArrayList<>();
            while (rs.next()) {
                RadiationBean rb = new RadiationBean();
                rb.setRadNo(rs.getInt("NO"));
                rb.setRadValue(rs.getDouble("RAD_VALUE"));
                rb.setUploadTime(MonitorUtil.formatDate(rs.getTimestamp("UPLOAD_TIME")));
                list.add(rb);
            }
            return list;
        });
    }

    public int countRadiationData(String unitId) {
        String sql = "SELECT COUNT(`RAD_VALUE`) AS TOTAL FROM `RADIATION` WHERE `UNIT_ID`=?";
        return jdbcTemplate.query(sql, new String[] {unitId}, rs -> {
            int total = 0;
            while(rs.next()) {
                total = rs.getInt("TOTAL");
            }
            return total;
        });
    }

    private DashboardUnitBean initDashUnitBean(Map<String, Object> item) {
        DashboardUnitBean bean = new DashboardUnitBean();
        bean.setUnitId((String) item.get("UNIT_ID"));
        bean.setUnitName((String) item.get("UNIT_NAME"));
        bean.setParentId((String) item.get("PARENT_ID"));
        bean.setRemark((String) item.get("REMARK"));
        bean.setUnitStatus((Integer) item.get("UNIT_STATUS"));
        bean.setLeaf((Boolean) item.get("LEAF"));
        bean.setUnitType((Integer) item.get("UNIT_TYPE"));

        bean.setErrorCount((Integer) item.get("ERROR_COUNT"));
        bean.setWarnCount((Integer) item.get("WARN_COUNT"));
        bean.setNormalCount((Integer) item.get("NORMAL_COUNT"));

        return bean;
    }

    private void resetUnitStatus() {
        String sql = "UPDATE `UNIT` SET UNIT_STATUS=-1 WHERE ACTIVE=true AND LEAF=false";
        jdbcTemplate.update(sql);
    }
}

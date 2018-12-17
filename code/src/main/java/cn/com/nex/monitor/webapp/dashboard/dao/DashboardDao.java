package cn.com.nex.monitor.webapp.dashboard.dao;

import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import cn.com.nex.monitor.webapp.setting.bean.ThresholdBean;
import cn.com.nex.monitor.webapp.warn.bean.UnitWarnBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DashboardDao {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public void updateUnitStatus() {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("updateUnitStatus");
        simpleJdbcCall.execute();
    }

    public List<DashboardUnitBean> getUnitListByManagerAndParent(String userId, String parentId) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("getUnitByManagerParentId");
        Map<String, Object> inParamMap = new HashMap<String, Object>();
        inParamMap.put("userId", userId);
        inParamMap.put("unitParentId", parentId);
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);

        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

        List<DashboardUnitBean> beanList = new ArrayList<>();

        return beanList;
    }

    public void addUnitWarn(UnitWarnBean bean) {
        String sql = "INSERT INTO `UNIT_WARN` (`UNIT_ID`,`UNIT_STATUS`, `NOTIFY_TIME`) VALUES (?,?,?)";
        jdbcTemplate.update(sql, bean.getWarnId(), bean.getUnitStatus(), new Date());
    }

    public List<DashboardUnitBean> getNotifyUnitList() {
        String sql = "SELECT `UNIT_ID`, `UNIT_NAME`, `UNIT_STATUS` FROM `UNIT` WHERE ACTIVE=true AND LEAF=true AND UNIT_STATUS > 0";
        return jdbcTemplate.query(sql, rs -> {
            List<DashboardUnitBean> beanList = new ArrayList<>();
            if (rs.next()) {
                DashboardUnitBean bean = new DashboardUnitBean();
                bean.setUnitId(rs.getString("UNIT_ID"));
                bean.setUnitName(rs.getString("UNIT_NAME"));
                bean.setUnitStatus(rs.getInt("UNIT_STATUS"));
                beanList.add(bean);
            }
            return beanList;
        });
    }
}

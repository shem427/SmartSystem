package cn.com.nex.monitor.webapp.dashboard.dao;

import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DashboardDao {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public List<DashboardUnitBean> getUnitListByManagerAndParent(String userId, String parentId) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("getUnitByManagerParentId");
        Map<String, Object> inParamMap = new HashMap<String, Object>();
        inParamMap.put("userId", userId);
        inParamMap.put("unitParentId", "parentId");
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);

        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
        return null;
    }
}

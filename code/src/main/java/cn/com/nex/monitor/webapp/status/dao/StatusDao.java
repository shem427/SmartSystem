package cn.com.nex.monitor.webapp.status.dao;

import cn.com.nex.monitor.webapp.common.constant.DBConstant;
import cn.com.nex.monitor.webapp.setting.bean.ThresholdBean;
import cn.com.nex.monitor.webapp.status.bean.StatusBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatusDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public StatusBean getUnitStatus(String unitId, String userId) {
        String sql = "SELECT `DATA_ID`, `NORML`, `WARN` FROM `THRESHOLD` WHERE `DATA_ID`=?";
        ThresholdBean thresholdBean = jdbcTemplate.query(sql, new String[] { DBConstant.RADIATION_THRESHOLD }, rs -> {
            if (rs.next()) {
                ThresholdBean bean = new ThresholdBean();
                bean.setNorml(rs.getInt("NORML"));
                bean.setWarn(rs.getInt("WARN"));
                return bean;
            } else {
                return null;
            }
        });
        final StatusBean bean = new StatusBean();
        bean.setUnitId(unitId);

        String getUnitSql = "SELECT `UNIT_ID`,`UNIT_NAME`,getUnitPath(UNIT_ID) AS UNIT_PATH FROM `unit` WHERE `UNIT_ID`=?";
        jdbcTemplate.query(getUnitSql, new String[] {unitId}, rs -> {
            bean.setUnitName(rs.getString("UNIT_NAME"));
            bean.setUnitPath(rs.getString("UNIT_PATH"));
        });
        int unitPathLength = bean.getUnitPath().length();

        String normalSql = "SELECT COUNT(1) AS NORMAL FROM `RADIATION` R WHERE R.`RAD_VALUE` >= ? AND R.`UNIT_ID` IN (SELECT U.`UNIT_ID` FROM `unit` U, `unit_manager` M WHERE U.UNIT_ID=M.UNIT_ID AND M.USER_ID=? AND U.`LEAF`=true AND LEFT(getUnitPath(U.`UNIT_ID`), ?) = ?)";
        String warningSql = "SELECT COUNT(1) AS WARN FROM `RADIATION` R WHERE R.`RAD_VALUE` > ? AND R.`RAD_VALUE` < ? AND R.`UNIT_ID` IN (SELECT U.`UNIT_ID` FROM `unit` U, `unit_manager` M WHERE U.UNIT_ID=M.UNIT_ID AND M.USER_ID=? AND U.`LEAF`=true AND LEFT(getUnitPath(U.`UNIT_ID`), ?) = ?)";
        String errorSql = "SELECT COUNT(1) AS ERROR FROM `RADIATION` R WHERE R.`RAD_VALUE` <= ? AND R.`UNIT_ID` IN (SELECT U.`UNIT_ID` FROM `unit` U, `unit_manager` M WHERE U.UNIT_ID=M.UNIT_ID AND M.USER_ID=? AND U.`LEAF`=true AND LEFT(getUnitPath(U.`UNIT_ID`), ?) = ?)";
        jdbcTemplate.query(normalSql, new Object[] { thresholdBean.getNorml(), userId, unitPathLength, bean.getUnitPath() }, rs -> {
            bean.setNormalRadiationData(rs.getInt("NORMAL"));
        });
        jdbcTemplate.query(warningSql, new Object[] { thresholdBean.getWarn(), thresholdBean.getNorml(), userId, unitPathLength, bean.getUnitPath() }, rs -> {
            bean.setWarningRadiationData(rs.getInt("WARN"));
        });
        jdbcTemplate.query(errorSql, new Object[] { thresholdBean.getWarn(), userId, unitPathLength, bean.getUnitPath() }, rs -> {
            bean.setWarningRadiationData(rs.getInt("ERROR"));
        });

        return bean;
    }
}

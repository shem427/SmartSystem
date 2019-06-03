package cn.com.nex.monitor.webapp.status.dao;

import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.constant.DBConstant;
import cn.com.nex.monitor.webapp.sensor.bean.SensorBean;
import cn.com.nex.monitor.webapp.setting.bean.ThresholdBean;
import cn.com.nex.monitor.webapp.status.bean.StatusBean;
import cn.com.nex.monitor.webapp.unit.bean.UnitChainBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StatusDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public StatusBean getUnitInfo(String unitId) {
        String getUnitSql = "SELECT `UNIT_ID`,`UNIT_NAME`,getUnitPath(UNIT_ID) AS UNIT_PATH, getUnitIdChain(UNIT_ID) AS UNIT_ID_CHAIN FROM `unit` WHERE `UNIT_ID`=?";
        return jdbcTemplate.query(getUnitSql, new String[] {unitId}, rs -> {
            StatusBean bean = new StatusBean();
            bean.setUnitId(unitId);
            while (rs.next()) {
                bean.setUnitName(rs.getString("UNIT_NAME"));
                bean.setUnitPath(rs.getString("UNIT_PATH"));
                bean.setUnitIdChain(rs.getString("UNIT_ID_CHAIN"));
            }
            return bean;
        });
    }

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

        final StatusBean bean = getUnitInfo(unitId);
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
            bean.setErrorRadiationData(rs.getInt("ERROR"));
        });

        return bean;
    }

    public int countUnitDetail(String unitId, String detailType) {
        String sql = "SELECT COUNT(1) AS TOTAL FROM "
                + "(SELECT `UNIT_ID`,`UNIT_NAME`, getUnitPath(`UNIT_ID`) AS UNIT_PATH, `REMARK`, `UNIT_STATUS` FROM `UNIT` WHERE `ACTIVE`=true AND `LEAF`=true) U1, "
                + "(SELECT `UNIT_ID`, getUnitPath(`UNIT_ID`) AS UNIT_PATH FROM `UNIT` WHERE `UNIT_ID`=? AND ACTIVE=true) U2 "
                + "WHERE LEFT(U1.UNIT_PATH, CHAR_LENGTH(U2.UNIT_PATH)) = U2.UNIT_PATH AND (U1.`UNIT_STATUS`=? OR U1.`UNIT_STATUS`=?)";
        List<Object> params = new ArrayList<>();
        params.add(unitId);
        if ("normal".equalsIgnoreCase(detailType)) {
            params.add(0);
            params.add(-1);
        } else {
            params.add(1);
            params.add(2);
        }
        return jdbcTemplate.query(sql, params.toArray(), rs -> {
            int total = 0;
            if (rs.next()) {
                total = rs.getInt(DBConstant.TOTAL);
            }
            return total;
        });
    }

    public List<UnitChainBean> listUnitDetail(SearchParam searchParam, String unitId, String detailType) {
        String sql = "SELECT U1.UNIT_ID, U1.UNIT_NAME, U1.UNIT_PATH, U1.REMARK FROM "
                + "(SELECT `UNIT_ID`,`UNIT_NAME`, getUnitPath(`UNIT_ID`) AS UNIT_PATH, `REMARK`, `UNIT_STATUS` FROM `UNIT` WHERE `ACTIVE`=true AND `LEAF`=true) U1, "
                + "(SELECT `UNIT_ID`, getUnitPath(`UNIT_ID`) AS UNIT_PATH FROM `UNIT` WHERE `UNIT_ID`=? AND ACTIVE=true) U2 "
                + "WHERE LEFT(U1.UNIT_PATH, CHAR_LENGTH(U2.UNIT_PATH)) = U2.UNIT_PATH AND (U1.`UNIT_STATUS`=? OR U1.`UNIT_STATUS`=?)";
        List<Object> params = new ArrayList<>();
        params.add(unitId);
        if ("normal".equalsIgnoreCase(detailType)) {
            params.add(0);
            params.add(-1);
        } else {
            params.add(1);
            params.add(2);
        }
        if (searchParam != null) {
            sql += searchParam.toSQL();
        }

        return jdbcTemplate.query(sql, params.toArray(), rs -> {
            List<UnitChainBean> beanList = new ArrayList<>();
            while (rs.next()) {
                UnitChainBean bean = new UnitChainBean();
                bean.setId(rs.getString("UNIT_ID"));
                bean.setName(rs.getString("UNIT_NAME"));
                bean.setFullPath(rs.getString("UNIT_PATH"));
                bean.setUnitRemark(rs.getString("REMARK"));

                beanList.add(bean);
            }

            return beanList;
        });
    }

    public int countSensorDetail(String unitId, String detailType) {
        // TODO:
        return 0;
    }

    public List<SensorBean> listSensorDetail(SearchParam searchParam, String unitId, String detailType) {
        // TODO:
        return null;
    }
}

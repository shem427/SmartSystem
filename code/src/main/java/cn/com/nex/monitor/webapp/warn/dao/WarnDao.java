package cn.com.nex.monitor.webapp.warn.dao;

import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.dao.CommonDao;
import cn.com.nex.monitor.webapp.warn.bean.UnitWarnBean;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class WarnDao extends CommonDao<UnitWarnBean> {
    /**
     * 从ResultSet中抽取Bean对象。
     *
     * @param rs ResultSet对象
     * @return Bean对象
     * @throws SQLException SQL例外
     */
    @Override
    protected UnitWarnBean convertBean(ResultSet rs) {
        // ignore not used.
        return null;
    }

    /**
     * 获取表名。
     *
     * @return 表名
     */
    @Override
    protected String getTableName() {
        return "UNIT_WARN";
    }

    /**
     * 获取TABLE ID列名
     *
     * @return TABLE ID列名
     */
    @Override
    protected String idName() {
        return "WARN_ID";
    }

    public List<UnitWarnBean> getWarnsByUser(SearchParam param, String userId, Date begin, Date end, String unitPath) {
        String sql = "SELECT * FROM (" +
                "SELECT W.`WARN_ID`, W.`UNIT_ID`, U.`UNIT_NAME`, getUnitPath(U.`UNIT_ID`) AS UNIT_PATH, W.`UNIT_STATUS`, W.`NOTIFY_TIME` FROM `UNIT_WARN` W, `UNIT_MANAGER` M, `UNIT` U WHERE M.`UNIT_ID`=W.`UNIT_ID` AND W.`UNIT_ID`=U.`UNIT_ID` AND M.`USER_ID`=?" +
                ") SUB WHERE LEFT(SUB.UNIT_PATH, ?) = ? ";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(unitPath.length());
        params.add(unitPath);
        if (begin != null) {
            sql += " AND SUB.`NOTIFY_TIME` > ? ";
            params.add(begin);
        }
        if (end != null) {
            sql += " AND SUB.`NOTIFY_TIME` < ? ";
            params.add(end);
        }
        sql += " ORDER BY SUB.NOTIFY_TIME DESC ";
        sql += param.toSQL();
        return jdbcTemplate.query(sql, params.toArray(), rs -> {
            List<UnitWarnBean> beanList = new ArrayList<>();
            while(rs.next()) {
                UnitWarnBean bean = new UnitWarnBean();
                bean.setWarnId(rs.getInt("WARN_ID"));
                bean.setUnitName(rs.getString("UNIT_NAME"));
                bean.setUnitPath(rs.getString("UNIT_PATH"));
                bean.setUnitId(rs.getString("UNIT_ID"));
                bean.setUnitStatus(rs.getInt("UNIT_STATUS"));
                bean.setNotifyTime(rs.getTimestamp("NOTIFY_TIME"));
                beanList.add(bean);
            }
            return beanList;
        });
    }

    public int countWarnsByUser(String userId, Date begin, Date end, String unitPath) {
//        String sql = "SELECT COUNT(1) AS TOTAL FROM `UNIT_WARN` W, `UNIT_MANAGER` M WHERE M.`UNIT_ID`=W.`UNIT_ID` AND M.`USER_ID`=?";
        String sql = "SELECT COUNT(1) AS TOTAL FROM (" +
                "SELECT W.`WARN_ID`, W.`UNIT_ID`, U.`UNIT_NAME`, getUnitPath(U.`UNIT_ID`) AS UNIT_PATH, W.`UNIT_STATUS`, W.`NOTIFY_TIME` FROM `UNIT_WARN` W, `UNIT_MANAGER` M, `UNIT` U WHERE M.`UNIT_ID`=W.`UNIT_ID` AND W.`UNIT_ID`=U.`UNIT_ID` AND M.`USER_ID`=?" +
                ") SUB WHERE LEFT(SUB.UNIT_PATH, ?) = ? ";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(unitPath.length());
        params.add(unitPath);
        if (begin != null) {
            sql += " AND SUB.`NOTIFY_TIME` > ? ";
            params.add(begin);
        }
        if (end != null) {
            sql += " AND SUB.`NOTIFY_TIME` < ? ";
            params.add(end);
        }
        return jdbcTemplate.query(sql, params.toArray(), getTotalExtractor());
    }
}

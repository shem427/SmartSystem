package cn.com.nex.monitor.webapp.unit.dao;

import cn.com.nex.monitor.webapp.common.constant.DBConstant;
import cn.com.nex.monitor.webapp.common.dao.CommonDao;
import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import cn.com.nex.monitor.webapp.unit.bean.UnitBean;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Component
public class UnitDao extends CommonDao<UnitBean> {
    private static final String TABLE_NAME = "unit";
    private static final String UNIT_ID = "UNIT_ID";
    private static final String UNIT_NAME = "UNIT_NAME";
    private static final String PARENT_ID = "PARENT_ID";
    private static final String REMARK = "REMARK";
    private static final String LEAF = "LEAF";
    private static final String UNIT_TYPE = "UNIT_TYPE";

    /**
     * 从ResultSet中抽取Bean对象。
     *
     * @param rs ResultSet对象
     * @return Bean对象
     * @throws SQLException SQL例外
     */
    @Override
    protected UnitBean convertBean(ResultSet rs) throws SQLException {
        UnitBean bean = new UnitBean();
        bean.setId(rs.getString(UNIT_ID));
        bean.setName(rs.getString(UNIT_NAME));
        bean.setPId(rs.getString(PARENT_ID));
        bean.setUnitRemark(rs.getString(REMARK));
        bean.setIsParent(!rs.getBoolean(LEAF));
        bean.setUnitType(rs.getInt(UNIT_TYPE));

        return bean;
    }

    /**
     * 获取表名。
     *
     * @return 表名
     */
    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    /**
     * 获取TABLE ID列名
     *
     * @return TABLE ID列名
     */
    @Override
    protected String idName() {
        return UNIT_ID;
    }

    public List<UnitBean> getUnitByParentId(String pId) {
        String sql = "SELECT `UNIT_ID`, `UNIT_NAME`, `REMARK`, `PARENT_ID`, `LEAF`, `UNIT_TYPE` FROM `UNIT` WHERE `ACTIVE`=true AND ";
        if (pId == null) {
            sql += "`PARENT_ID` IS NULL";
            List<UnitBean> subList = jdbcTemplate.query(sql,
                    rs -> {
                        List<UnitBean> list = new ArrayList<>();
                        while(rs.next()) {
                            list.add(convertBean(rs));
                        }
                        return list;
                    });
            return subList;
        } else {
            sql += "`PARENT_ID`=?";
            List<UnitBean> subList = jdbcTemplate.query(sql, new String[] {pId},
                    rs -> {
                        List<UnitBean> list = new ArrayList<>();
                        while(rs.next()) {
                            list.add(convertBean(rs));
                        }
                        return list;
                    });
            return subList;
        }
    }

    public List<UserBean> getManagers(String unitId) {
        String sql = "SELECT M.`UNIT_ID`, M.`USER_ID`, U.`USER_NAME`, U.`MAIL_ADDRESS`, U.`PHONE_NUMBER`"
                + " FROM `UNIT_MANAGER` M, `USER` U WHERE M.`USER_ID`=U.`USER_ID` AND M.`UNIT_ID`=?";
        List<UserBean> userList = jdbcTemplate.query(sql, new String[] {unitId},
                rs -> {
                    List<UserBean> us = new ArrayList<>();
                    while (rs.next()) {
                        us.add(getMananger(rs));
                    }
                    return us;
                });
        return userList;
    }

    public void deleteManagers(String unitId) {
        String sql = "DELETE FROM `UNIT_MANAGER` WHERE `UNIT_ID`=?";
        jdbcTemplate.update(sql, unitId);
    }

    public void saveManagers(String unitId, List<String> userIdList) {
        if (userIdList == null || userIdList.isEmpty()) {
            return;
        }
        String sql = "INSERT INTO `UNIT_MANAGER` (`UNIT_ID`, `USER_ID`) VALUES (?,?)";
        for (String userId : userIdList) {
            jdbcTemplate.update(sql, unitId, userId);
        }
    }

    public synchronized String add(UnitBean unit) {
        String getIdSql = "INSERT INTO `UNIT_SEQ` VALUES (null);";
        jdbcTemplate.update(getIdSql);
        int id = getLastInsertId();
        String unitId = "UT"+ StringUtils.leftPad(String.valueOf(id), 14, '0');

        String sql = "INSERT INTO `UNIT`(`UNIT_ID`, `UNIT_NAME`, `PARENT_ID`, `REMARK`, `LEAF`, `UNIT_TYPE`) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, unitId, unit.getName(), unit.getPId(), unit.getUnitRemark(), !unit.getIsParent(), unit.getUnitType());

        return unitId;
    }

    public int edit(UnitBean unit) {
        String sql = "UPDATE `UNIT` SET `UNIT_NAME`=?, `REMARK`=? WHERE `UNIT_ID`=?";
        return jdbcTemplate.update(sql, unit.getName(), unit.getUnitRemark(), unit.getId());
    }

    public int delete(String unitId) {
        String sql = "UPDATE `UNIT` SET `ACTIVE`=false WHERE `UNIT_ID`=?";
        return jdbcTemplate.update(sql, unitId);
    }

    public String getUnitFullPath(String unitId) {
        String sql = "SELECT getUnitPath(?) AS UNIT_FULL_PATH FROM DUAL";
        return jdbcTemplate.query(sql, new String[] {unitId}, rs -> {
            String path = null;
            while(rs.next()) {
                path = rs.getString(DBConstant.UNIT_FULL_PATH);
            }
            return path;
        });
    }

    private UserBean getMananger(ResultSet rs) throws SQLException {
        UserBean user = new UserBean();
        user.setUserId(rs.getString("USER_ID"));
        user.setName(rs.getString("USER_NAME"));
        user.setPhoneNumber(rs.getString("PHONE_NUMBER"));
        user.setMailAddress(rs.getString("MAIL_ADDRESS"));

        return user;
    }

    public int countUnitByStatus(int status, String parentUnitId) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("countLightByStatus").withoutProcedureColumnMetaDataAccess();
        simpleJdbcCall.addDeclaredParameter(new SqlParameter("parentUnitId", Types.CHAR));
        simpleJdbcCall.addDeclaredParameter(new SqlParameter("stats", Types.INTEGER));

        simpleJdbcCall.addDeclaredParameter(new SqlOutParameter("size", Types.INTEGER));

        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("parentUnitId", parentUnitId);
        inParamMap.put("stats", status);
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

        return (int) simpleJdbcCallResult.get("size");
    }

    public boolean unitNameDuplicated(String unitId, String unitName) {
        String sql = "SELECT COUNT(1) AS AMOUNT FROM `UNIT` WHERE `ACTIVE`=true AND `UNIT_NAME`=?";
        if (unitId != null) {
            sql += " AND `UNIT_ID` <> ?";
            return jdbcTemplate.query(sql, new String[] {unitName, unitId}, rs -> {
                int count = 0;
                if (rs.next()) {
                    count = rs.getInt("AMOUNT");
                }
                return count != 0;
            });
        } else {
            return jdbcTemplate.query(sql, new String[] {unitName}, rs -> {
                int count = 0;
                if (rs.next()) {
                    count = rs.getInt("AMOUNT");
                }
                return count != 0;
            });
        }
    }

    public UnitBean getUnitByName(String name) {
        String sql = "SELECT `UNIT_ID`, `UNIT_NAME`, `PARENT_ID`, `REMARK`, `LEAF`, `UNIT_TYPE` FROM `UNIT` WHERE UNIT_NAME=? AND ACTIVE=true";
        return jdbcTemplate.query(sql, new String[] {name}, rs -> {
            if (rs.next()) {
                UnitBean bean = new UnitBean();
                bean.setId(rs.getString("UNIT_ID"));
                bean.setName(rs.getString("UNIT_NAME"));
                bean.setPId(rs.getString("PARENT_ID"));
                bean.setUnitRemark(rs.getString("REMARK"));
                bean.setIsParent(!rs.getBoolean("LEAF"));
                bean.setUnitType(rs.getInt("UNIT_TYPE"));

                return bean;
            }
            return null;
        });
    }

    public UnitBean getParentUnitById(String unitId) {
        String sql = "SELECT * FROM `UNIT` U, (SELECT `PARENT_ID` FROM `UNIT` WHERE `UNIT_ID`=?) SU where U.`UNIT_ID`=SU.`PARENT_ID`";
        return jdbcTemplate.query(sql, new String[] {unitId}, rs -> {
            if (rs.next()) {
                UnitBean bean = new UnitBean();
                bean.setUnitType(rs.getInt("UNIT_TYPE"));
                bean.setIsParent(!rs.getBoolean("LEAF"));
                bean.setPId(rs.getString("PARENT_ID"));
                bean.setName(rs.getString("UNIT_NAME"));
                bean.setId(rs.getString("UNIT_ID"));
                bean.setUnitRemark(rs.getString("REMARK"));

                return bean;
            } else {
                return null;
            }
        });
    }
}

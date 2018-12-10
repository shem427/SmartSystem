package cn.com.nex.monitor.webapp.unit.dao;

import cn.com.nex.monitor.webapp.common.dao.CommonDao;
import cn.com.nex.monitor.webapp.unit.bean.UnitBean;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UnitDao extends CommonDao<UnitBean> {
    private static final String TABLE_NAME = "unit";
    private static final String UNIT_ID = "UNIT_ID";
    private static final String UNIT_NAME = "UNIT_NAME";
    private static final String PARENT_ID = "PARENT_ID";
    private static final String REMARK = "REMARK";
    private static final String LEAF = "LEAF";

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
        String sql = "SELECT `UNIT_ID`, `UNIT_NAME`, `REMARK`, `PARENT_ID`, `LEAF` FROM `UNIT` WHERE `ACTIVE`=true AND ";
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

    public synchronized int add(UnitBean unit) {
        String sql = "INSERT INTO `UNIT` (`UNIT_NAME`, `PARENT_ID`, `REMARK`, `LEAF`) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, unit.getName(), unit.getPId(), unit.getUnitRemark(), !unit.getIsParent());
    }

    public int edit(UnitBean unit) {
        String sql = "UPDATE `UNIT` SET `UNIT_NAME`=?, `REMARK`=? WHERE `UNIT_ID`=?";
        return jdbcTemplate.update(sql, unit.getName(), unit.getUnitRemark(), unit.getId());
    }

    public int delete(String unitId) {
        String sql = "UPDATE `UNIT` SET `ACTIVE`=false WHERE `UNIT_ID`=?";
        return jdbcTemplate.update(sql, unitId);
    }

    private UserBean getMananger(ResultSet rs) throws SQLException {
        UserBean user = new UserBean();
        user.setUserId(rs.getString("USER_ID"));
        user.setName(rs.getString("USER_NAME"));
        user.setPhoneNumber(rs.getString("PHONE_NUMBER"));
        user.setMailAddress(rs.getString("MAIL_ADDRESS"));

        return user;
    }
}

package cn.com.nex.monitor.webapp.user.dao;

import cn.com.nex.monitor.webapp.common.CommonDao;
import cn.com.nex.monitor.webapp.common.DBConstant;
import cn.com.nex.monitor.webapp.common.SearchParam;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao extends CommonDao<UserBean> {
    private static final String TABLE_NAME = "user";
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String MAIL_ADDRESS = "MAIL_ADDRESS";
    private static final String USER_ROLE = "USER_ROLES";
    private static final String PASSWORD = "USER_PASSWORD";

    @Override
    protected UserBean convertBean(ResultSet rs) throws SQLException {
        UserBean bean = new UserBean();
        bean.setUserId(rs.getString(USER_ID));
        bean.setName(rs.getString(USER_NAME));
        bean.setMailAddress(rs.getString(MAIL_ADDRESS));
        bean.setUserRoles(rs.getString(USER_ROLE));
        bean.setPassword(rs.getString(PASSWORD));

        return bean;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String idName() {
        return USER_ID;
    }

    /**
     * 根据指定条件检索符合条件的人员。
     * @param param 共通检索条件
     * @param userIdLike 用户ID模糊条件
     * @param nameLike 姓名模糊条件
     * @return 符合条件人员
     */
    public List<UserBean> searchUser(SearchParam param, String userIdLike, String nameLike) {
        // parameter
        List<String> argList = new ArrayList<>();
        // sql文
        String sql = "SELECT `USER_ID`,`USER_NAME`,`MAIL_ADDRESS`,`PHONE_NUMBER`,`USER_ROLES`,`USER_PASSWORD` FROM `USER`";

        String sqlWhere = getWhereForSearch(userIdLike, nameLike, argList);
        if (sqlWhere.length() > 0) {
            sql = sql + " WHERE " + sqlWhere;
        }
        sql += param.toSQL();

        return jdbcTemplate.query(sql, argList.toArray(new String[0]), getAllExtractor());
    }

    /**
     * 根据指定条件检索符合条件的人员件数。
     * @param userIdLike 用户ID模糊条件
     * @param nameLike 姓名模糊条件
     * @return 符合条件人员件数
     */
    public int count(String userIdLike, String nameLike) {
        // parameter
        List<String> argList = new ArrayList<>();
        String sql = "SELECT COUNT(1) AS TOTAL FROM `USER`";
        String sqlWhere = getWhereForSearch(userIdLike, nameLike, argList);
        if (sqlWhere.length() > 0) {
            sql = sql + " WHERE " + sqlWhere;
        }

        return jdbcTemplate.query(sql, argList.toArray(new String[0]), rs -> {
            int total = 0;
            while(rs.next()) {
                total = rs.getInt("TOTAL");
            }
            return total;
        });
    }

    private String getWhereForSearch(String policeNoLike, String nameLike, List<String> argList) {
        StringBuilder sbSqlWhere = new StringBuilder();
        sbSqlWhere.append(DBConstant.IS_ACTIVE).append("=true");
        if (!StringUtils.isEmpty(policeNoLike)) {
            argList.add("%" + policeNoLike + "%");
            sbSqlWhere.append(" AND ").append(DBConstant.USER_ID).append(" LIKE ?");
        }
        if (!StringUtils.isEmpty(nameLike)) {
            argList.add("%" + nameLike + "%");
            sbSqlWhere.append(" AND ").append(DBConstant.USER_NAME).append(" LIKE ? ");
        }
        return sbSqlWhere.toString();
    }
}

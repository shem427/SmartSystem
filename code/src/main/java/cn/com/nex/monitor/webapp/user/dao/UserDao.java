package cn.com.nex.monitor.webapp.user.dao;

import cn.com.nex.monitor.webapp.common.CommonDao;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDao extends CommonDao<UserBean> {
    private static final String TABLE_NAME = "user";
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String MAIL_ADDRESS = "MAIL_ADDRESS";
    private static final String USER_ROLE = "USER_ROLE";
    private static final String PASSWORD = "USER_PASSWORD";

    @Override
    protected UserBean convertBean(ResultSet rs) throws SQLException {
        UserBean bean = new UserBean();
        bean.setUserId(rs.getString(USER_ID));
        bean.setName(rs.getString(USER_NAME));
        bean.setMailAddress(rs.getString(MAIL_ADDRESS));
        bean.setUserRole(rs.getString(USER_ROLE));
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
}

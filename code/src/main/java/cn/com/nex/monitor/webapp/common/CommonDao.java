package cn.com.nex.monitor.webapp.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class CommonDao<T extends CommonBean> {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public T get(String id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + idName() + "=?";
        T bean = jdbcTemplate.query(sql, new String[] {id}, getSingleExtractor());
        return bean;
    }

    public List<T> getAll() {
        String sql = "SELECT * FROM " + getTableName();
        List<T> beanList = jdbcTemplate.query(sql, getAllExtractor());

        return beanList;
    }

    protected ResultSetExtractor<T> getSingleExtractor() {
        return rs -> {
            T bean = null;
            while(rs.next()) {
                bean = convertBean(rs);
            }
            return bean;
        };
    }

    protected ResultSetExtractor<List<T>> getAllExtractor() {
        return rs -> {
            List<T> beanList = new ArrayList<>();
            while(rs.next()) {
                T bean = convertBean(rs);
                beanList.add(bean);
            }
            return beanList;
        };
    }

    protected abstract T convertBean(ResultSet rs) throws SQLException;

    protected abstract String getTableName();

    protected abstract String idName();
}

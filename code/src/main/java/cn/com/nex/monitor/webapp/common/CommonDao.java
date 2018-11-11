package cn.com.nex.monitor.webapp.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO共通父类。
 * @param <T> Bean类型
 */
public abstract class CommonDao<T extends CommonBean> {
    /**
     * JDBC Accessor.
     */
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * 根据ID获取Bean对象。
     * @param id id
     * @return 获取的Bean对象
     */
    public T get(String id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + idName() + "=?";
        T bean = jdbcTemplate.query(sql, new String[] {id}, getSingleExtractor());
        return bean;
    }

    /**
     * 获取所有的Bean对象List。
     * @return 所有的Bean对象List
     */
    public List<T> getAll() {
        String sql = "SELECT * FROM " + getTableName();
        List<T> beanList = jdbcTemplate.query(sql, getAllExtractor());

        return beanList;
    }

    /**
     * 从ResultSet中抽取Bean对象。
     * @return Bean对象抽取lamada。
     */
    protected ResultSetExtractor<T> getSingleExtractor() {
        return rs -> {
            T bean = null;
            while(rs.next()) {
                bean = convertBean(rs);
            }
            return bean;
        };
    }

    /**
     * 从ResultSet中抽取Bean对象。
     * @return Bean对象抽取lamada。
     */
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

    /**
     * 从ResultSet中抽取Bean对象。
     * @param rs ResultSet对象
     * @return Bean对象
     * @throws SQLException SQL例外
     */
    protected abstract T convertBean(ResultSet rs) throws SQLException;

    /**
     * 获取表名。
     * @return 表名
     */
    protected abstract String getTableName();

    /**
     * 获取TABLE ID列名
     * @return TABLE ID列名
     */
    protected abstract String idName();

    /**
     * 新建之后，获取插入的ID值。
     * @return 插入的ID值
     */
    protected int getLastInsertId() {
        return jdbcTemplate.query("SELECT LAST_INSERT_ID() AS ID", rs -> {
            int id = -1;
            while(rs.next()) {
                id = rs.getInt("ID");
            }
            return id;
        });
    }
}

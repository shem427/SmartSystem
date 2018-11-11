package cn.com.nex.monitor.webapp.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO��ͨ���ࡣ
 * @param <T> Bean����
 */
public abstract class CommonDao<T extends CommonBean> {
    /**
     * JDBC Accessor.
     */
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * ����ID��ȡBean����
     * @param id id
     * @return ��ȡ��Bean����
     */
    public T get(String id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + idName() + "=?";
        T bean = jdbcTemplate.query(sql, new String[] {id}, getSingleExtractor());
        return bean;
    }

    /**
     * ��ȡ���е�Bean����List��
     * @return ���е�Bean����List
     */
    public List<T> getAll() {
        String sql = "SELECT * FROM " + getTableName();
        List<T> beanList = jdbcTemplate.query(sql, getAllExtractor());

        return beanList;
    }

    /**
     * ��ResultSet�г�ȡBean����
     * @return Bean�����ȡlamada��
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
     * ��ResultSet�г�ȡBean����
     * @return Bean�����ȡlamada��
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
     * ��ResultSet�г�ȡBean����
     * @param rs ResultSet����
     * @return Bean����
     * @throws SQLException SQL����
     */
    protected abstract T convertBean(ResultSet rs) throws SQLException;

    /**
     * ��ȡ������
     * @return ����
     */
    protected abstract String getTableName();

    /**
     * ��ȡTABLE ID����
     * @return TABLE ID����
     */
    protected abstract String idName();

    /**
     * �½�֮�󣬻�ȡ�����IDֵ��
     * @return �����IDֵ
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

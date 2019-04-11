package cn.com.nex.monitor.webapp.sensor.dao;

import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.constant.DBConstant;
import cn.com.nex.monitor.webapp.common.dao.CommonDao;
import cn.com.nex.monitor.webapp.sensor.bean.SensorBean;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SensorDao extends CommonDao<SensorBean> {

    @Value("${cn.com.nex.monitor.active.interval}")
    private int activeInterval;

    /**
     * 从ResultSet中抽取Bean对象。
     *
     * @param rs ResultSet对象
     * @return Bean对象
     * @throws SQLException SQL例外
     */
    @Override
    protected SensorBean convertBean(ResultSet rs) throws SQLException {
        SensorBean bean = new SensorBean();
        bean.setSensorId(rs.getString(DBConstant.SENSOR_ID));
        bean.setRadiationModelId(rs.getString(DBConstant.RADIATION_MODEL_ID));
        bean.setSensorName(rs.getString(DBConstant.SENSOR_NAME));
        bean.setSensorSn(rs.getString(DBConstant.SENSOR_SN));
        bean.setSensorModel(rs.getString(DBConstant.SENSOR_MODEL));
        bean.setSensorRemark(rs.getString(DBConstant.SENSOR_REMARK));
        bean.setUnitId(rs.getString(DBConstant.UNIT_ID));
        bean.setUnitName(rs.getString(DBConstant.UNIT_NAME));
        bean.setUnitFullPath(rs.getString(DBConstant.UNIT_FULL_PATH));

        return bean;
    }

    /**
     * 获取表名。
     *
     * @return 表名
     */
    @Override
    protected String getTableName() {
        return DBConstant.SENSOR_TABLE;
    }

    /**
     * 获取TABLE ID列名
     *
     * @return TABLE ID列名
     */
    @Override
    protected String idName() {
        return DBConstant.SENSOR_ID;
    }

    public SensorBean getSensorById(String sensorId) {
        String sql = "SELECT S.`SENSOR_ID`, S.`RADIATION_MODEL_ID`, S.`SENSOR_NAME`, S.`SENSOR_REMARK`, S.`SENSOR_SN`, S.`SENSOR_MODEL`,"
                + " S.`UNIT_ID`, U.`UNIT_NAME`, getUnitPath(S.`UNIT_ID`) AS `UNIT_FULL_PATH`"
                + " FROM `SENSOR` S, `UNIT` U WHERE S.`UNIT_ID`=U.`UNIT_ID` AND S.`ACTIVE`=true"
                + " AND U.`ACTIVE`=true AND S.`SENSOR_ID`=?";
        return jdbcTemplate.query(sql, new String[] {sensorId}, getSingleExtractor());
    }

    public List<SensorBean> searchSensor(SearchParam param, String sensorNameLike, String sensorModelLike) {
        List<String> argList = new ArrayList<>();

        String sql = "SELECT S.`SENSOR_ID`, S.`RADIATION_MODEL_ID`, S.`SENSOR_NAME`, S.`SENSOR_REMARK`, S.`SENSOR_SN`, S.`SENSOR_MODEL`,"
                + " S.`UNIT_ID`, U.`UNIT_NAME`, getUnitPath(S.`UNIT_ID`) AS `UNIT_FULL_PATH`"
                + " FROM `SENSOR` S, `UNIT` U WHERE S.`UNIT_ID`=U.`UNIT_ID` AND S.`ACTIVE`=true"
                + " AND U.`ACTIVE`=true ";

        String sqlWhere = getWhereForSearch(sensorNameLike, sensorModelLike, argList);
        if (sqlWhere.length() > 0) {
            sql = sql + sqlWhere;
        }
        if (param != null) {
            sql += param.toSQL();
        }

        return jdbcTemplate.query(sql, argList.toArray(new String[0]), getAllExtractor());
    }

    public int count(String sensorNameLike, String sensorModelLike) {
        List<String> argList = new ArrayList<>();
        String sql = "SELECT COUNT(1) AS TOTAL FROM `SENSOR` S, `UNIT` U"
                + " WHERE S.`UNIT_ID`=U.`UNIT_ID` AND S.`ACTIVE`=true AND U.`ACTIVE`=true ";
        String sqlWhere = getWhereForSearch(sensorNameLike, sensorModelLike, argList);
        if (sqlWhere.length() > 0) {
            sql = sql + sqlWhere;
        }

        return jdbcTemplate.query(sql, argList.toArray(new String[0]), getTotalExtractor());
    }

    public int deleteSensor(String sensorId) {
        String sql = "UPDATE `SENSOR` SET `ACTIVE`=false WHERE `SENSOR_ID`=?";
        return jdbcTemplate.update(sql, sensorId);
    }

    public int addSensor(SensorBean sensor) {
        String sql = "INSERT INTO `SENSOR` (`SENSOR_NAME`,`RADIATION_MODEL_ID`,`SENSOR_REMARK`,`SENSOR_SN`,`SENSOR_MODEL`,`UNIT_ID`, `ACTIVE`) VALUES (?,?,?,?,?,?, true)";
        return jdbcTemplate.update(sql,
                sensor.getSensorName(),
                sensor.getRadiationModelId(),
                sensor.getSensorRemark(),
                sensor.getSensorSn(),
                sensor.getSensorModel(),
                sensor.getUnitId());
    }

    public int updateSensor(SensorBean sensor) {
        String sql = "UPDATE `SENSOR` SET `SENSOR_NAME`=?, `RADIATION_MODEL_ID`=?, `SENSOR_REMARK`=?, `SENSOR_SN`=?, `SENSOR_MODEL`=? WHERE `SENSOR_ID`=?";
        return jdbcTemplate.update(sql,
                sensor.getSensorName(),
                sensor.getRadiationModelId(),
                sensor.getSensorRemark(),
                sensor.getSensorSn(),
                sensor.getSensorModel(),
                sensor.getSensorId());
    }

    public int[] countSensor(String parentUnitId) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("countSensor").withoutProcedureColumnMetaDataAccess();
        simpleJdbcCall.addDeclaredParameter(new SqlParameter("parentUnitId", Types.CHAR));
        simpleJdbcCall.addDeclaredParameter(new SqlParameter("inactiveHours", Types.INTEGER));

        simpleJdbcCall.addDeclaredParameter(new SqlOutParameter("activeCount", Types.INTEGER));
        simpleJdbcCall.addDeclaredParameter(new SqlOutParameter("inactiveCount", Types.INTEGER));

        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("parentUnitId", parentUnitId);
        inParamMap.put("inactiveHours", activeInterval);
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

        int activeCount = (int) simpleJdbcCallResult.get("activeCount");
        int inactiveCount = (int) simpleJdbcCallResult.get("inactiveCount");

        return new int[] {activeCount, inactiveCount};
    }

    private String getWhereForSearch(String sensorNameLike, String sensorModelLike, List<String> argList) {
        StringBuilder sbSqlWhere = new StringBuilder();
        if (!StringUtils.isEmpty(sensorNameLike)) {
            argList.add("%" + sensorNameLike + "%");
            sbSqlWhere.append(" AND ").append(DBConstant.SENSOR_NAME).append(" LIKE ?");
        }
        if (!StringUtils.isEmpty(sensorModelLike)) {
            argList.add("%" + sensorModelLike + "%");
            sbSqlWhere.append(" AND ").append(DBConstant.SENSOR_MODEL).append(" LIKE ? ");
        }
        return sbSqlWhere.toString();
    }
}

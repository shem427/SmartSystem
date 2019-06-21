package cn.com.nex.monitor.webapp.data.dao;

import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.common.constant.DBConstant;
import cn.com.nex.monitor.webapp.common.dao.CommonDao;
import cn.com.nex.monitor.webapp.data.bean.DataBean;
import cn.com.nex.monitor.webapp.sensor.bean.SensorUnknowBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DataDao extends CommonDao<DataBean> {
    /** log */
    private static final Logger LOG = LoggerFactory.getLogger(DataDao.class);

    /**
     * 从ResultSet中抽取Bean对象。
     *
     * @param rs ResultSet对象
     * @return Bean对象
     * @throws SQLException SQL例外
     */
    @Override
    protected DataBean convertBean(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("not support.");
    }

    /**
     * 获取表名。
     *
     * @return 表名
     */
    @Override
    protected String getTableName() {
        throw new UnsupportedOperationException("not support.");
    }

    /**
     * 获取TABLE ID列名
     *
     * @return TABLE ID列名
     */
    @Override
    protected String idName() {
        throw new UnsupportedOperationException("not support.");
    }

    public int uploadRadiationData(DataBean data) {

        String unitId = getUnitIdByRadiationModel(data.getID());
        if (unitId == null) {
            LOG.warn("紫外模块[" + data.getID() + "]没有登录并关联到紫外灯！");
            return 0;
        }
        String sql = "INSERT INTO `RADIATION` (`UNIT_ID`, `RAD_VALUE`, `UPLOAD_TIME`) VALUES (?,?,?)";
        return jdbcTemplate.update(sql, unitId, data.getValue(), new Timestamp(data.getUploadTime().getTime()));
    }

    public int uploadSensorData(DataBean data) {
        String sql = "INSERT INTO `SENSOR_DATA` (`RADIATION_MODEL_ID`, `DATA_VALUE`, `DATA_TIME`) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, data.getID(), data.getValue(), new Timestamp(new Date().getTime()));
    }

    public List<SensorUnknowBean> getUnknownSensors(SearchParam param) {
        String sql = "SELECT `RADIATION_MODEL_ID`, count(`DATA_ID`) AS DATA_COUNT FROM `SENSOR_DATA` D WHERE NOT EXISTS (SELECT `DATA_ID` FROM `SENSOR` S WHERE D.`RADIATION_MODEL_ID`=S.`RADIATION_MODEL_ID` AND S.`ACTIVE`=true) GROUP BY `RADIATION_MODEL_ID` ";
        if (param != null) {
            sql += param.toSQL();
        }
        return jdbcTemplate.query(sql, rs -> {
            List<SensorUnknowBean> beanList = new ArrayList<>();
            while (rs.next()) {
                SensorUnknowBean bean = new SensorUnknowBean();
                bean.setRadiationModelId(rs.getString("RADIATION_MODEL_ID"));
                bean.setDataCount(rs.getInt("DATA_COUNT"));

                beanList.add(bean);
            }
            return beanList;
        });
    }

    public int countUnknownSensors(SearchParam param) {
        String sql = "SELECT count(`RADIATION_MODEL_ID`) AS TOTAL FROM `SENSOR_DATA` D WHERE NOT EXISTS (SELECT `DATA_ID` FROM `SENSOR` S WHERE D.`RADIATION_MODEL_ID`=S.`RADIATION_MODEL_ID` AND S.`ACTIVE`=true) GROUP BY `RADIATION_MODEL_ID` ";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                return rs.getInt("TOTAL");
            }
            return 0;
        });
    }

    public int moveDataToRadition(String radiationId) {
        String sql = "INSERT INTO `RADIATION` (`UNIT_ID`, `RAD_VALUE`, `UPLOAD_TIME`) SELECT S.`UNIT_ID`, D.`DATA_VALUE`, NOW() FROM `SENSOR_DATA` D, `SENSOR` S WHERE D.`RADIATION_MODEL_ID`=S.`RADIATION_MODEL_ID` AND D.`RADIATION_MODEL_ID`=?";
        return jdbcTemplate.update(sql, radiationId);
    }

    private String getUnitIdByRadiationModel(String radiationModelId) {
        String sql = "SELECT `UNIT_ID` FROM `SENSOR` WHERE RADIATION_MODEL_ID=? AND ACTIVE=true";
        return jdbcTemplate.query(sql, new String[] {radiationModelId}, rs -> {
            while (rs.next()) {
                String unitId = rs.getString(DBConstant.UNIT_ID);
                return unitId;
            }
            return null;
        });
    }
}

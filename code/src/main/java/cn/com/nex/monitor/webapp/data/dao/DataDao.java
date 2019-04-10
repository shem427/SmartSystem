package cn.com.nex.monitor.webapp.data.dao;

import cn.com.nex.monitor.webapp.common.constant.DBConstant;
import cn.com.nex.monitor.webapp.common.dao.CommonDao;
import cn.com.nex.monitor.webapp.data.bean.DataBean;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

@Component
public class DataDao extends CommonDao<DataBean> {
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
            throw new IllegalArgumentException("紫外模块没有关联到紫外灯！");
        }
        String sql = "INSERT INTO `RADIATION` (`UNIT_ID`, `RAD_VALUE`, `UPLOAD_TIME`) VALUES (?,?,?)";
        return jdbcTemplate.update(sql, unitId, data.getValue(), new Timestamp(data.getUploadTime().getTime()));
    }

    public int uploadSensorData(DataBean data) {
        String sql = "INSERT INTO `SENSOR_DATA` (`RADIATION_MODEL_ID`, `DATA_VALUE`, `DATA_TIME`) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, data.getID(), data.getValue(), new Timestamp(new Date().getTime()));
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

package com.wms.backend.modules.iot.repository;

import com.wms.backend.modules.iot.dto.DeviceModelSaveRequest;
import com.wms.backend.modules.iot.entity.DeviceModel;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceModelRepository {
    private final JdbcTemplate jdbcTemplate;
    public DeviceModelRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<DeviceModel> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, pointCount, alarmRuleCount, protocol, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM device_model WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR protocol LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<DeviceModel>(DeviceModel.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<DeviceModel>(DeviceModel.class), like, like, like);
        }
        if (hasText(status)) {
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<DeviceModel>(DeviceModel.class), status);
        }
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<DeviceModel>(DeviceModel.class));
    }

    public DeviceModel findById(String id) {
        List<DeviceModel> list = jdbcTemplate.query("SELECT id, code, name, pointCount, alarmRuleCount, protocol, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM device_model WHERE id = ?",
                new SqliteRowMapper<DeviceModel>(DeviceModel.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public DeviceModel insert(DeviceModelSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO device_model (id, code, name, pointCount, alarmRuleCount, protocol, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getPointCount(), request.getAlarmRuleCount(), request.getProtocol(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public DeviceModel update(String id, DeviceModelSaveRequest request) {
        jdbcTemplate.update("UPDATE device_model SET code = ?, name = ?, pointCount = ?, alarmRuleCount = ?, protocol = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getPointCount(), request.getAlarmRuleCount(), request.getProtocol(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM device_model WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

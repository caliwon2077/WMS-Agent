package com.wms.backend.modules.iot.repository;

import com.wms.backend.modules.iot.dto.IotDeviceSaveRequest;
import com.wms.backend.modules.iot.entity.IotDevice;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class IotDeviceRepository {
    private final JdbcTemplate jdbcTemplate;
    public IotDeviceRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<IotDevice> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, modelName, connectionStatus, lastReportTime, owner, ipAddress, remark, created_at AS createdAt, updated_at AS updatedAt FROM iot_device WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR modelName LIKE ? OR owner LIKE ?) ");
        if (hasText(status)) sql.append("AND connectionStatus = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<IotDevice>(IotDevice.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<IotDevice>(IotDevice.class), like, like, like, like);
        }
        if (hasText(status)) {
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<IotDevice>(IotDevice.class), status);
        }
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<IotDevice>(IotDevice.class));
    }

    public IotDevice findById(String id) {
        List<IotDevice> list = jdbcTemplate.query("SELECT id, code, name, modelName, connectionStatus, lastReportTime, owner, ipAddress, remark, created_at AS createdAt, updated_at AS updatedAt FROM iot_device WHERE id = ?",
                new SqliteRowMapper<IotDevice>(IotDevice.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public IotDevice insert(IotDeviceSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO iot_device (id, code, name, modelName, connectionStatus, lastReportTime, owner, ipAddress, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getModelName(), request.getConnectionStatus(), request.getLastReportTime(), request.getOwner(), request.getIpAddress(), request.getRemark());
        return findById(id);
    }

    public IotDevice update(String id, IotDeviceSaveRequest request) {
        jdbcTemplate.update("UPDATE iot_device SET code = ?, name = ?, modelName = ?, connectionStatus = ?, lastReportTime = ?, owner = ?, ipAddress = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getModelName(), request.getConnectionStatus(), request.getLastReportTime(), request.getOwner(), request.getIpAddress(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM iot_device WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

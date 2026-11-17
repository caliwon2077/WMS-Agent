package com.wms.backend.modules.maintenance.repository;

import com.wms.backend.modules.maintenance.dto.MaintenanceRecordSaveRequest;
import com.wms.backend.modules.maintenance.entity.MaintenanceRecord;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MaintenanceRecordRepository {
    private final JdbcTemplate jdbcTemplate;
    public MaintenanceRecordRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    public List<MaintenanceRecord> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, target, executor, maintainDate, result, status, planName, remark, created_at AS createdAt, updated_at AS updatedAt FROM maintenance_record WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR target LIKE ? OR executor LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenanceRecord>(MaintenanceRecord.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenanceRecord>(MaintenanceRecord.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenanceRecord>(MaintenanceRecord.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenanceRecord>(MaintenanceRecord.class));
    }
    public MaintenanceRecord findById(String id) {
        List<MaintenanceRecord> list = jdbcTemplate.query("SELECT id, code, target, executor, maintainDate, result, status, planName, remark, created_at AS createdAt, updated_at AS updatedAt FROM maintenance_record WHERE id = ?", new SqliteRowMapper<MaintenanceRecord>(MaintenanceRecord.class), id);
        return list.isEmpty() ? null : list.get(0);
    }
    public MaintenanceRecord insert(MaintenanceRecordSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO maintenance_record (id, code, target, executor, maintainDate, result, status, planName, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getTarget(), request.getExecutor(), request.getMaintainDate(), request.getResult(), request.getStatus(), request.getPlanName(), request.getRemark());
        return findById(id);
    }
    public MaintenanceRecord update(String id, MaintenanceRecordSaveRequest request) {
        jdbcTemplate.update("UPDATE maintenance_record SET code = ?, target = ?, executor = ?, maintainDate = ?, result = ?, status = ?, planName = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getTarget(), request.getExecutor(), request.getMaintainDate(), request.getResult(), request.getStatus(), request.getPlanName(), request.getRemark(), id);
        return findById(id);
    }
    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM maintenance_record WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

package com.wms.backend.modules.maintenance.repository;

import com.wms.backend.modules.maintenance.dto.MaintenanceItemSaveRequest;
import com.wms.backend.modules.maintenance.entity.MaintenanceItem;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MaintenanceItemRepository {
    private final JdbcTemplate jdbcTemplate;
    public MaintenanceItemRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    public List<MaintenanceItem> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, standardHours, targetType, riskLevel, status, executeStandard, remark, created_at AS createdAt, updated_at AS updatedAt FROM maintenance_item WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR targetType LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenanceItem>(MaintenanceItem.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenanceItem>(MaintenanceItem.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenanceItem>(MaintenanceItem.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenanceItem>(MaintenanceItem.class));
    }
    public MaintenanceItem findById(String id) {
        List<MaintenanceItem> list = jdbcTemplate.query("SELECT id, code, name, standardHours, targetType, riskLevel, status, executeStandard, remark, created_at AS createdAt, updated_at AS updatedAt FROM maintenance_item WHERE id = ?", new SqliteRowMapper<MaintenanceItem>(MaintenanceItem.class), id);
        return list.isEmpty() ? null : list.get(0);
    }
    public MaintenanceItem insert(MaintenanceItemSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO maintenance_item (id, code, name, standardHours, targetType, riskLevel, status, executeStandard, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getStandardHours(), request.getTargetType(), request.getRiskLevel(), request.getStatus(), request.getExecuteStandard(), request.getRemark());
        return findById(id);
    }
    public MaintenanceItem update(String id, MaintenanceItemSaveRequest request) {
        jdbcTemplate.update("UPDATE maintenance_item SET code = ?, name = ?, standardHours = ?, targetType = ?, riskLevel = ?, status = ?, executeStandard = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getStandardHours(), request.getTargetType(), request.getRiskLevel(), request.getStatus(), request.getExecuteStandard(), request.getRemark(), id);
        return findById(id);
    }
    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM maintenance_item WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

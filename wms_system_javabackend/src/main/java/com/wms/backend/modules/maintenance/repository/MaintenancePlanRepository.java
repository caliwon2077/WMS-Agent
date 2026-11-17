package com.wms.backend.modules.maintenance.repository;

import com.wms.backend.modules.maintenance.dto.MaintenancePlanSaveRequest;
import com.wms.backend.modules.maintenance.entity.MaintenancePlan;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MaintenancePlanRepository {
    private final JdbcTemplate jdbcTemplate;
    public MaintenancePlanRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<MaintenancePlan> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, target, cycle, owner, status, nextDate, strategy, remark, created_at AS createdAt, updated_at AS updatedAt FROM maintenance_plan WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR target LIKE ? OR owner LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenancePlan>(MaintenancePlan.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenancePlan>(MaintenancePlan.class), like, like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenancePlan>(MaintenancePlan.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MaintenancePlan>(MaintenancePlan.class));
    }

    public MaintenancePlan findById(String id) {
        List<MaintenancePlan> list = jdbcTemplate.query("SELECT id, code, name, target, cycle, owner, status, nextDate, strategy, remark, created_at AS createdAt, updated_at AS updatedAt FROM maintenance_plan WHERE id = ?", new SqliteRowMapper<MaintenancePlan>(MaintenancePlan.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public MaintenancePlan insert(MaintenancePlanSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO maintenance_plan (id, code, name, target, cycle, owner, status, nextDate, strategy, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getTarget(), request.getCycle(), request.getOwner(), request.getStatus(), request.getNextDate(), request.getStrategy(), request.getRemark());
        return findById(id);
    }

    public MaintenancePlan update(String id, MaintenancePlanSaveRequest request) {
        jdbcTemplate.update("UPDATE maintenance_plan SET code = ?, name = ?, target = ?, cycle = ?, owner = ?, status = ?, nextDate = ?, strategy = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getTarget(), request.getCycle(), request.getOwner(), request.getStatus(), request.getNextDate(), request.getStrategy(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM maintenance_plan WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

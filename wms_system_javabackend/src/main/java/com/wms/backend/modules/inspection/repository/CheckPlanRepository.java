package com.wms.backend.modules.inspection.repository;

import com.wms.backend.modules.inspection.dto.CheckPlanSaveRequest;
import com.wms.backend.modules.inspection.entity.CheckPlan;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CheckPlanRepository {
    private final JdbcTemplate jdbcTemplate;
    public CheckPlanRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<CheckPlan> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, target, frequency, executor, status, startDate, cycleRule, remark, created_at AS createdAt, updated_at AS updatedAt FROM check_plan WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR target LIKE ? OR executor LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckPlan>(CheckPlan.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckPlan>(CheckPlan.class), like, like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckPlan>(CheckPlan.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckPlan>(CheckPlan.class));
    }

    public CheckPlan findById(String id) {
        List<CheckPlan> list = jdbcTemplate.query("SELECT id, code, name, target, frequency, executor, status, startDate, cycleRule, remark, created_at AS createdAt, updated_at AS updatedAt FROM check_plan WHERE id = ?", new SqliteRowMapper<CheckPlan>(CheckPlan.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public CheckPlan insert(CheckPlanSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO check_plan (id, code, name, target, frequency, executor, status, startDate, cycleRule, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getTarget(), request.getFrequency(), request.getExecutor(), request.getStatus(), request.getStartDate(), request.getCycleRule(), request.getRemark());
        return findById(id);
    }

    public CheckPlan update(String id, CheckPlanSaveRequest request) {
        jdbcTemplate.update("UPDATE check_plan SET code = ?, name = ?, target = ?, frequency = ?, executor = ?, status = ?, startDate = ?, cycleRule = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getTarget(), request.getFrequency(), request.getExecutor(), request.getStatus(), request.getStartDate(), request.getCycleRule(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM check_plan WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

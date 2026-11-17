package com.wms.backend.modules.repair.repository;

import com.wms.backend.modules.repair.dto.RepairStageSaveRequest;
import com.wms.backend.modules.repair.entity.RepairStage;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RepairStageRepository {
    private final JdbcTemplate jdbcTemplate;
    public RepairStageRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<RepairStage> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, sortOrder, allowRollback, slaHours, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM repair_stage WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR slaHours LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY sortOrder ASC, updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairStage>(RepairStage.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairStage>(RepairStage.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairStage>(RepairStage.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairStage>(RepairStage.class));
    }

    public RepairStage findById(String id) {
        List<RepairStage> list = jdbcTemplate.query("SELECT id, code, name, sortOrder, allowRollback, slaHours, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM repair_stage WHERE id = ?",
                new SqliteRowMapper<RepairStage>(RepairStage.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public RepairStage insert(RepairStageSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO repair_stage (id, code, name, sortOrder, allowRollback, slaHours, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getSortOrder(), request.getAllowRollback(), request.getSlaHours(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public RepairStage update(String id, RepairStageSaveRequest request) {
        jdbcTemplate.update("UPDATE repair_stage SET code = ?, name = ?, sortOrder = ?, allowRollback = ?, slaHours = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getSortOrder(), request.getAllowRollback(), request.getSlaHours(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM repair_stage WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

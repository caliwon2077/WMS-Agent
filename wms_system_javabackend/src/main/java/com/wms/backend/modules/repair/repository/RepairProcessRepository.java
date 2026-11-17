package com.wms.backend.modules.repair.repository;

import com.wms.backend.modules.repair.dto.RepairProcessSaveRequest;
import com.wms.backend.modules.repair.entity.RepairProcess;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RepairProcessRepository {
    private final JdbcTemplate jdbcTemplate;
    public RepairProcessRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<RepairProcess> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, scope, nodeCount, approvalMode, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM repair_process WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR scope LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairProcess>(RepairProcess.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairProcess>(RepairProcess.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairProcess>(RepairProcess.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairProcess>(RepairProcess.class));
    }

    public RepairProcess findById(String id) {
        List<RepairProcess> list = jdbcTemplate.query("SELECT id, code, name, scope, nodeCount, approvalMode, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM repair_process WHERE id = ?",
                new SqliteRowMapper<RepairProcess>(RepairProcess.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public RepairProcess insert(RepairProcessSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO repair_process (id, code, name, scope, nodeCount, approvalMode, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getScope(), request.getNodeCount(), request.getApprovalMode(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public RepairProcess update(String id, RepairProcessSaveRequest request) {
        jdbcTemplate.update("UPDATE repair_process SET code = ?, name = ?, scope = ?, nodeCount = ?, approvalMode = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getScope(), request.getNodeCount(), request.getApprovalMode(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM repair_process WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

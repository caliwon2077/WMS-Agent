package com.wms.backend.modules.oa.repository;

import com.wms.backend.modules.oa.dto.ApprovalTypeSaveRequest;
import com.wms.backend.modules.oa.entity.ApprovalType;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ApprovalTypeRepository {
    private final JdbcTemplate jdbcTemplate;
    public ApprovalTypeRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<ApprovalType> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, bindFlow, strategy, modifyCount, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM oa_approval_type WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR bindFlow LIKE ? OR strategy LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<ApprovalType>(ApprovalType.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<ApprovalType>(ApprovalType.class), like, like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<ApprovalType>(ApprovalType.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<ApprovalType>(ApprovalType.class));
    }

    public ApprovalType findById(String id) {
        List<ApprovalType> list = jdbcTemplate.query("SELECT id, code, name, bindFlow, strategy, modifyCount, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM oa_approval_type WHERE id = ?",
                new SqliteRowMapper<ApprovalType>(ApprovalType.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public ApprovalType insert(ApprovalTypeSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO oa_approval_type (id, code, name, bindFlow, strategy, modifyCount, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getBindFlow(), request.getStrategy(), request.getModifyCount(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public ApprovalType update(String id, ApprovalTypeSaveRequest request) {
        jdbcTemplate.update("UPDATE oa_approval_type SET code = ?, name = ?, bindFlow = ?, strategy = ?, modifyCount = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getBindFlow(), request.getStrategy(), request.getModifyCount(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM oa_approval_type WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

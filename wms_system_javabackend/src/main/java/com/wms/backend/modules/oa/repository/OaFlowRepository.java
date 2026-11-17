package com.wms.backend.modules.oa.repository;

import com.wms.backend.modules.oa.dto.OaFlowSaveRequest;
import com.wms.backend.modules.oa.entity.OaFlow;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OaFlowRepository {
    private final JdbcTemplate jdbcTemplate;
    public OaFlowRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<OaFlow> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, bizType, nodeCount, version, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM oa_flow WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR bizType LIKE ? OR version LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<OaFlow>(OaFlow.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<OaFlow>(OaFlow.class), like, like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<OaFlow>(OaFlow.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<OaFlow>(OaFlow.class));
    }

    public OaFlow findById(String id) {
        List<OaFlow> list = jdbcTemplate.query("SELECT id, code, name, bizType, nodeCount, version, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM oa_flow WHERE id = ?",
                new SqliteRowMapper<OaFlow>(OaFlow.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public OaFlow insert(OaFlowSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO oa_flow (id, code, name, bizType, nodeCount, version, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getBizType(), request.getNodeCount(), request.getVersion(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public OaFlow update(String id, OaFlowSaveRequest request) {
        jdbcTemplate.update("UPDATE oa_flow SET code = ?, name = ?, bizType = ?, nodeCount = ?, version = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getBizType(), request.getNodeCount(), request.getVersion(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM oa_flow WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

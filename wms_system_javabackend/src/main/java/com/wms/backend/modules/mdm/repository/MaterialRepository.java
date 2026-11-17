package com.wms.backend.modules.mdm.repository;

import com.wms.backend.modules.mdm.dto.MaterialSaveRequest;
import com.wms.backend.modules.mdm.entity.Material;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MaterialRepository {
    private final JdbcTemplate jdbcTemplate;
    public MaterialRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Material> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, spec, unit, category, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM material WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR spec LIKE ? OR category LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Material>(Material.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Material>(Material.class), like, like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Material>(Material.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Material>(Material.class));
    }

    public Material findById(String id) {
        List<Material> list = jdbcTemplate.query("SELECT id, code, name, spec, unit, category, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM material WHERE id = ?",
                new SqliteRowMapper<Material>(Material.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public Material insert(MaterialSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO material (id, code, name, spec, unit, category, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getSpec(), request.getUnit(), request.getCategory(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public Material update(String id, MaterialSaveRequest request) {
        jdbcTemplate.update("UPDATE material SET code = ?, name = ?, spec = ?, unit = ?, category = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getSpec(), request.getUnit(), request.getCategory(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM material WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

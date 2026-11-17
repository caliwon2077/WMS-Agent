package com.wms.backend.modules.accessory.repository;

import com.wms.backend.modules.accessory.dto.AccessoryCategorySaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryCategory;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccessoryCategoryRepository {
    private final JdbcTemplate jdbcTemplate;
    public AccessoryCategoryRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    public List<AccessoryCategory> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, parentCategory, itemCount, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM accessory_category WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR parentCategory LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryCategory>(AccessoryCategory.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryCategory>(AccessoryCategory.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryCategory>(AccessoryCategory.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryCategory>(AccessoryCategory.class));
    }
    public AccessoryCategory findById(String id) {
        List<AccessoryCategory> list = jdbcTemplate.query("SELECT id, code, name, parentCategory, itemCount, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM accessory_category WHERE id = ?", new SqliteRowMapper<AccessoryCategory>(AccessoryCategory.class), id);
        return list.isEmpty() ? null : list.get(0);
    }
    public AccessoryCategory insert(AccessoryCategorySaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO accessory_category (id, code, name, parentCategory, itemCount, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getParentCategory(), request.getItemCount(), request.getStatus(), request.getRemark());
        return findById(id);
    }
    public AccessoryCategory update(String id, AccessoryCategorySaveRequest request) {
        jdbcTemplate.update("UPDATE accessory_category SET code = ?, name = ?, parentCategory = ?, itemCount = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getParentCategory(), request.getItemCount(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }
    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM accessory_category WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

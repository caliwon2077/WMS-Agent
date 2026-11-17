package com.wms.backend.modules.equipment.repository;

import com.wms.backend.modules.equipment.dto.EquipmentCategorySaveRequest;
import com.wms.backend.modules.equipment.entity.EquipmentCategory;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class EquipmentCategoryRepository {
    private final JdbcTemplate jdbcTemplate;
    public EquipmentCategoryRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<EquipmentCategory> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, parentCategory, checkStrategy, maintenanceStrategy, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM equipment_category WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR parentCategory LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<EquipmentCategory>(EquipmentCategory.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<EquipmentCategory>(EquipmentCategory.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<EquipmentCategory>(EquipmentCategory.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<EquipmentCategory>(EquipmentCategory.class));
    }

    public EquipmentCategory findById(String id) {
        List<EquipmentCategory> list = jdbcTemplate.query("SELECT id, code, name, parentCategory, checkStrategy, maintenanceStrategy, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM equipment_category WHERE id = ?", new SqliteRowMapper<EquipmentCategory>(EquipmentCategory.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public EquipmentCategory insert(EquipmentCategorySaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO equipment_category (id, code, name, parentCategory, checkStrategy, maintenanceStrategy, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getParentCategory(), request.getCheckStrategy(), request.getMaintenanceStrategy(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public EquipmentCategory update(String id, EquipmentCategorySaveRequest request) {
        jdbcTemplate.update("UPDATE equipment_category SET code = ?, name = ?, parentCategory = ?, checkStrategy = ?, maintenanceStrategy = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getParentCategory(), request.getCheckStrategy(), request.getMaintenanceStrategy(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM equipment_category WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

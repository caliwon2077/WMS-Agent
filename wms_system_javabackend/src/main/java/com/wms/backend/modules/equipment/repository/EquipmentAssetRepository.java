package com.wms.backend.modules.equipment.repository;

import com.wms.backend.modules.equipment.dto.EquipmentAssetSaveRequest;
import com.wms.backend.modules.equipment.entity.EquipmentAsset;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class EquipmentAssetRepository {

    private final JdbcTemplate jdbcTemplate;

    public EquipmentAssetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<EquipmentAsset> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, code, name, category, workshop, owner, status, purchaseDate, model, supplier, remark, ");
        sql.append("created_at AS createdAt, updated_at AS updatedAt FROM equipment_assets WHERE 1=1 ");

        if (hasText(keyword)) {
            sql.append("AND (code LIKE ? OR name LIKE ? OR category LIKE ? OR workshop LIKE ? OR owner LIKE ?) ");
        }
        if (hasText(status)) {
            sql.append("AND status = ? ");
        }
        sql.append("ORDER BY updated_at DESC, created_at DESC");

        if (hasText(keyword) && hasText(status)) {
            String likeKeyword = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<EquipmentAsset>(EquipmentAsset.class),
                    likeKeyword, likeKeyword, likeKeyword, likeKeyword, likeKeyword, status);
        }
        if (hasText(keyword)) {
            String likeKeyword = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<EquipmentAsset>(EquipmentAsset.class),
                    likeKeyword, likeKeyword, likeKeyword, likeKeyword, likeKeyword);
        }
        if (hasText(status)) {
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<EquipmentAsset>(EquipmentAsset.class), status);
        }
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<EquipmentAsset>(EquipmentAsset.class));
    }

    public EquipmentAsset findById(String id) {
        List<EquipmentAsset> records = jdbcTemplate.query(
                "SELECT id, code, name, category, workshop, owner, status, purchaseDate, model, supplier, remark, " +
                        "created_at AS createdAt, updated_at AS updatedAt FROM equipment_assets WHERE id = ?",
                new SqliteRowMapper<EquipmentAsset>(EquipmentAsset.class),
                id
        );
        return records.isEmpty() ? null : records.get(0);
    }

    public EquipmentAsset insert(EquipmentAssetSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update(
                "INSERT INTO equipment_assets (id, code, name, category, workshop, owner, status, purchaseDate, model, supplier, remark) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id,
                request.getCode(),
                request.getName(),
                request.getCategory(),
                request.getWorkshop(),
                request.getOwner(),
                request.getStatus(),
                request.getPurchaseDate(),
                request.getModel(),
                request.getSupplier(),
                request.getRemark()
        );
        return findById(id);
    }

    public EquipmentAsset update(String id, EquipmentAssetSaveRequest request) {
        jdbcTemplate.update(
                "UPDATE equipment_assets SET code = ?, name = ?, category = ?, workshop = ?, owner = ?, status = ?, " +
                        "purchaseDate = ?, model = ?, supplier = ?, remark = ? WHERE id = ?",
                request.getCode(),
                request.getName(),
                request.getCategory(),
                request.getWorkshop(),
                request.getOwner(),
                request.getStatus(),
                request.getPurchaseDate(),
                request.getModel(),
                request.getSupplier(),
                request.getRemark(),
                id
        );
        return findById(id);
    }

    public int deleteById(String id) {
        return jdbcTemplate.update("DELETE FROM equipment_assets WHERE id = ?", id);
    }

    private boolean hasText(String text) {
        return text != null && text.trim().length() > 0;
    }
}

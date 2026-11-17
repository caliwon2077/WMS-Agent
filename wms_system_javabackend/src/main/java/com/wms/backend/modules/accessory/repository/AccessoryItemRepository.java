package com.wms.backend.modules.accessory.repository;

import com.wms.backend.modules.accessory.dto.AccessoryItemSaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryItem;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccessoryItemRepository {
    private final JdbcTemplate jdbcTemplate;
    public AccessoryItemRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    public List<AccessoryItem> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, spec, warehouse, stockQty, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM accessory_item WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR spec LIKE ? OR warehouse LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryItem>(AccessoryItem.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryItem>(AccessoryItem.class), like, like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryItem>(AccessoryItem.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryItem>(AccessoryItem.class));
    }
    public AccessoryItem findById(String id) {
        List<AccessoryItem> list = jdbcTemplate.query("SELECT id, code, name, spec, warehouse, stockQty, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM accessory_item WHERE id = ?", new SqliteRowMapper<AccessoryItem>(AccessoryItem.class), id);
        return list.isEmpty() ? null : list.get(0);
    }
    public AccessoryItem insert(AccessoryItemSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO accessory_item (id, code, name, spec, warehouse, stockQty, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getSpec(), request.getWarehouse(), request.getStockQty(), request.getStatus(), request.getRemark());
        return findById(id);
    }
    public AccessoryItem update(String id, AccessoryItemSaveRequest request) {
        jdbcTemplate.update("UPDATE accessory_item SET code = ?, name = ?, spec = ?, warehouse = ?, stockQty = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getSpec(), request.getWarehouse(), request.getStockQty(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }
    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM accessory_item WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

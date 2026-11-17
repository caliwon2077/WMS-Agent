package com.wms.backend.modules.accessory.repository;

import com.wms.backend.modules.accessory.dto.AccessoryWarehouseSaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryWarehouse;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccessoryWarehouseRepository {
    private final JdbcTemplate jdbcTemplate;
    public AccessoryWarehouseRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    public List<AccessoryWarehouse> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, zoneCount, manager, stockTotal, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM accessory_warehouse WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR manager LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryWarehouse>(AccessoryWarehouse.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryWarehouse>(AccessoryWarehouse.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryWarehouse>(AccessoryWarehouse.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryWarehouse>(AccessoryWarehouse.class));
    }
    public AccessoryWarehouse findById(String id) {
        List<AccessoryWarehouse> list = jdbcTemplate.query("SELECT id, code, name, zoneCount, manager, stockTotal, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM accessory_warehouse WHERE id = ?", new SqliteRowMapper<AccessoryWarehouse>(AccessoryWarehouse.class), id);
        return list.isEmpty() ? null : list.get(0);
    }
    public AccessoryWarehouse insert(AccessoryWarehouseSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO accessory_warehouse (id, code, name, zoneCount, manager, stockTotal, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getZoneCount(), request.getManager(), request.getStockTotal(), request.getStatus(), request.getRemark());
        return findById(id);
    }
    public AccessoryWarehouse update(String id, AccessoryWarehouseSaveRequest request) {
        jdbcTemplate.update("UPDATE accessory_warehouse SET code = ?, name = ?, zoneCount = ?, manager = ?, stockTotal = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getZoneCount(), request.getManager(), request.getStockTotal(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }
    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM accessory_warehouse WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

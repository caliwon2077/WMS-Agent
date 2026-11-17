package com.wms.backend.modules.accessory.repository;

import com.wms.backend.modules.accessory.dto.AccessorySupplierSaveRequest;
import com.wms.backend.modules.accessory.entity.AccessorySupplier;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccessorySupplierRepository {
    private final JdbcTemplate jdbcTemplate;
    public AccessorySupplierRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    public List<AccessorySupplier> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, contact, deliveryCycle, grade, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM accessory_supplier WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR contact LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessorySupplier>(AccessorySupplier.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessorySupplier>(AccessorySupplier.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessorySupplier>(AccessorySupplier.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessorySupplier>(AccessorySupplier.class));
    }
    public AccessorySupplier findById(String id) {
        List<AccessorySupplier> list = jdbcTemplate.query("SELECT id, code, name, contact, deliveryCycle, grade, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM accessory_supplier WHERE id = ?", new SqliteRowMapper<AccessorySupplier>(AccessorySupplier.class), id);
        return list.isEmpty() ? null : list.get(0);
    }
    public AccessorySupplier insert(AccessorySupplierSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO accessory_supplier (id, code, name, contact, deliveryCycle, grade, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getContact(), request.getDeliveryCycle(), request.getGrade(), request.getStatus(), request.getRemark());
        return findById(id);
    }
    public AccessorySupplier update(String id, AccessorySupplierSaveRequest request) {
        jdbcTemplate.update("UPDATE accessory_supplier SET code = ?, name = ?, contact = ?, deliveryCycle = ?, grade = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getContact(), request.getDeliveryCycle(), request.getGrade(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }
    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM accessory_supplier WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

package com.wms.backend.modules.equipment.repository;

import com.wms.backend.modules.equipment.dto.ManufacturerSaveRequest;
import com.wms.backend.modules.equipment.entity.Manufacturer;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ManufacturerRepository {
    private final JdbcTemplate jdbcTemplate;
    public ManufacturerRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Manufacturer> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, contact, mobile, email, address, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM manufacturer WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR contact LIKE ? OR mobile LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Manufacturer>(Manufacturer.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Manufacturer>(Manufacturer.class), like, like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Manufacturer>(Manufacturer.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Manufacturer>(Manufacturer.class));
    }

    public Manufacturer findById(String id) {
        List<Manufacturer> list = jdbcTemplate.query("SELECT id, code, name, contact, mobile, email, address, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM manufacturer WHERE id = ?", new SqliteRowMapper<Manufacturer>(Manufacturer.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public Manufacturer insert(ManufacturerSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO manufacturer (id, code, name, contact, mobile, email, address, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getContact(), request.getMobile(), request.getEmail(), request.getAddress(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public Manufacturer update(String id, ManufacturerSaveRequest request) {
        jdbcTemplate.update("UPDATE manufacturer SET code = ?, name = ?, contact = ?, mobile = ?, email = ?, address = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getContact(), request.getMobile(), request.getEmail(), request.getAddress(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM manufacturer WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

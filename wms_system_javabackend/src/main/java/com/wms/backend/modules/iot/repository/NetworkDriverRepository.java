package com.wms.backend.modules.iot.repository;

import com.wms.backend.modules.iot.dto.NetworkDriverSaveRequest;
import com.wms.backend.modules.iot.entity.NetworkDriver;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NetworkDriverRepository {
    private final JdbcTemplate jdbcTemplate;
    public NetworkDriverRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<NetworkDriver> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, protocol, version, vendor, status, configTemplate, remark, created_at AS createdAt, updated_at AS updatedAt FROM network_driver WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR protocol LIKE ? OR vendor LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<NetworkDriver>(NetworkDriver.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<NetworkDriver>(NetworkDriver.class), like, like, like, like);
        }
        if (hasText(status)) {
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<NetworkDriver>(NetworkDriver.class), status);
        }
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<NetworkDriver>(NetworkDriver.class));
    }

    public NetworkDriver findById(String id) {
        List<NetworkDriver> list = jdbcTemplate.query("SELECT id, code, name, protocol, version, vendor, status, configTemplate, remark, created_at AS createdAt, updated_at AS updatedAt FROM network_driver WHERE id = ?",
                new SqliteRowMapper<NetworkDriver>(NetworkDriver.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public NetworkDriver insert(NetworkDriverSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO network_driver (id, code, name, protocol, version, vendor, status, configTemplate, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getProtocol(), request.getVersion(), request.getVendor(), request.getStatus(), request.getConfigTemplate(), request.getRemark());
        return findById(id);
    }

    public NetworkDriver update(String id, NetworkDriverSaveRequest request) {
        jdbcTemplate.update("UPDATE network_driver SET code = ?, name = ?, protocol = ?, version = ?, vendor = ?, status = ?, configTemplate = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getProtocol(), request.getVersion(), request.getVendor(), request.getStatus(), request.getConfigTemplate(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM network_driver WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

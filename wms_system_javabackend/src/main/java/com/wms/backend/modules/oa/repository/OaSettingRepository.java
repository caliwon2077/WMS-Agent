package com.wms.backend.modules.oa.repository;

import com.wms.backend.modules.oa.dto.OaSettingSaveRequest;
import com.wms.backend.modules.oa.entity.OaSetting;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OaSettingRepository {
    private final JdbcTemplate jdbcTemplate;
    public OaSettingRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<OaSetting> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, description, currentValue, updatedAtLabel, maintainer, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM oa_setting WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR description LIKE ? OR maintainer LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<OaSetting>(OaSetting.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<OaSetting>(OaSetting.class), like, like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<OaSetting>(OaSetting.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<OaSetting>(OaSetting.class));
    }

    public OaSetting findById(String id) {
        List<OaSetting> list = jdbcTemplate.query("SELECT id, code, name, description, currentValue, updatedAtLabel, maintainer, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM oa_setting WHERE id = ?",
                new SqliteRowMapper<OaSetting>(OaSetting.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public OaSetting insert(OaSettingSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO oa_setting (id, code, name, description, currentValue, updatedAtLabel, maintainer, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getDescription(), request.getCurrentValue(), request.getUpdatedAtLabel(), request.getMaintainer(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public OaSetting update(String id, OaSettingSaveRequest request) {
        jdbcTemplate.update("UPDATE oa_setting SET code = ?, name = ?, description = ?, currentValue = ?, updatedAtLabel = ?, maintainer = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getDescription(), request.getCurrentValue(), request.getUpdatedAtLabel(), request.getMaintainer(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM oa_setting WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

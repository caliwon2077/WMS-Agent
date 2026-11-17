package com.wms.backend.modules.mdm.repository;

import com.wms.backend.modules.mdm.dto.WorkshopSaveRequest;
import com.wms.backend.modules.mdm.entity.Workshop;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WorkshopRepository {
    private final JdbcTemplate jdbcTemplate;
    public WorkshopRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Workshop> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, areaName, owner, teamCount, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM workshop WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR areaName LIKE ? OR owner LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Workshop>(Workshop.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Workshop>(Workshop.class), like, like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Workshop>(Workshop.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<Workshop>(Workshop.class));
    }

    public Workshop findById(String id) {
        List<Workshop> list = jdbcTemplate.query("SELECT id, code, name, areaName, owner, teamCount, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM workshop WHERE id = ?",
                new SqliteRowMapper<Workshop>(Workshop.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public Workshop insert(WorkshopSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO workshop (id, code, name, areaName, owner, teamCount, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getAreaName(), request.getOwner(), request.getTeamCount(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public Workshop update(String id, WorkshopSaveRequest request) {
        jdbcTemplate.update("UPDATE workshop SET code = ?, name = ?, areaName = ?, owner = ?, teamCount = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getAreaName(), request.getOwner(), request.getTeamCount(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM workshop WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

package com.wms.backend.modules.mdm.repository;

import com.wms.backend.modules.mdm.dto.WorkAreaSaveRequest;
import com.wms.backend.modules.mdm.entity.WorkArea;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WorkAreaRepository {
    private final JdbcTemplate jdbcTemplate;
    public WorkAreaRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<WorkArea> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, parentArea, owner, equipmentCount, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM work_area WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR parentArea LIKE ? OR owner LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<WorkArea>(WorkArea.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<WorkArea>(WorkArea.class), like, like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<WorkArea>(WorkArea.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<WorkArea>(WorkArea.class));
    }

    public WorkArea findById(String id) {
        List<WorkArea> list = jdbcTemplate.query("SELECT id, code, name, parentArea, owner, equipmentCount, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM work_area WHERE id = ?",
                new SqliteRowMapper<WorkArea>(WorkArea.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public WorkArea insert(WorkAreaSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO work_area (id, code, name, parentArea, owner, equipmentCount, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getParentArea(), request.getOwner(), request.getEquipmentCount(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public WorkArea update(String id, WorkAreaSaveRequest request) {
        jdbcTemplate.update("UPDATE work_area SET code = ?, name = ?, parentArea = ?, owner = ?, equipmentCount = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getParentArea(), request.getOwner(), request.getEquipmentCount(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM work_area WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

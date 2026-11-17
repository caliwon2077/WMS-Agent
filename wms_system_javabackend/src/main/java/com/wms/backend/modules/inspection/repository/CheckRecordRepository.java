package com.wms.backend.modules.inspection.repository;

import com.wms.backend.modules.inspection.dto.CheckRecordSaveRequest;
import com.wms.backend.modules.inspection.entity.CheckRecord;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CheckRecordRepository {
    private final JdbcTemplate jdbcTemplate;
    public CheckRecordRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<CheckRecord> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, equipmentName, executor, executeTime, result, status, planName, abnormalDesc, remark, created_at AS createdAt, updated_at AS updatedAt FROM check_record WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR equipmentName LIKE ? OR executor LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckRecord>(CheckRecord.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckRecord>(CheckRecord.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckRecord>(CheckRecord.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckRecord>(CheckRecord.class));
    }

    public CheckRecord findById(String id) {
        List<CheckRecord> list = jdbcTemplate.query("SELECT id, code, equipmentName, executor, executeTime, result, status, planName, abnormalDesc, remark, created_at AS createdAt, updated_at AS updatedAt FROM check_record WHERE id = ?", new SqliteRowMapper<CheckRecord>(CheckRecord.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public CheckRecord insert(CheckRecordSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO check_record (id, code, equipmentName, executor, executeTime, result, status, planName, abnormalDesc, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getEquipmentName(), request.getExecutor(), request.getExecuteTime(), request.getResult(), request.getStatus(), request.getPlanName(), request.getAbnormalDesc(), request.getRemark());
        return findById(id);
    }

    public CheckRecord update(String id, CheckRecordSaveRequest request) {
        jdbcTemplate.update("UPDATE check_record SET code = ?, equipmentName = ?, executor = ?, executeTime = ?, result = ?, status = ?, planName = ?, abnormalDesc = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getEquipmentName(), request.getExecutor(), request.getExecuteTime(), request.getResult(), request.getStatus(), request.getPlanName(), request.getAbnormalDesc(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM check_record WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

package com.wms.backend.modules.inspection.repository;

import com.wms.backend.modules.inspection.dto.CheckItemSaveRequest;
import com.wms.backend.modules.inspection.entity.CheckItem;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CheckItemRepository {
    private final JdbcTemplate jdbcTemplate;
    public CheckItemRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<CheckItem> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, judgeType, standardValue, equipmentType, riskLevel, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM check_item WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR equipmentType LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckItem>(CheckItem.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckItem>(CheckItem.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckItem>(CheckItem.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<CheckItem>(CheckItem.class));
    }

    public CheckItem findById(String id) {
        List<CheckItem> list = jdbcTemplate.query("SELECT id, code, name, judgeType, standardValue, equipmentType, riskLevel, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM check_item WHERE id = ?", new SqliteRowMapper<CheckItem>(CheckItem.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public CheckItem insert(CheckItemSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO check_item (id, code, name, judgeType, standardValue, equipmentType, riskLevel, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getJudgeType(), request.getStandardValue(), request.getEquipmentType(), request.getRiskLevel(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public CheckItem update(String id, CheckItemSaveRequest request) {
        jdbcTemplate.update("UPDATE check_item SET code = ?, name = ?, judgeType = ?, standardValue = ?, equipmentType = ?, riskLevel = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getJudgeType(), request.getStandardValue(), request.getEquipmentType(), request.getRiskLevel(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM check_item WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

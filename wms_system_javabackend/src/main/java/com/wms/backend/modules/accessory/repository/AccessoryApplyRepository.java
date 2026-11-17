package com.wms.backend.modules.accessory.repository;

import com.wms.backend.modules.accessory.dto.AccessoryApplySaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryApply;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AccessoryApplyRepository {
    private final JdbcTemplate jdbcTemplate;
    public AccessoryApplyRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    public List<AccessoryApply> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, applyNo, applicant, accessoryName, quantity, approvalStatus, deliveryStatus, warehouse, applyDate, purpose, receiver, remark, created_at AS createdAt, updated_at AS updatedAt FROM accessory_apply WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (applyNo LIKE ? OR applicant LIKE ? OR accessoryName LIKE ?) ");
        if (hasText(status)) sql.append("AND approvalStatus = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryApply>(AccessoryApply.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryApply>(AccessoryApply.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryApply>(AccessoryApply.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<AccessoryApply>(AccessoryApply.class));
    }
    public AccessoryApply findById(String id) {
        List<AccessoryApply> list = jdbcTemplate.query("SELECT id, applyNo, applicant, accessoryName, quantity, approvalStatus, deliveryStatus, warehouse, applyDate, purpose, receiver, remark, created_at AS createdAt, updated_at AS updatedAt FROM accessory_apply WHERE id = ?", new SqliteRowMapper<AccessoryApply>(AccessoryApply.class), id);
        return list.isEmpty() ? null : list.get(0);
    }
    public AccessoryApply insert(AccessoryApplySaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO accessory_apply (id, applyNo, applicant, accessoryName, quantity, approvalStatus, deliveryStatus, warehouse, applyDate, purpose, receiver, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getApplyNo(), request.getApplicant(), request.getAccessoryName(), request.getQuantity(), request.getApprovalStatus(), request.getDeliveryStatus(), request.getWarehouse(), request.getApplyDate(), request.getPurpose(), request.getReceiver(), request.getRemark());
        return findById(id);
    }
    public AccessoryApply update(String id, AccessoryApplySaveRequest request) {
        jdbcTemplate.update("UPDATE accessory_apply SET applyNo = ?, applicant = ?, accessoryName = ?, quantity = ?, approvalStatus = ?, deliveryStatus = ?, warehouse = ?, applyDate = ?, purpose = ?, receiver = ?, remark = ? WHERE id = ?",
                request.getApplyNo(), request.getApplicant(), request.getAccessoryName(), request.getQuantity(), request.getApprovalStatus(), request.getDeliveryStatus(), request.getWarehouse(), request.getApplyDate(), request.getPurpose(), request.getReceiver(), request.getRemark(), id);
        return findById(id);
    }
    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM accessory_apply WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

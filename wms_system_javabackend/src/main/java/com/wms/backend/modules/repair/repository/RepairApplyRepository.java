package com.wms.backend.modules.repair.repository;

import com.wms.backend.modules.repair.dto.RepairApplySaveRequest;
import com.wms.backend.modules.repair.entity.RepairApply;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class RepairApplyRepository {
    private final JdbcTemplate jdbcTemplate;
    public RepairApplyRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    public List<RepairApply> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, ticketNo, equipmentName, level, stage, team, status, applicant, applyDate, faultType, contact, description, created_at AS createdAt, updated_at AS updatedAt FROM repair_apply WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (ticketNo LIKE ? OR equipmentName LIKE ? OR applicant LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairApply>(RepairApply.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairApply>(RepairApply.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairApply>(RepairApply.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<RepairApply>(RepairApply.class));
    }
    public RepairApply findById(String id) {
        List<RepairApply> list = jdbcTemplate.query("SELECT id, ticketNo, equipmentName, level, stage, team, status, applicant, applyDate, faultType, contact, description, created_at AS createdAt, updated_at AS updatedAt FROM repair_apply WHERE id = ?", new SqliteRowMapper<RepairApply>(RepairApply.class), id);
        return list.isEmpty() ? null : list.get(0);
    }
    public RepairApply insert(RepairApplySaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO repair_apply (id, ticketNo, equipmentName, level, stage, team, status, applicant, applyDate, faultType, contact, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getTicketNo(), request.getEquipmentName(), request.getLevel(), request.getStage(), request.getTeam(), request.getStatus(), request.getApplicant(), request.getApplyDate(), request.getFaultType(), request.getContact(), request.getDescription());
        return findById(id);
    }
    public RepairApply update(String id, RepairApplySaveRequest request) {
        jdbcTemplate.update("UPDATE repair_apply SET ticketNo = ?, equipmentName = ?, level = ?, stage = ?, team = ?, status = ?, applicant = ?, applyDate = ?, faultType = ?, contact = ?, description = ? WHERE id = ?",
                request.getTicketNo(), request.getEquipmentName(), request.getLevel(), request.getStage(), request.getTeam(), request.getStatus(), request.getApplicant(), request.getApplyDate(), request.getFaultType(), request.getContact(), request.getDescription(), id);
        return findById(id);
    }
    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM repair_apply WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

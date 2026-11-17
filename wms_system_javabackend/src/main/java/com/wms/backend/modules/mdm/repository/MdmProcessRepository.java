package com.wms.backend.modules.mdm.repository;

import com.wms.backend.modules.mdm.dto.MdmProcessSaveRequest;
import com.wms.backend.modules.mdm.entity.MdmProcess;
import java.util.List;
import java.util.UUID;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MdmProcessRepository {
    private final JdbcTemplate jdbcTemplate;
    public MdmProcessRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<MdmProcess> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, code, name, standardHours, jobType, equipmentName, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM mdm_process WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (code LIKE ? OR name LIKE ? OR jobType LIKE ? OR equipmentName LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MdmProcess>(MdmProcess.class), like, like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MdmProcess>(MdmProcess.class), like, like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MdmProcess>(MdmProcess.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<MdmProcess>(MdmProcess.class));
    }

    public MdmProcess findById(String id) {
        List<MdmProcess> list = jdbcTemplate.query("SELECT id, code, name, standardHours, jobType, equipmentName, status, remark, created_at AS createdAt, updated_at AS updatedAt FROM mdm_process WHERE id = ?",
                new SqliteRowMapper<MdmProcess>(MdmProcess.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public MdmProcess insert(MdmProcessSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO mdm_process (id, code, name, standardHours, jobType, equipmentName, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getCode(), request.getName(), request.getStandardHours(), request.getJobType(), request.getEquipmentName(), request.getStatus(), request.getRemark());
        return findById(id);
    }

    public MdmProcess update(String id, MdmProcessSaveRequest request) {
        jdbcTemplate.update("UPDATE mdm_process SET code = ?, name = ?, standardHours = ?, jobType = ?, equipmentName = ?, status = ?, remark = ? WHERE id = ?",
                request.getCode(), request.getName(), request.getStandardHours(), request.getJobType(), request.getEquipmentName(), request.getStatus(), request.getRemark(), id);
        return findById(id);
    }

    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM mdm_process WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

package com.wms.backend.modules.user.repository;

import com.wms.backend.modules.user.dto.UserSaveRequest;
import com.wms.backend.modules.user.entity.SysUser;
import com.wms.backend.common.SqliteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    public UserRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    public List<SysUser> findAll(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, account, name, dept, role, mobile, status, email, syncStatus, remark, created_at AS createdAt, updated_at AS updatedAt FROM sys_user WHERE 1=1 ");
        if (hasText(keyword)) sql.append("AND (account LIKE ? OR name LIKE ? OR dept LIKE ?) ");
        if (hasText(status)) sql.append("AND status = ? ");
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        if (hasText(keyword) && hasText(status)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<SysUser>(SysUser.class), like, like, like, status);
        }
        if (hasText(keyword)) {
            String like = "%" + keyword.trim() + "%";
            return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<SysUser>(SysUser.class), like, like, like);
        }
        if (hasText(status)) return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<SysUser>(SysUser.class), status);
        return jdbcTemplate.query(sql.toString(), new SqliteRowMapper<SysUser>(SysUser.class));
    }
    public SysUser findById(String id) {
        List<SysUser> list = jdbcTemplate.query("SELECT id, account, name, dept, role, mobile, status, email, syncStatus, remark, created_at AS createdAt, updated_at AS updatedAt FROM sys_user WHERE id = ?", new SqliteRowMapper<SysUser>(SysUser.class), id);
        return list.isEmpty() ? null : list.get(0);
    }
    public SysUser insert(UserSaveRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update("INSERT INTO sys_user (id, account, name, dept, role, mobile, status, email, syncStatus, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, request.getAccount(), request.getName(), request.getDept(), request.getRole(), request.getMobile(), request.getStatus(), request.getEmail(), request.getSyncStatus(), request.getRemark());
        return findById(id);
    }
    public SysUser update(String id, UserSaveRequest request) {
        jdbcTemplate.update("UPDATE sys_user SET account = ?, name = ?, dept = ?, role = ?, mobile = ?, status = ?, email = ?, syncStatus = ?, remark = ? WHERE id = ?",
                request.getAccount(), request.getName(), request.getDept(), request.getRole(), request.getMobile(), request.getStatus(), request.getEmail(), request.getSyncStatus(), request.getRemark(), id);
        return findById(id);
    }
    public int deleteById(String id) { return jdbcTemplate.update("DELETE FROM sys_user WHERE id = ?", id); }
    private boolean hasText(String text) { return text != null && text.trim().length() > 0; }
}

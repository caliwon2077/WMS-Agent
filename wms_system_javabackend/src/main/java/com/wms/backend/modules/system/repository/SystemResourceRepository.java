package com.wms.backend.modules.system.repository;

import com.wms.backend.common.exception.BusinessException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SystemResourceRepository {
    private final JdbcTemplate jdbcTemplate;

    public SystemResourceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> findAll(ResourceConfig config, String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT id, ");
        sql.append(String.join(", ", config.getColumns()));
        sql.append(", created_at AS createdAt, updated_at AS updatedAt FROM ");
        sql.append(config.getTableName());
        sql.append(" WHERE 1=1 ");

        List<Object> args = new ArrayList<Object>();
        if (hasText(keyword) && !config.getKeywordColumns().isEmpty()) {
            sql.append("AND (");
            sql.append(config.getKeywordColumns().stream().map(column -> column + " LIKE ?").collect(Collectors.joining(" OR ")));
            sql.append(") ");
            String like = "%" + keyword.trim() + "%";
            for (int i = 0; i < config.getKeywordColumns().size(); i++) {
                args.add(like);
            }
        }
        if (hasText(status) && hasText(config.getStatusColumn())) {
            sql.append("AND ").append(config.getStatusColumn()).append(" = ? ");
            args.add(status);
        }
        sql.append("ORDER BY updated_at DESC, created_at DESC");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    public Map<String, Object> findById(ResourceConfig config, String id) {
        String sql = "SELECT id, " + String.join(", ", config.getColumns()) + ", created_at AS createdAt, updated_at AS updatedAt FROM " + config.getTableName() + " WHERE id = ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public Map<String, Object> insert(ResourceConfig config, Map<String, Object> payload) {
        String id = UUID.randomUUID().toString().replace("-", "");
        List<String> insertColumns = new ArrayList<String>();
        insertColumns.add("id");
        insertColumns.addAll(config.getColumns());

        List<Object> values = new ArrayList<Object>();
        values.add(id);
        for (String column : config.getColumns()) {
            values.add(payload.get(column));
        }

        String sql = "INSERT INTO " + config.getTableName() + " (" + String.join(", ", insertColumns) + ") VALUES (" +
                insertColumns.stream().map(item -> "?").collect(Collectors.joining(", ")) + ")";
        jdbcTemplate.update(sql, values.toArray());
        return findById(config, id);
    }

    public Map<String, Object> update(ResourceConfig config, String id, Map<String, Object> payload) {
        String sql = "UPDATE " + config.getTableName() + " SET " +
                config.getColumns().stream().map(column -> column + " = ?").collect(Collectors.joining(", ")) +
                " WHERE id = ?";
        List<Object> values = new ArrayList<Object>();
        for (String column : config.getColumns()) {
            values.add(payload.get(column));
        }
        values.add(id);
        jdbcTemplate.update(sql, values.toArray());
        return findById(config, id);
    }

    public int deleteById(ResourceConfig config, String id) {
        return jdbcTemplate.update("DELETE FROM " + config.getTableName() + " WHERE id = ?", id);
    }

    public ResourceConfig config(String resourceKey) {
        ResourceConfig config = configs().get(resourceKey);
        if (config == null) {
            throw new BusinessException("NOT_FOUND", "System resource not found: " + resourceKey);
        }
        return config;
    }

    private Map<String, ResourceConfig> configs() {
        Map<String, ResourceConfig> map = new LinkedHashMap<String, ResourceConfig>();
        map.put("menu", new ResourceConfig("sys_menu",
                Arrays.asList("name", "type", "route", "component", "sortOrder", "status", "remark"),
                Arrays.asList("name", "type", "route", "component"),
                "status",
                Arrays.asList("name", "type", "route", "component", "sortOrder", "status")));
        map.put("dictTree", new ResourceConfig("sys_dict_tree",
                Arrays.asList("code", "name", "level", "detailCount", "modifyCount", "status", "remark"),
                Arrays.asList("code", "name"),
                "status",
                Arrays.asList("code", "name", "level", "detailCount", "modifyCount", "status")));
        map.put("setting", new ResourceConfig("sys_setting",
                Arrays.asList("configKey", "name", "currentValue", "category", "modifyCount", "status", "remark"),
                Arrays.asList("configKey", "name", "category"),
                "status",
                Arrays.asList("configKey", "name", "currentValue", "category", "modifyCount", "status")));
        map.put("apiLog", new ResourceConfig("sys_api_log",
                Arrays.asList("logNo", "apiPath", "operator", "duration", "result", "logTime", "remark"),
                Arrays.asList("logNo", "apiPath", "operator"),
                "result",
                Arrays.asList("logNo", "apiPath", "operator", "duration", "result", "logTime")));
        map.put("quartz", new ResourceConfig("sys_quartz",
                Arrays.asList("code", "name", "cron", "lastExecute", "result", "status", "remark"),
                Arrays.asList("code", "name", "cron"),
                "status",
                Arrays.asList("code", "name", "cron", "lastExecute", "result", "status")));
        map.put("org", new ResourceConfig("sys_org",
                Arrays.asList("code", "name", "parentOrg", "owner", "userCount", "status", "remark"),
                Arrays.asList("code", "name", "parentOrg", "owner"),
                "status",
                Arrays.asList("code", "name", "parentOrg", "owner", "userCount", "status")));
        map.put("city", new ResourceConfig("sys_city",
                Arrays.asList("code", "name", "province", "region", "warehouseCount", "status", "remark"),
                Arrays.asList("code", "name", "province", "region"),
                "status",
                Arrays.asList("code", "name", "province", "region", "warehouseCount", "status")));
        map.put("dictSys", new ResourceConfig("sys_dict",
                Arrays.asList("code", "name", "category", "itemCount", "modifyCount", "status", "remark"),
                Arrays.asList("code", "name", "category"),
                "status",
                Arrays.asList("code", "name", "category", "itemCount", "modifyCount", "status")));
        map.put("autoCode", new ResourceConfig("sys_auto_code",
                Arrays.asList("code", "bizObject", "prefix", "ruleExample", "resetCycle", "status", "remark"),
                Arrays.asList("code", "bizObject", "prefix"),
                "status",
                Arrays.asList("code", "bizObject", "prefix", "ruleExample", "resetCycle", "status")));
        map.put("dict", new ResourceConfig("sys_common_dict",
                Arrays.asList("code", "name", "valueType", "itemCount", "modifyCount", "status", "remark"),
                Arrays.asList("code", "name", "valueType"),
                "status",
                Arrays.asList("code", "name", "valueType", "itemCount", "modifyCount", "status")));
        map.put("role", new ResourceConfig("sys_role",
                Arrays.asList("code", "name", "userCount", "menuCount", "permissionCount", "status", "remark"),
                Arrays.asList("code", "name"),
                "status",
                Arrays.asList("code", "name", "userCount", "menuCount", "permissionCount", "status")));
        map.put("dictItem", new ResourceConfig("sys_dict_item",
                Arrays.asList("dictName", "itemCode", "itemName", "sortOrder", "defaultValue", "status", "remark"),
                Arrays.asList("dictName", "itemCode", "itemName"),
                "status",
                Arrays.asList("dictName", "itemCode", "itemName", "sortOrder", "defaultValue", "status")));
        map.put("dept", new ResourceConfig("sys_dept",
                Arrays.asList("code", "name", "parentDept", "owner", "syncStatus", "status", "remark"),
                Arrays.asList("code", "name", "parentDept", "owner"),
                "status",
                Arrays.asList("code", "name", "parentDept", "owner", "syncStatus", "status")));
        map.put("permissions", new ResourceConfig("sys_permissions",
                Arrays.asList("targetName", "targetType", "menuCount", "buttonCount", "lastAdjust", "status", "remark"),
                Arrays.asList("targetName", "targetType"),
                "status",
                Arrays.asList("targetName", "targetType", "menuCount", "buttonCount", "lastAdjust", "status")));
        return map;
    }

    private boolean hasText(String text) {
        return text != null && text.trim().length() > 0;
    }

    public static class ResourceConfig {
        private final String tableName;
        private final List<String> columns;
        private final List<String> keywordColumns;
        private final String statusColumn;
        private final List<String> requiredColumns;

        public ResourceConfig(String tableName, List<String> columns, List<String> keywordColumns, String statusColumn, List<String> requiredColumns) {
            this.tableName = tableName;
            this.columns = columns;
            this.keywordColumns = keywordColumns;
            this.statusColumn = statusColumn;
            this.requiredColumns = requiredColumns;
        }

        public String getTableName() { return tableName; }
        public List<String> getColumns() { return columns; }
        public List<String> getKeywordColumns() { return keywordColumns; }
        public String getStatusColumn() { return statusColumn; }
        public List<String> getRequiredColumns() { return requiredColumns; }
    }
}

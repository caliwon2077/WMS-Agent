package com.wms.backend;

import com.wms.backend.config.SqliteDatabaseConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SqliteIntegrationTest {
    private static final Path DATABASE;
    static {
        try { DATABASE = Files.createTempDirectory("wms-sqlite-test-").resolve("wms.sqlite"); }
        catch (Exception e) { throw new IllegalStateException(e); }
    }
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.database.path", () -> DATABASE.toString());
        registry.add("app.demo.enabled", () -> false);
    }
    @Autowired TestRestTemplate http;
    @Autowired JdbcTemplate jdbc;

    @Test
    void initializesEveryModuleAndRoundTripsDatesAndPersistsChangesOnReopen() throws Exception {
        String[] endpoints = {"equipment/manufacturers", "equipment/categories", "equipment/assets",
                "inspection/plans", "inspection/items", "inspection/records", "maintenance/plans",
                "maintenance/items", "maintenance/records", "repair/applies", "accessory/applies", "system/users",
                "iot/device-models", "iot/device-instances", "iot/network-drivers", "repair/stages", "repair/processes",
                "mdm/materials", "mdm/work-areas", "mdm/workshops", "mdm/processes", "oa/settings", "oa/flows",
                "oa/approval-types", "accessory/items", "accessory/categories", "accessory/suppliers", "accessory/warehouses"};
        for (String endpoint : endpoints) {
            Map<?, ?> response = http.getForObject("/v1/" + endpoint, Map.class);
            assertEquals(true, response.get("success"), endpoint + ": " + response);
        }
        Map<String, Object> asset = new LinkedHashMap<>();
        asset.put("code", "TEST-ASSET"); asset.put("name", "测试设备");
        asset.put("category", "测试分类"); asset.put("workshop", "测试车间");
        asset.put("owner", "测试员"); asset.put("status", "运行中");
        asset.put("purchaseDate", "2026-11-08");
        String assetUrl = "/v1/equipment/assets";
        Map<?, ?> assetResponse = http.postForObject(assetUrl, asset, Map.class);
        assertEquals(true, assetResponse.get("success"), assetResponse.toString());
        Map<?, ?> assetData = (Map<?, ?>) assetResponse.get("data");
        assertEquals("2026-11-08", assetData.get("purchaseDate"));
        http.delete(assetUrl + "/" + assetData.get("id"));
        Map<String, Object> record = new LinkedHashMap<>();
        record.put("code", "TEST-DATE"); record.put("equipmentName", "测试设备");
        record.put("executor", "测试员"); record.put("executeTime", "2026-11-08T17:30:00");
        record.put("result", "正常"); record.put("status", "已完成");
        String url = "/v1/inspection/records";
        Map<?, ?> created = http.postForObject(url, record, Map.class);
        assertEquals(true, created.get("success"), created.toString());
        Map<?, ?> data = (Map<?, ?>) created.get("data");
        assertEquals("2026-11-08T17:30:00", data.get("executeTime"));
        String id = (String) data.get("id");
        jdbc.update("UPDATE check_record SET updated_at = '2000-01-01 00:00:00' WHERE id = ?", id);
        record.put("remark", "已修改");
        Map<?, ?> updated = http.exchange(url + "/" + id, HttpMethod.PUT, new HttpEntity<>(record), Map.class).getBody();
        assertEquals(true, updated.get("success"), updated.toString());
        assertNotEquals("2000-01-01T00:00:00", ((Map<?, ?>) updated.get("data")).get("updatedAt"));
        jdbc.update("DELETE FROM equipment_assets WHERE id = 'eq-1'");
        try (HikariDataSource reopened = new SqliteDatabaseConfig().dataSource(DATABASE.toString())) {
            JdbcTemplate again = new JdbcTemplate(reopened);
            assertEquals(0, again.queryForObject("SELECT COUNT(*) FROM equipment_assets WHERE id = 'eq-1'", Integer.class));
            assertEquals("已修改", again.queryForObject("SELECT remark FROM check_record WHERE id = ?", String.class, id));
        }
        Map<?, ?> deleted = http.exchange(url + "/" + id, HttpMethod.DELETE, HttpEntity.EMPTY, Map.class).getBody();
        assertEquals(true, deleted.get("success"));
        assertEquals(0, jdbc.queryForObject("SELECT COUNT(*) FROM check_record WHERE id = ?", Integer.class, id));
    }
}

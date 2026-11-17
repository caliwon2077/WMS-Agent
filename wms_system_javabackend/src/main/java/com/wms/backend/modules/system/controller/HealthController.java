package com.wms.backend.modules.system.controller;

import com.wms.backend.common.api.ApiResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/system")
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/health")
    public ApiResponse<Map<String, Object>> health() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("database", result != null && result == 1 ? "UP" : "DOWN");
        data.put("service", "UP");
        return ApiResponse.success("服务正常", data);
    }
}

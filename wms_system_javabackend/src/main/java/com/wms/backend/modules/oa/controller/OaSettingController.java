package com.wms.backend.modules.oa.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.oa.dto.OaSettingSaveRequest;
import com.wms.backend.modules.oa.entity.OaSetting;
import com.wms.backend.modules.oa.service.OaSettingService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/oa/settings")
public class OaSettingController {
    private final OaSettingService service;
    public OaSettingController(OaSettingService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<OaSetting>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "status", required = false) String status) {
        List<OaSetting> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<OaSetting>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<OaSetting> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<OaSetting> create(@Valid @RequestBody OaSettingSaveRequest request) {
        return ApiResponse.success("Created successfully", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<OaSetting> update(@PathVariable("id") String id, @Valid @RequestBody OaSettingSaveRequest request) {
        return ApiResponse.success("Updated successfully", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("Deleted successfully", null);
    }
}

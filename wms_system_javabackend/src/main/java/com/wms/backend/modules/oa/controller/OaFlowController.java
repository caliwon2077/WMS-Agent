package com.wms.backend.modules.oa.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.oa.dto.OaFlowSaveRequest;
import com.wms.backend.modules.oa.entity.OaFlow;
import com.wms.backend.modules.oa.service.OaFlowService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/oa/flows")
public class OaFlowController {
    private final OaFlowService service;
    public OaFlowController(OaFlowService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<OaFlow>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                     @RequestParam(value = "status", required = false) String status) {
        List<OaFlow> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<OaFlow>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<OaFlow> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<OaFlow> create(@Valid @RequestBody OaFlowSaveRequest request) {
        return ApiResponse.success("Created successfully", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<OaFlow> update(@PathVariable("id") String id, @Valid @RequestBody OaFlowSaveRequest request) {
        return ApiResponse.success("Updated successfully", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("Deleted successfully", null);
    }
}

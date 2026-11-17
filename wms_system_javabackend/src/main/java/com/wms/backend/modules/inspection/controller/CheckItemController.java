package com.wms.backend.modules.inspection.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.inspection.dto.CheckItemSaveRequest;
import com.wms.backend.modules.inspection.entity.CheckItem;
import com.wms.backend.modules.inspection.service.CheckItemService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/inspection/items")
public class CheckItemController {
    private final CheckItemService service;
    public CheckItemController(CheckItemService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<CheckItem>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "status", required = false) String status) {
        List<CheckItem> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<CheckItem>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<CheckItem> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<CheckItem> create(@Valid @RequestBody CheckItemSaveRequest request) { return ApiResponse.success("新增成功", service.create(request)); }

    @PutMapping("/{id}")
    public ApiResponse<CheckItem> update(@PathVariable("id") String id, @Valid @RequestBody CheckItemSaveRequest request) { return ApiResponse.success("修改成功", service.update(id, request)); }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("删除成功", null); }
}

package com.wms.backend.modules.accessory.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.accessory.dto.AccessoryItemSaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryItem;
import com.wms.backend.modules.accessory.service.AccessoryItemService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/accessory/items")
public class AccessoryItemController {
    private final AccessoryItemService service;
    public AccessoryItemController(AccessoryItemService service) { this.service = service; }
    @GetMapping
    public ApiResponse<ApiPageResponse<AccessoryItem>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                            @RequestParam(value = "status", required = false) String status) {
        List<AccessoryItem> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<AccessoryItem>(records, records.size()));
    }
    @GetMapping("/{id}")
    public ApiResponse<AccessoryItem> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }
    @PostMapping
    public ApiResponse<AccessoryItem> create(@Valid @RequestBody AccessoryItemSaveRequest request) { return ApiResponse.success("Created successfully", service.create(request)); }
    @PutMapping("/{id}")
    public ApiResponse<AccessoryItem> update(@PathVariable("id") String id, @Valid @RequestBody AccessoryItemSaveRequest request) { return ApiResponse.success("Updated successfully", service.update(id, request)); }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("Deleted successfully", null); }
}

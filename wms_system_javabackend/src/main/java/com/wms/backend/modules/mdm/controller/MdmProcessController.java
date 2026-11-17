package com.wms.backend.modules.mdm.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.mdm.dto.MdmProcessSaveRequest;
import com.wms.backend.modules.mdm.entity.MdmProcess;
import com.wms.backend.modules.mdm.service.MdmProcessService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/mdm/processes")
public class MdmProcessController {
    private final MdmProcessService service;
    public MdmProcessController(MdmProcessService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<MdmProcess>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                         @RequestParam(value = "status", required = false) String status) {
        List<MdmProcess> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<MdmProcess>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<MdmProcess> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<MdmProcess> create(@Valid @RequestBody MdmProcessSaveRequest request) {
        return ApiResponse.success("Created successfully", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<MdmProcess> update(@PathVariable("id") String id, @Valid @RequestBody MdmProcessSaveRequest request) {
        return ApiResponse.success("Updated successfully", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("Deleted successfully", null);
    }
}

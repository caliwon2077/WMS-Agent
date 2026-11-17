package com.wms.backend.modules.oa.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.oa.dto.ApprovalTypeSaveRequest;
import com.wms.backend.modules.oa.entity.ApprovalType;
import com.wms.backend.modules.oa.repository.ApprovalTypeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ApprovalTypeService {
    private final ApprovalTypeRepository repository;
    public ApprovalTypeService(ApprovalTypeRepository repository) { this.repository = repository; }

    public List<ApprovalType> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public ApprovalType detail(String id) {
        ApprovalType record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "Approval type not found");
        return record;
    }
    public ApprovalType create(ApprovalTypeSaveRequest request) { return repository.insert(request); }
    public ApprovalType update(String id, ApprovalTypeSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}

package com.wms.backend.modules.oa.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.oa.dto.OaFlowSaveRequest;
import com.wms.backend.modules.oa.entity.OaFlow;
import com.wms.backend.modules.oa.repository.OaFlowRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OaFlowService {
    private final OaFlowRepository repository;
    public OaFlowService(OaFlowRepository repository) { this.repository = repository; }

    public List<OaFlow> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public OaFlow detail(String id) {
        OaFlow record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "OA flow not found");
        return record;
    }
    public OaFlow create(OaFlowSaveRequest request) { return repository.insert(request); }
    public OaFlow update(String id, OaFlowSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}

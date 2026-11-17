package com.wms.backend.modules.oa.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.oa.dto.OaSettingSaveRequest;
import com.wms.backend.modules.oa.entity.OaSetting;
import com.wms.backend.modules.oa.repository.OaSettingRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OaSettingService {
    private final OaSettingRepository repository;
    public OaSettingService(OaSettingRepository repository) { this.repository = repository; }

    public List<OaSetting> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public OaSetting detail(String id) {
        OaSetting record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "OA setting not found");
        return record;
    }
    public OaSetting create(OaSettingSaveRequest request) { return repository.insert(request); }
    public OaSetting update(String id, OaSettingSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}

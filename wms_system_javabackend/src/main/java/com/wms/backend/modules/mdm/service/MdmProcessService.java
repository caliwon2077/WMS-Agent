package com.wms.backend.modules.mdm.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.mdm.dto.MdmProcessSaveRequest;
import com.wms.backend.modules.mdm.entity.MdmProcess;
import com.wms.backend.modules.mdm.repository.MdmProcessRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MdmProcessService {
    private final MdmProcessRepository repository;
    public MdmProcessService(MdmProcessRepository repository) { this.repository = repository; }

    public List<MdmProcess> list(String keyword, String status) { return repository.findAll(keyword, status); }

    public MdmProcess detail(String id) {
        MdmProcess record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "Process not found");
        return record;
    }

    public MdmProcess create(MdmProcessSaveRequest request) { return repository.insert(request); }
    public MdmProcess update(String id, MdmProcessSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}

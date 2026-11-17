package com.wms.backend.modules.system.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.system.repository.SystemResourceRepository;
import com.wms.backend.modules.system.repository.SystemResourceRepository.ResourceConfig;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SystemResourceService {
    private final SystemResourceRepository repository;

    public SystemResourceService(SystemResourceRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> list(String resourceKey, String keyword, String status) {
        return repository.findAll(repository.config(resourceKey), keyword, status);
    }

    public Map<String, Object> detail(String resourceKey, String id) {
        Map<String, Object> record = repository.findById(repository.config(resourceKey), id);
        if (record == null) {
            throw new BusinessException("NOT_FOUND", "System resource record not found");
        }
        return record;
    }

    public Map<String, Object> create(String resourceKey, Map<String, Object> payload) {
        ResourceConfig config = repository.config(resourceKey);
        validate(config, payload);
        return repository.insert(config, payload);
    }

    public Map<String, Object> update(String resourceKey, String id, Map<String, Object> payload) {
        ResourceConfig config = repository.config(resourceKey);
        detail(resourceKey, id);
        validate(config, payload);
        return repository.update(config, id, payload);
    }

    public void delete(String resourceKey, String id) {
        ResourceConfig config = repository.config(resourceKey);
        detail(resourceKey, id);
        repository.deleteById(config, id);
    }

    private void validate(ResourceConfig config, Map<String, Object> payload) {
        for (String column : config.getRequiredColumns()) {
            Object value = payload.get(column);
            if (value == null || String.valueOf(value).trim().isEmpty()) {
                throw new BusinessException("VALIDATION_ERROR", column + " is required");
            }
        }
    }
}

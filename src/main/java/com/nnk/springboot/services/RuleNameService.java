package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RuleNameService {

    private static final Logger log = LoggerFactory.getLogger(RuleNameService.class);

    private final RuleNameRepository ruleNameRepository;

    public RuleNameService(RuleNameRepository ruleNameRepository) {
        this.ruleNameRepository = ruleNameRepository;
    }

    @Transactional(readOnly = true)
    public Page<RuleName> findAll(Pageable pageable) {
        return ruleNameRepository.findAll(pageable);
    }

    @Transactional
    public RuleName save(RuleName ruleName) {
        RuleName saved = ruleNameRepository.save(ruleName);
        log.info("RuleName créé — id: {}, name: {}", saved.getId(), saved.getName());
        return saved;
    }

    @Transactional(readOnly = true)
    public RuleName findById(Integer id) {
        return ruleNameRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("RuleName introuvable — id: {}", id);
                    return new ResourceNotFoundException("RuleName not found for id: " + id);
                });
    }

    @Transactional
    public RuleName update(Integer id, RuleName form) {
        RuleName existing = findById(id);
        existing.setName(form.getName());
        existing.setDescription(form.getDescription());
        existing.setJson(form.getJson());
        existing.setTemplate(form.getTemplate());
        existing.setSqlStr(form.getSqlStr());
        existing.setSqlPart(form.getSqlPart());
        log.info("RuleName mis à jour — id: {}, name: {}", id, existing.getName());
        return existing;
    }

    @Transactional
    public void delete(Integer id) {
        RuleName ruleName = findById(id);
        ruleNameRepository.delete(ruleName);
        log.info("RuleName supprimé — id: {}", id);
    }
}

package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurvePointService {

    private static final Logger log = LoggerFactory.getLogger(CurvePointService.class);

    private final CurvePointRepository curvePointRepository;

    public CurvePointService(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    @Transactional(readOnly = true)
    public Page<CurvePoint> findAll(Pageable pageable) {
        return curvePointRepository.findAll(pageable);
    }

    @Transactional
    public CurvePoint save(CurvePoint curvePoint) {
        CurvePoint saved = curvePointRepository.save(curvePoint);
        log.info("CurvePoint créé — id: {}", saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public CurvePoint findById(Integer id) {
        return curvePointRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("CurvePoint introuvable — id: {}", id);
                    return new ResourceNotFoundException("CurvePoint not found for id: " + id);
                });
    }

    @Transactional
    public CurvePoint update(Integer id, CurvePoint form) {
        CurvePoint existing = findById(id);
        existing.setCurveId(form.getCurveId());
        existing.setAsOfDate(form.getAsOfDate());
        existing.setTerm(form.getTerm());
        existing.setValue(form.getValue());
        log.info("CurvePoint mis à jour — id: {}", id);
        return existing;
    }

    @Transactional
    public void delete(Integer id) {
        CurvePoint curvePoint = findById(id);
        curvePointRepository.delete(curvePoint);
        log.info("CurvePoint supprimé — id: {}", id);
    }
}

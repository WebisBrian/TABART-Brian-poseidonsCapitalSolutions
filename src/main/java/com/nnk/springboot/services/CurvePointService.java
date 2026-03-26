package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurvePointService {

    private final CurvePointRepository curvePointRepository;

    public CurvePointService(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    @Transactional(readOnly = true)
    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    @Transactional
    public CurvePoint save(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    @Transactional(readOnly = true)
    public CurvePoint findById(Integer id) {
        return curvePointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CurvePoint not found for id: " + id));
    }

    @Transactional
    public CurvePoint update(Integer id, CurvePoint form) {
        CurvePoint existing = findById(id);
        existing.setCurveId(form.getCurveId());
        existing.setAsOfDate(form.getAsOfDate());
        existing.setTerm(form.getTerm());
        existing.setValue(form.getValue());
        return existing;
    }

    @Transactional
    public void delete(Integer id) {
        CurvePoint curvePoint = findById(id);
        curvePointRepository.delete(curvePoint);
    }
}

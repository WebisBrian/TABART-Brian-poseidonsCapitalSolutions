package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Transactional(readOnly = true)
    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    @Transactional
    public Trade save(Trade trade) {
        return tradeRepository.save(trade);
    }

    @Transactional(readOnly = true)
    public Trade findById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trade not found for id: " + id));
    }

    @Transactional
    public Trade update(Integer id, Trade form) {
        Trade existing = findById(id);
        existing.setAccount(form.getAccount());
        existing.setType(form.getType());
        existing.setBuyQuantity(form.getBuyQuantity());
        return existing;
    }

    @Transactional
    public void delete(Integer id) {
        Trade trade = findById(id);
        tradeRepository.delete(trade);
    }
}

package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TradeService {

    private static final Logger log = LoggerFactory.getLogger(TradeService.class);

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Transactional(readOnly = true)
    public Page<Trade> findAll(Pageable pageable) {
        return tradeRepository.findAll(pageable);
    }

    @Transactional
    public Trade save(Trade trade) {
        Trade saved = tradeRepository.save(trade);
        log.info("Trade créé — id: {}, account: {}, type: {}", saved.getTradeId(), saved.getAccount(), saved.getType());
        return saved;
    }

    @Transactional(readOnly = true)
    public Trade findById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Trade introuvable — id: {}", id);
                    return new ResourceNotFoundException("Trade not found for id: " + id);
                });
    }

    @Transactional
    public Trade update(Integer id, Trade form) {
        Trade existing = findById(id);
        existing.setAccount(form.getAccount());
        existing.setType(form.getType());
        existing.setBuyQuantity(form.getBuyQuantity());
        log.info("Trade mis à jour — id: {}", id);
        return existing;
    }

    @Transactional
    public void delete(Integer id) {
        Trade trade = findById(id);
        tradeRepository.delete(trade);
        log.info("Trade supprimé — id: {}", id);
    }
}

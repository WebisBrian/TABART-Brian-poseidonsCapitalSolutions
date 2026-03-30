package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BidListService {

    private static final Logger log = LoggerFactory.getLogger(BidListService.class);

    private final BidListRepository bidListRepository;

    public BidListService(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    @Transactional(readOnly = true)
    public Page<BidList> findAll(Pageable pageable) {
        return bidListRepository.findAll(pageable);
    }

    @Transactional
    public BidList save(BidList bidList) {
        BidList saved = bidListRepository.save(bidList);
        log.info("BidList créée — id: {}, account: {}", saved.getBidListId(), saved.getAccount());
        return saved;
    }

    @Transactional(readOnly = true)
    public BidList findById(Integer id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("BidList introuvable — id: {}", id);
                    return new ResourceNotFoundException("BidList not found for id: " + id);
                });
    }

    @Transactional
    public BidList update(Integer id, BidList form) {
        BidList existing = findById(id);
        existing.setAccount(form.getAccount());
        existing.setType(form.getType());
        existing.setBidQuantity(form.getBidQuantity());
        log.info("BidList mise à jour — id: {}", id);
        return existing;
    }

    @Transactional
    public void delete(Integer id) {
        BidList bidList = findById(id);
        bidListRepository.delete(bidList);
        log.info("BidList supprimée — id: {}", id);
    }
}

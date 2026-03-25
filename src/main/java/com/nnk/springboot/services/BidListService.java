package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BidListService {

    private final BidListRepository bidListRepository;

    public BidListService(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    @Transactional(readOnly = true)
    public List<BidList> findAll() {
        return bidListRepository.findAll();
    }

    @Transactional
    public BidList save(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    @Transactional(readOnly = true)
    public BidList findById(Integer id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BidList not found for id: " + id));
    }

    @Transactional
    public BidList update(Integer id, BidList form) {
        BidList existing = findById(id);
        existing.setAccount(form.getAccount());
        existing.setType(form.getType());
        existing.setBidQuantity(form.getBidQuantity());
        return bidListRepository.save(existing);
    }

    @Transactional
    public void delete(Integer id) {
        BidList bidList = findById(id);
        bidListRepository.delete(bidList);
    }
}

package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingService {

    private static final Logger log = LoggerFactory.getLogger(RatingService.class);

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Transactional(readOnly = true)
    public Page<Rating> findAll(Pageable pageable) {
        return ratingRepository.findAll(pageable);
    }

    @Transactional
    public Rating save(Rating rating) {
        Rating saved = ratingRepository.save(rating);
        log.info("Rating créé — id: {}", saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public Rating findById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Rating introuvable — id: {}", id);
                    return new ResourceNotFoundException("Rating not found for id: " + id);
                });
    }

    @Transactional
    public Rating update(Integer id, Rating form) {
        Rating existing = findById(id);
        existing.setMoodysRating(form.getMoodysRating());
        existing.setSandPRating(form.getSandPRating());
        existing.setFitchRating(form.getFitchRating());
        existing.setOrderNumber(form.getOrderNumber());
        log.info("Rating mis à jour — id: {}", id);
        return existing;
    }

    @Transactional
    public void delete(Integer id) {
        Rating rating = findById(id);
        ratingRepository.delete(rating);
        log.info("Rating supprimé — id: {}", id);
    }
}

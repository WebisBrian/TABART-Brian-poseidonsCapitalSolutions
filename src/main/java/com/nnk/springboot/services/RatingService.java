package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingService {

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
        return ratingRepository.save(rating);
    }

    @Transactional(readOnly = true)
    public Rating findById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for id: " + id));
    }

    @Transactional
    public Rating update(Integer id, Rating form) {
        Rating existing = findById(id);
        existing.setMoodysRating(form.getMoodysRating());
        existing.setSandPRating(form.getSandPRating());
        existing.setFitchRating(form.getFitchRating());
        existing.setOrderNumber(form.getOrderNumber());
        return existing;
    }

    @Transactional
    public void delete(Integer id) {
        Rating rating = findById(id);
        ratingRepository.delete(rating);
    }
}

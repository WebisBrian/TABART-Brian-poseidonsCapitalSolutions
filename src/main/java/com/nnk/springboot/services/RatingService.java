package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating findById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for id: " + id));
    }

    public Rating update(Integer id, Rating form) {
        Rating existing = findById(id);
        existing.setMoodysRating(form.getMoodysRating());
        existing.setSandPRating(form.getSandPRating());
        existing.setFitchRating(form.getFitchRating());
        existing.setOrderNumber(form.getOrderNumber());
        return ratingRepository.save(existing);
    }

    public void delete(Integer id) {
        Rating rating = findById(id);
        ratingRepository.delete(rating);
    }
}

package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    // --- findAll ---

    @Test
    void findAll_whenRepositoryReturnsItems_shouldReturnList() {
        Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
        when(ratingRepository.findAll()).thenReturn(List.of(rating));

        List<Rating> result = ratingService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getOrderNumber()).isEqualTo(10);
    }

    @Test
    void findAll_whenRepositoryReturnsEmpty_shouldReturnEmptyList() {
        when(ratingRepository.findAll()).thenReturn(List.of());

        List<Rating> result = ratingService.findAll();

        assertThat(result).isEmpty();
    }

    // --- save ---

    @Test
    void save_whenValidRating_shouldPersistAndReturn() {
        Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating result = ratingService.save(rating);

        assertThat(result).isNotNull();
        assertThat(result.getOrderNumber()).isEqualTo(10);
        verify(ratingRepository, times(1)).save(rating);
    }

    // --- findById ---

    @Test
    void findById_whenIdExists_shouldReturnRating() {
        Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        Rating result = ratingService.findById(1);

        assertThat(result.getOrderNumber()).isEqualTo(10);
    }

    @Test
    void findById_whenIdNotFound_shouldThrowException() {
        when(ratingRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.findById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- update ---

    @Test
    void update_whenIdExists_shouldUpdateFields() {
        Rating existing = new Rating("OldMoodys", "OldSandP", "OldFitch", 1);
        Rating form = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
        when(ratingRepository.findById(1)).thenReturn(Optional.of(existing));
        when(ratingRepository.save(any(Rating.class))).thenReturn(existing);

        ratingService.update(1, form);

        assertThat(existing.getMoodysRating()).isEqualTo("Moodys Rating");
        assertThat(existing.getSandPRating()).isEqualTo("Sand PRating");
        assertThat(existing.getFitchRating()).isEqualTo("Fitch Rating");
        assertThat(existing.getOrderNumber()).isEqualTo(10);
        verify(ratingRepository, times(1)).save(existing);
    }

    @Test
    void update_whenIdNotFound_shouldThrowException() {
        when(ratingRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.update(99, new Rating()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- delete ---

    @Test
    void delete_whenIdExists_shouldCallRepositoryDelete() {
        Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        ratingService.delete(1);

        verify(ratingRepository, times(1)).delete(rating);
    }

    @Test
    void delete_whenIdNotFound_shouldThrowException() {
        when(ratingRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

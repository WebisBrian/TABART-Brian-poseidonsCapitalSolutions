package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
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
class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurvePointService curvePointService;

    // --- findAll ---

    @Test
    void findAll_whenRepositoryReturnsItems_shouldReturnList() {
        CurvePoint cp = new CurvePoint(10, 10.0, 30.0);
        when(curvePointRepository.findAll()).thenReturn(List.of(cp));

        List<CurvePoint> result = curvePointService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTerm()).isEqualTo(10.0);
    }

    @Test
    void findAll_whenRepositoryReturnsEmpty_shouldReturnEmptyList() {
        when(curvePointRepository.findAll()).thenReturn(List.of());

        List<CurvePoint> result = curvePointService.findAll();

        assertThat(result).isEmpty();
    }

    // --- save ---

    @Test
    void save_whenValidCurvePoint_shouldPersistAndReturn() {
        CurvePoint cp = new CurvePoint(10, 10.0, 30.0);
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(cp);

        CurvePoint result = curvePointService.save(cp);

        assertThat(result).isNotNull();
        assertThat(result.getCurveId()).isEqualTo(10);
        verify(curvePointRepository, times(1)).save(cp);
    }

    // --- findById ---

    @Test
    void findById_whenIdExists_shouldReturnCurvePoint() {
        CurvePoint cp = new CurvePoint(10, 10.0, 30.0);
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(cp));

        CurvePoint result = curvePointService.findById(1);

        assertThat(result.getCurveId()).isEqualTo(10);
    }

    @Test
    void findById_whenIdNotFound_shouldThrowException() {
        when(curvePointRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> curvePointService.findById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- delete ---

    @Test
    void delete_whenIdExists_shouldCallRepositoryDelete() {
        CurvePoint cp = new CurvePoint(10, 10.0, 30.0);
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(cp));

        curvePointService.delete(1);

        verify(curvePointRepository, times(1)).delete(cp);
    }

    @Test
    void delete_whenIdNotFound_shouldThrowException() {
        when(curvePointRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> curvePointService.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

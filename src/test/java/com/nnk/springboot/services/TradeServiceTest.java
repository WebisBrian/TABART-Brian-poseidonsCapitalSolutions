package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
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
class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    // --- findAll ---

    @Test
    void findAll_whenRepositoryReturnsItems_shouldReturnList() {
        Trade trade = new Trade("Account", "Type", 10.0);
        when(tradeRepository.findAll()).thenReturn(List.of(trade));

        List<Trade> result = tradeService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getAccount()).isEqualTo("Account");
    }

    @Test
    void findAll_whenRepositoryReturnsEmpty_shouldReturnEmptyList() {
        when(tradeRepository.findAll()).thenReturn(List.of());

        List<Trade> result = tradeService.findAll();

        assertThat(result).isEmpty();
    }

    // --- save ---

    @Test
    void save_whenValidTrade_shouldPersistAndReturn() {
        Trade trade = new Trade("Account", "Type", 10.0);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        Trade result = tradeService.save(trade);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isEqualTo("Account");
        verify(tradeRepository, times(1)).save(trade);
    }

    // --- findById ---

    @Test
    void findById_whenIdExists_shouldReturnTrade() {
        Trade trade = new Trade("Account", "Type", 10.0);
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        Trade result = tradeService.findById(1);

        assertThat(result.getAccount()).isEqualTo("Account");
    }

    @Test
    void findById_whenIdNotFound_shouldThrowException() {
        when(tradeRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tradeService.findById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- delete ---

    @Test
    void delete_whenIdExists_shouldCallRepositoryDelete() {
        Trade trade = new Trade("Account", "Type", 10.0);
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        tradeService.delete(1);

        verify(tradeRepository, times(1)).delete(trade);
    }

    @Test
    void delete_whenIdNotFound_shouldThrowException() {
        when(tradeRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tradeService.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

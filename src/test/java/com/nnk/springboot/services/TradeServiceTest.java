package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    void findAll_whenRepositoryReturnsItems_shouldReturnPagedResult() {
        Trade trade = new Trade("Account", "Type", 10.0);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "tradeId"));
        when(tradeRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(trade)));

        Page<Trade> result = tradeService.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getAccount()).isEqualTo("Account");
    }

    @Test
    void findAll_whenRepositoryReturnsEmpty_shouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "tradeId"));
        when(tradeRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

        Page<Trade> result = tradeService.findAll(pageable);

        assertThat(result.getContent()).isEmpty();
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

    // --- update ---

    @Test
    void update_whenIdExists_shouldUpdateFields() {
        Trade existing = new Trade("OldAccount", "OldType", 5.0);
        Trade form = new Trade("NewAccount", "NewType", 20.0);
        when(tradeRepository.findById(1)).thenReturn(Optional.of(existing));

        tradeService.update(1, form);

        assertThat(existing.getAccount()).isEqualTo("NewAccount");
        assertThat(existing.getType()).isEqualTo("NewType");
        assertThat(existing.getBuyQuantity()).isEqualTo(20.0);
        verify(tradeRepository, times(0)).save(any());
    }

    @Test
    void update_whenIdNotFound_shouldThrowException() {
        when(tradeRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tradeService.update(99, new Trade()))
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

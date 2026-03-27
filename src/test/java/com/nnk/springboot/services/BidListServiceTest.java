package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
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
class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListService bidListService;

    // --- findAll ---

    @Test
    void findAll_whenRepositoryReturnsItems_shouldReturnPagedResult() {
        BidList bid = new BidList("Account", "Type", 10.0);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "bidListId"));
        when(bidListRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(bid)));

        Page<BidList> result = bidListService.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getAccount()).isEqualTo("Account");
    }

    @Test
    void findAll_whenRepositoryReturnsEmpty_shouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "bidListId"));
        when(bidListRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

        Page<BidList> result = bidListService.findAll(pageable);

        assertThat(result.getContent()).isEmpty();
    }

    // --- save ---

    @Test
    void save_whenValidBid_shouldPersistAndReturn() {
        BidList bid = new BidList("Account", "Type", 10.0);
        when(bidListRepository.save(any(BidList.class))).thenReturn(bid);

        BidList result = bidListService.save(bid);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isEqualTo("Account");
        verify(bidListRepository, times(1)).save(bid);
    }

    // --- findById ---

    @Test
    void findById_whenIdExists_shouldReturnBidList() {
        BidList bid = new BidList("Account", "Type", 10.0);
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid));

        BidList result = bidListService.findById(1);

        assertThat(result.getAccount()).isEqualTo("Account");
    }

    @Test
    void findById_whenIdNotFound_shouldThrowException() {
        when(bidListRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bidListService.findById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- update ---

    @Test
    void update_whenIdExists_shouldUpdateFields() {
        BidList existing = new BidList("OldAccount", "OldType", 5.0);
        BidList form = new BidList("NewAccount", "NewType", 20.0);
        when(bidListRepository.findById(1)).thenReturn(Optional.of(existing));

        bidListService.update(1, form);

        assertThat(existing.getAccount()).isEqualTo("NewAccount");
        assertThat(existing.getType()).isEqualTo("NewType");
        assertThat(existing.getBidQuantity()).isEqualTo(20.0);
        verify(bidListRepository, times(0)).save(any());
    }

    @Test
    void update_whenIdNotFound_shouldThrowException() {
        when(bidListRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bidListService.update(99, new BidList()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- delete ---

    @Test
    void delete_whenIdExists_shouldCallRepositoryDelete() {
        BidList bid = new BidList("Account", "Type", 10.0);
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid));

        bidListService.delete(1);

        verify(bidListRepository, times(1)).delete(bid);
    }

    @Test
    void delete_whenIdNotFound_shouldThrowException() {
        when(bidListRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bidListService.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
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
class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameService ruleNameService;

    // --- findAll ---

    @Test
    void findAll_whenRepositoryReturnsItems_shouldReturnList() {
        RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
        when(ruleNameRepository.findAll()).thenReturn(List.of(rule));

        List<RuleName> result = ruleNameService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Rule Name");
    }

    @Test
    void findAll_whenRepositoryReturnsEmpty_shouldReturnEmptyList() {
        when(ruleNameRepository.findAll()).thenReturn(List.of());

        List<RuleName> result = ruleNameService.findAll();

        assertThat(result).isEmpty();
    }

    // --- save ---

    @Test
    void save_whenValidRuleName_shouldPersistAndReturn() {
        RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(rule);

        RuleName result = ruleNameService.save(rule);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Rule Name");
        verify(ruleNameRepository, times(1)).save(rule);
    }

    // --- findById ---

    @Test
    void findById_whenIdExists_shouldReturnRuleName() {
        RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(rule));

        RuleName result = ruleNameService.findById(1);

        assertThat(result.getName()).isEqualTo("Rule Name");
    }

    @Test
    void findById_whenIdNotFound_shouldThrowException() {
        when(ruleNameRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ruleNameService.findById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- update ---

    @Test
    void update_whenIdExists_shouldUpdateFields() {
        RuleName existing = new RuleName("OldName", "OldDesc", "", "", "", "");
        RuleName form = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(existing));
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(existing);

        ruleNameService.update(1, form);

        assertThat(existing.getName()).isEqualTo("Rule Name");
        assertThat(existing.getDescription()).isEqualTo("Description");
        assertThat(existing.getJson()).isEqualTo("Json");
        assertThat(existing.getTemplate()).isEqualTo("Template");
        assertThat(existing.getSqlStr()).isEqualTo("SQL");
        assertThat(existing.getSqlPart()).isEqualTo("SQL Part");
        verify(ruleNameRepository, times(1)).save(existing);
    }

    @Test
    void update_whenIdNotFound_shouldThrowException() {
        when(ruleNameRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ruleNameService.update(99, new RuleName()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- delete ---

    @Test
    void delete_whenIdExists_shouldCallRepositoryDelete() {
        RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(rule));

        ruleNameService.delete(1);

        verify(ruleNameRepository, times(1)).delete(rule);
    }

    @Test
    void delete_whenIdNotFound_shouldThrowException() {
        when(ruleNameRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ruleNameService.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

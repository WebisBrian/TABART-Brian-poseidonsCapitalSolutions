package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserForm;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // --- findAll ---

    @Test
    void findAll_whenRepositoryReturnsItems_shouldReturnList() {
        User user = new User("john", "Password1!", "John Doe", "USER");
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getUsername()).isEqualTo("john");
    }

    @Test
    void findAll_whenRepositoryReturnsEmpty_shouldReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = userService.findAll();

        assertThat(result).isEmpty();
    }

    // --- save ---

    @Test
    void save_whenValidUser_shouldEncodePasswordAndPersist() {
        UserForm form = new UserForm(null, "john", "Password1!", "John Doe", "USER");
        when(passwordEncoder.encode("Password1!")).thenReturn("$2a$hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.save(form);

        verify(passwordEncoder, times(1)).encode("Password1!");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void save_shouldStoreEncodedPasswordNotPlaintext() {
        UserForm form = new UserForm(null, "john", "Password1!", "John Doe", "USER");
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.save(form);

        assertThat(result.getPassword()).isEqualTo("$2a$hashed");
    }

    // --- findById ---

    @Test
    void findById_whenIdExists_shouldReturnUser() {
        User user = new User("john", "Password1!", "John Doe", "USER");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.findById(1);

        assertThat(result.getUsername()).isEqualTo("john");
    }

    @Test
    void findById_whenIdNotFound_shouldThrowException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- update ---

    @Test
    void update_whenIdExists_shouldUpdateFieldsAndEncodePassword() {
        User existing = new User("old", "OldPassword1!", "Old Name", "USER");
        UserForm form = new UserForm(null, "john", "NewPassword1!", "John Doe", "ADMIN");
        when(userRepository.findById(1)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("NewPassword1!")).thenReturn("$2a$hashed");
        when(userRepository.save(any(User.class))).thenReturn(existing);

        userService.update(1, form);

        assertThat(existing.getUsername()).isEqualTo("john");
        assertThat(existing.getFullname()).isEqualTo("John Doe");
        assertThat(existing.getRole()).isEqualTo("ADMIN");
        assertThat(existing.getPassword()).isEqualTo("$2a$hashed");
        verify(userRepository, times(1)).save(existing);
    }

    @Test
    void update_whenIdNotFound_shouldThrowException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(99, new UserForm()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- delete ---

    @Test
    void delete_whenIdExists_shouldCallRepositoryDelete() {
        User user = new User("john", "Password1!", "John Doe", "USER");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.delete(1);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void delete_whenIdNotFound_shouldThrowException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

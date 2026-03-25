package com.nnk.springboot.services;

import com.nnk.springboot.config.CustomUserDetailsService;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_whenUserExists_shouldReturnUserDetails() {
        User user = new User("john", "$2a$hashed", "John Doe", "USER");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("john");

        assertThat(result.getUsername()).isEqualTo("john");
        assertThat(result.getPassword()).isEqualTo("$2a$hashed");
        assertThat(result.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");
    }

    @Test
    void loadUserByUsername_whenAdminUser_shouldReturnAdminAuthority() {
        User user = new User("admin", "$2a$hashed", "Admin User", "ADMIN");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("admin");

        assertThat(result.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void loadUserByUsername_whenUserNotFound_shouldThrowUsernameNotFoundException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("unknown");
    }
}

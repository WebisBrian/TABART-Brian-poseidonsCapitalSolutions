package com.nnk.springboot.config;

import com.nnk.springboot.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur par son nom d'utilisateur pour l'authentification Spring Security.
     * Le rôle stocké en base (ex: "USER") est préfixé en "ROLE_USER" pour respecter
     * la convention Spring Security.
     *
     * @param username le nom d'utilisateur à rechercher
     * @return un objet UserDetails contenant les informations d'authentification
     * @throws UsernameNotFoundException si aucun utilisateur n'est trouvé avec ce nom
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.nnk.springboot.domain.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Tentative de connexion échouée — utilisateur introuvable : {}", username);
                    return new UsernameNotFoundException("Utilisateur introuvable : " + username);
                });

        log.info("Authentification réussie pour l'utilisateur : {}", username);
        return new User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}

package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserForm;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retourne une page d'utilisateurs triés par id décroissant.
     *
     * @param pageable paramètres de pagination et de tri
     * @return page d'utilisateurs
     */
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Crée un utilisateur depuis le formulaire.
     * Encode le mot de passe en clair avant persistance.
     *
     * @param form le DTO de formulaire (mot de passe en clair)
     * @return l'utilisateur persisté
     */
    @Transactional
    public User save(UserForm form) {
        User user = new User(form.getUsername(), passwordEncoder.encode(form.getPassword()), form.getFullname(), form.getRole());
        User saved = userRepository.save(user);
        log.info("Utilisateur créé — id: {}, username: {}, role: {}", saved.getId(), saved.getUsername(), saved.getRole());
        return saved;
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return l'utilisateur trouvé
     * @throws ResourceNotFoundException si aucun utilisateur n'est trouvé pour cet id
     */
    @Transactional(readOnly = true)
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Utilisateur introuvable — id: {}", id);
                    return new ResourceNotFoundException("User not found for id: " + id);
                });
    }

    /**
     * Met à jour les informations d'un utilisateur existant depuis le formulaire.
     * Le mot de passe fourni en clair est encodé avant persistance.
     *
     * @param id   l'identifiant de l'utilisateur à mettre à jour
     * @param form le DTO de formulaire contenant les nouvelles valeurs
     * @return l'utilisateur mis à jour et persisté
     * @throws ResourceNotFoundException si aucun utilisateur n'est trouvé pour cet id
     */
    @Transactional
    public User update(Integer id, UserForm form) {
        User existing = findById(id);
        existing.setUsername(form.getUsername());
        existing.setFullname(form.getFullname());
        existing.setRole(form.getRole());
        existing.setPassword(passwordEncoder.encode(form.getPassword()));
        log.info("Utilisateur mis à jour — id: {}, username: {}, role: {}", id, existing.getUsername(), existing.getRole());
        return userRepository.save(existing);
    }

    /**
     * Supprime un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à supprimer
     * @throws ResourceNotFoundException si aucun utilisateur n'est trouvé pour cet id
     */
    @Transactional
    public void delete(Integer id) {
        User user = findById(id);
        userRepository.delete(user);
        log.info("Utilisateur supprimé — id: {}, username: {}", id, user.getUsername());
    }
}
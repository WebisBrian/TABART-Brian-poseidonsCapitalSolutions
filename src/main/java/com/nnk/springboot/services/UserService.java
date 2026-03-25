package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retourne la liste de tous les utilisateurs.
     *
     * @return liste de tous les utilisateurs
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Encode le mot de passe en clair puis persiste l'utilisateur.
     * À appeler avec le mot de passe en clair — le hachage BCrypt est appliqué ici.
     *
     * @param user l'utilisateur à sauvegarder (mot de passe en clair)
     * @return l'utilisateur persisté
     */
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + id));
    }

    /**
     * Met à jour les informations d'un utilisateur existant.
     * Le mot de passe fourni en clair est encodé avant persistance.
     *
     * @param id   l'identifiant de l'utilisateur à mettre à jour
     * @param form les nouvelles valeurs à appliquer
     * @return l'utilisateur mis à jour et persisté
     * @throws ResourceNotFoundException si aucun utilisateur n'est trouvé pour cet id
     */
    @Transactional
    public User update(Integer id, User form) {
        User existing = findById(id);
        existing.setUsername(form.getUsername());
        existing.setFullname(form.getFullname());
        existing.setRole(form.getRole());
        existing.setPassword(form.getPassword());
        return save(existing);
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
    }
}

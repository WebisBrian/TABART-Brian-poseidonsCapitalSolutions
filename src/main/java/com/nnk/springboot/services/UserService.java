package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return l'utilisateur trouvé
     * @throws IllegalArgumentException si aucun utilisateur n'est trouvé pour cet id
     */
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found for id: " + id));
    }

    /**
     * Supprime un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à supprimer
     * @throws IllegalArgumentException si aucun utilisateur n'est trouvé pour cet id
     */
    public void delete(Integer id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}

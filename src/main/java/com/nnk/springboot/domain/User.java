package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "username", "fullname", "role"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 125)
    @Column(nullable = false, unique = true, length = 125)
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Column(nullable = false, length = 125)
    private String password;

    @NotBlank(message = "Full name is mandatory")
    @Size(max = 125)
    @Column(nullable = false, length = 125)
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Size(max = 125)
    @Column(nullable = false, length = 125)
    private String role;

    public User(String username, String password, String fullname, String role) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.role = role;
    }
}

package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    private Integer id;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 125)
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$",
            message = "Password must be at least 8 characters and include an uppercase letter, a digit, and a special character"
    )
    private String password;

    @NotBlank(message = "Full name is mandatory")
    @Size(max = 125)
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Size(max = 125)
    private String role;
}
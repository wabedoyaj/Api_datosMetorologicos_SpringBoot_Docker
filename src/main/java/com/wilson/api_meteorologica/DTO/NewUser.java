package com.wilson.api_meteorologica.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class NewUser {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private Set<String> roles = new HashSet<>();

    public NewUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

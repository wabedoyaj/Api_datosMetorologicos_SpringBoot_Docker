package com.wilson.api_meteorologica.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ErrorResponseDTO {
    private static final String DEFAULT_MESSAGE = "⚠️ Has excedido el límite de 100 solicitudes por hora.";
    private String message;

    public ErrorResponseDTO() {
        this.message = DEFAULT_MESSAGE;
    }
}

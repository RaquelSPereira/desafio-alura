package br.com.alura.ProjetoAlura.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuccessResponseDTO <T> {
    private String message;
    private T body;
}

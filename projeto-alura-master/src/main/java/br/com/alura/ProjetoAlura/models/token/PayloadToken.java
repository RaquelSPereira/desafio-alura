package br.com.alura.ProjetoAlura.models.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayloadToken {
    private String email;
    private String role;
}

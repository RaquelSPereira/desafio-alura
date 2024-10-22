package br.com.alura.ProjetoAlura.dtos.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecoveryJwtTokenDto {
    private String token;
}

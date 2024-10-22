package br.com.alura.ProjetoAlura.services.auth;

import br.com.alura.ProjetoAlura.models.token.PayloadToken;
import br.com.alura.ProjetoAlura.services.userDetails.UserDetailsImpl;

public interface AuthService {

    String generateToken(UserDetailsImpl user);

    PayloadToken getPayloadFromToken(String token);

}

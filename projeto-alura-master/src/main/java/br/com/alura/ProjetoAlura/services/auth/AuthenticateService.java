package br.com.alura.ProjetoAlura.services.auth;

import br.com.alura.ProjetoAlura.adapters.auth.JwtTokenAdapter;
import br.com.alura.ProjetoAlura.dtos.login.LoginUserDto;
import br.com.alura.ProjetoAlura.dtos.token.RecoveryJwtTokenDto;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.services.user.UserService;
import br.com.alura.ProjetoAlura.services.userDetails.UserDetailsImpl;
import br.com.alura.ProjetoAlura.util.encrypts.EncryptUtil;
import br.com.alura.ProjetoAlura.util.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        User user = userService.findByEmailAndPassword(loginUserDto.getEmail(),
                EncryptUtil.toMD5(loginUserDto.getPassword())).orElseThrow(() -> new UnauthorizedException("Usuário não encontrado."));
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        return new RecoveryJwtTokenDto(authService.generateToken(userDetails));
    }
}

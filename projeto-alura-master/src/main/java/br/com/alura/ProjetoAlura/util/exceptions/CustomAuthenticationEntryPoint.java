package br.com.alura.ProjetoAlura.util.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        String errorMsg = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        String message = "Esta requisição exige autenticação. Por favor, passe um token válido.";
        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;

        Map<String, Object> body = get(errorMsg, message, statusCode);

        String jsonBody = objectMapper.writeValueAsString(body);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(jsonBody);
    }

    private Map<String, Object> get(String error, String message,
                                          int status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", new Date());
        errorResponse.put("status", status);
        errorResponse.put("message", message);
        errorResponse.put("error", error);
        return errorResponse;
    }
}

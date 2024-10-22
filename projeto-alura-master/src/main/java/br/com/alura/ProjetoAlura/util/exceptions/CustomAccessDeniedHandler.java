package br.com.alura.ProjetoAlura.util.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, IOException {
        String errorMsg = HttpStatus.FORBIDDEN.getReasonPhrase();
        String message = "Você não tem permissão para acessar este recurso.";
        int statusCode = HttpServletResponse.SC_FORBIDDEN;

        Map<String, Object> body = this.get(errorMsg, message, statusCode);

        String jsonBody = objectMapper.writeValueAsString(body);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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

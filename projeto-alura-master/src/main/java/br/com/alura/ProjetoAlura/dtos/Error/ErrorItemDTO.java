package br.com.alura.ProjetoAlura.dtos.Error;

import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.validation.FieldError;

@Data
public class ErrorItemDTO {

    private final String field;
    private final String message;

    public ErrorItemDTO(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }

    public ErrorItemDTO(String field, String message) {
        Assert.notNull(field, "Field description must not be null");
        Assert.isTrue(!field.isEmpty(), "Field description must not be empty");

        Assert.notNull(message, "Message description must not be null");
        Assert.isTrue(!message.isEmpty(), "Message description must not be empty");

        this.field = field;
        this.message = message;
    }


}
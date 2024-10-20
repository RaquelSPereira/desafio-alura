package br.com.alura.ProjetoAlura.dtos.user;

import br.com.alura.ProjetoAlura.entities.User;
import br.com.alura.ProjetoAlura.enums.role.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserListItemDTO implements Serializable {

    private String name;
    private String email;
    private RoleEnum role;

    public UserListItemDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}

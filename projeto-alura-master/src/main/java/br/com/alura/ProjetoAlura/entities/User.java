package br.com.alura.ProjetoAlura.entities;

import br.com.alura.ProjetoAlura.enums.role.RoleEnum;
import br.com.alura.ProjetoAlura.util.EncryptUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt = LocalDateTime.now();
    private String name;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    private String email;
    private String password;

    public User(String name, String email, RoleEnum role, String password) {
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = EncryptUtil.toMD5(password);
    }
}

package br.com.alura.ProjetoAlura.repository.user;


import br.com.alura.ProjetoAlura.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String Email);
}

package br.com.alura.ProjetoAlura.repository.user;


import br.com.alura.ProjetoAlura.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String Email);
}

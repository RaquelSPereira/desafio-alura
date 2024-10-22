package br.com.alura.ProjetoAlura.repositories.user;


import br.com.alura.ProjetoAlura.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> save (User user);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String Email);

    Optional<User> findByEmailAndPassword(String email, String password);

    List<User> findAll();
}

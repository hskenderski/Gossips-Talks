package bg.codeacademy.spring.gossiptalks.repositories;

import bg.codeacademy.spring.gossiptalks.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query(value = "SELECT * FROM users as u WHERE u.username=?1", nativeQuery = true)
  User findUserByUsername(String username);

  @Query(value = "SELECT * FROM users as u WHERE u.email=?1", nativeQuery = true)
  User findUserByEmail(String email);


}

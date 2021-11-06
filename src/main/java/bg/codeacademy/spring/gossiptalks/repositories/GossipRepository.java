package bg.codeacademy.spring.gossiptalks.repositories;

import bg.codeacademy.spring.gossiptalks.entities.Gossips;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GossipRepository extends JpaRepository<Gossips, Long> {

  @Query(value = "SELECT g FROM Gossips  g WHERE g.username=?1")
  Page<Gossips> findAllGossipsByUsername(Pageable pageable, String username);

  @Query(value = "SELECT g.gossip_id,g.text,g.username,g.date_post from gossips as g\n"
      + "JOIN users_gossips ug on g.gossip_id = ug.gossips_gossip_id\n"
      + "JOIN users u on u.user_id = ug.user_user_id\n"
      + "JOIN user_followed uf on u.user_id = uf.followed_id\n"
      + "where u.email != ?1 AND uf.user_id = ?2 ORDER BY g.date_post desc LIMIT ?3 , ?4"
      , nativeQuery = true
  )
  List<Gossips> getHistory(String email, Long id, Integer start, Integer end);

  @Query(value = "SELECT g.gossip_id,g.text,g.username,g.date_post from gossips as g\n"
      + "      where g.username = ?1 ORDER BY g.date_post desc LIMIT ?2 , ?3", nativeQuery = true)
  List<Gossips> getUserGossips(String username, Integer start, Integer end);
}

package bg.codeacademy.spring.gossiptalks.entities;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gossips")
public class Gossips {

  private Long id;
  private String text;
  private String username;
  private LocalDateTime datePost;

  public Gossips(String text, String username) {
    this.text = text;
    this.username = username;
    this.datePost = LocalDateTime.now();
  }

  public Gossips() {
  }

  @Id
  @Column(name = "gossip_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  @Column(name = "text")
  public String getText() {
    return text;
  }

  @Column(name = "username")
  public String getUsername() {
    return username;
  }

  @Column(name = "date_post")
  public LocalDateTime getDatePost() {
    return datePost;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setDatePost(LocalDateTime datePost) {
    this.datePost = datePost;
  }
}

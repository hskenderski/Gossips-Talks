package bg.codeacademy.spring.gossiptalks.dto;

public class GossipDto {

  private String text;
  private String id;
  private String username;
  private String date;

  public String getText() {
    return text;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getDate() {
    return date;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setDate(String date) {
    this.date = date;
  }
}

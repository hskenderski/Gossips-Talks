package bg.codeacademy.spring.gossiptalks.dto;

public class UserResponse {
  private String email;
  private String username;
  private String fullName;

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }

  public String getFullName() {
    return fullName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }


}

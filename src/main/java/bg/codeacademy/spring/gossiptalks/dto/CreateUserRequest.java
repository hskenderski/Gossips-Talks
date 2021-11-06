package bg.codeacademy.spring.gossiptalks.dto;


public class CreateUserRequest {

  private String email;
  private String username;
  private String name;
  private String password;
  private String passwordConfirmation;

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public String getPasswordConfirmation() {
    return passwordConfirmation;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setPasswordConfirmation(String passwordConfirmation) {
    this.passwordConfirmation = passwordConfirmation;
  }
}

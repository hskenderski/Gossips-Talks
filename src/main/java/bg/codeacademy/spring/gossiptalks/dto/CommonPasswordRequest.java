package bg.codeacademy.spring.gossiptalks.dto;

public class CommonPasswordRequest {

  private String password;
  private String passwordConfirmation;

  public String getPassword() {
    return password;
  }

  public String getPasswordConfirmation() {
    return passwordConfirmation;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setPasswordConfirmation(String passwordConfirmation) {
    this.passwordConfirmation = passwordConfirmation;
  }
}

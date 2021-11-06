package bg.codeacademy.spring.gossiptalks.dto;

public class ChangePasswordRequest {

  private String password;
  private String passwordConfirmation;
  private String oldPassword;

  public String getPassword() {
    return password;
  }

  public String getPasswordConfirmation() {
    return passwordConfirmation;
  }

  public String getOldPassword() {
    return oldPassword;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setPasswordConfirmation(String passwordConfirmation) {
    this.passwordConfirmation = passwordConfirmation;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }
}

package bg.codeacademy.spring.gossiptalks.dto;

public class CreateGossipRequest {

  private String text;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}

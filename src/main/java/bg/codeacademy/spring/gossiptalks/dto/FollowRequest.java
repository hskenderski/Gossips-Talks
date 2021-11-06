package bg.codeacademy.spring.gossiptalks.dto;

public class FollowRequest {

  private boolean follow;

  public boolean isFollow() {
    return follow;
  }

  public void setFollow(boolean follow) {
    this.follow = follow;
  }
}

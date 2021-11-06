package bg.codeacademy.spring.gossiptalks.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "users")
public class User implements GrantedAuthority {

  private Long userId;
  private String email;
  private String username;
  private String fullName;
  private String password;

  //The Users that current User followed
  private List<User> followed;

  //The followers of current user
  private List<User> followers;

  //This two collections is for the self connections
  private List<User> followedUsers;
  List<User> followerUsers;

  //The gossips of the user
  private List<Gossips> gossips;

  //One user have one default role USER
  private RoleName roleName;

  public User() {
    this.followed = new ArrayList<>();
    this.followers = new ArrayList<>();
    this.followedUsers = new ArrayList<>();
    this.followerUsers = new ArrayList<>();
    this.gossips = new ArrayList<>();
    this.roleName = RoleName.USER;
  }

  public User(String email, String username, String fullName, String password) {
    setEmail(email);
    setUsername(username);
    setFullName(fullName);
    setPassword(password);
    this.followed = new ArrayList<>();
    this.followers = new ArrayList<>();
    this.followedUsers = new ArrayList<>();
    this.followerUsers = new ArrayList<>();
    this.gossips = new ArrayList<>();
    this.roleName = RoleName.USER;
  }

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getUserId() {
    return userId;
  }

  @Column(unique = true, nullable = false, name = "email")
  public String getEmail() {
    return email;
  }

  @Column(unique = true, nullable = false, name = "username")
  public String getUsername() {
    return username;
  }

  @Column(name = "full_name")
  public String getFullName() {
    return fullName;
  }

  @Column(name = "password")
  public String getPassword() {
    return password;
  }

  @ManyToMany
  @JoinTable(name = "user_followers", joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "follower_id")})
  public List<User> getFollowers() {
    return followers;
  }

  @ManyToMany
  @JoinTable(name = "user_followed", joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "followed_id")})
  public List<User> getFollowed() {
    return followed;
  }

  @ManyToMany(mappedBy = "followed", fetch = FetchType.EAGER)
  public List<User> getFollowedUsers() {
    return followedUsers;
  }

  @ManyToMany(mappedBy = "followers", fetch = FetchType.EAGER)
  public List<User> getFollowerUsers() {
    return followerUsers;
  }

  @OneToMany(
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  public List<Gossips> getGossips() {
    return gossips;
  }

  public RoleName getRoleName() {
    return roleName;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
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

  public void setPassword(String password) {
    this.password = password;
  }

  public void setFollowers(List<User> followers) {
    this.followers = followers;
  }

  public void setFollowed(List<User> followed) {
    this.followed = followed;
  }

  public void setFollowedUsers(List<User> followedUser) {
    this.followedUsers = followedUser;
  }

  public void setFollowerUsers(List<User> followerUsers) {
    this.followerUsers = followerUsers;
  }

  public void setGossips(List<Gossips> gossips) {
    this.gossips = gossips;
  }

  public void setRoleName(RoleName roleName) {
    this.roleName = roleName;
  }


  public enum RoleName {
    USER
  }

  @Transient
  @Override
  public String getAuthority() {
    return "ROLE_" + roleName.toString();
  }

  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("MS_SHOULD_BE_FINAL")
  public static Comparator<User> userComparator = new Comparator<User>() {
    @Override
    public int compare(User o1, User o2) {
      if (o1.getGossips().size() == o2.getGossips().size()) {
        return o1.getUsername().compareTo(o2.getUsername());
      }
      return (o1.getGossips().size() < o2.getGossips().size()) ? 1 : -1;
    }
  };

  //This two is for bug in the SpotBugs
  private void writeObject(ObjectOutputStream stream)
      throws IOException {
    stream.defaultWriteObject();
  }

  private void readObject(ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
  }
}


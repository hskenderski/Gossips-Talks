package bg.codeacademy.spring.gossiptalks.services;

import bg.codeacademy.spring.gossiptalks.entities.User;
import bg.codeacademy.spring.gossiptalks.exceptions.InvalidException;
import bg.codeacademy.spring.gossiptalks.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserServiceImp implements UserService {

  private final ModelMapper modelMapper;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImp(UserRepository userRepository) {
    this.userRepository = userRepository;
    this.modelMapper = new ModelMapper();
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  @Override
  public User findUserByUsername(String username) {
    return userRepository.findUserByUsername(username);
  }

  @Override
  public User findUserByEmail(String email) {
    return userRepository.findUserByEmail(email);
  }

  @Override
  public ModelMapper getModelMapper() {
    return modelMapper;
  }

  @Override
  public User register(String email, String username, String fullName, String password,
      String repeatPassword) {
    emailAndUsernameValidation(email, username);
    passwordValidation(password, repeatPassword);
    String encodedPass = passwordEncoder.encode(password);
    return this.userRepository.save(new User(email, username, fullName, encodedPass));
  }

  @Override
  public User changePassword(String email, String password, String repeatPassword,
      String oldPassword) {
    User user = findUserByEmail(email);
    passwordValidation(password, repeatPassword);
    if (password.equals(oldPassword)) {
      throw new InvalidException("Enter different password than your current one!");
    }
    String encodedPass = passwordEncoder.encode(password);
    user.setPassword(encodedPass);
    return this.userRepository.save(user);
  }

  @Override
  public User followUser(String username, String otherUserUsername) {
    User user = findUserByEmail(username);
    if (this.userRepository.findUserByUsername(otherUserUsername) == null) {
      throw new InvalidException(
          String.format("There is not user with username: %s", otherUserUsername));
    }
    if (user.getUsername().equals(otherUserUsername)) {
      throw new InvalidException("You cannot follow yourself!");
    }
    User userToFollow = findUserByUsername(otherUserUsername);
    if (user.getFollowed().contains(userToFollow)) {
      return userToFollow;
    }
    userToFollow.getFollowers().add(user);
    user.getFollowed().add(userToFollow);
    this.userRepository.save(user);
    return this.userRepository.save(userToFollow);
  }

  @Override
  public User unfollowUser(String username, String otherUserUsername) {
    User user = findUserByEmail(username);
    if (user.getUsername().equals(otherUserUsername)) {
      throw new InvalidException("You cannot follow yourself!");
    }
    User userToFollow = findUserByUsername(otherUserUsername);
    if (user.getFollowed().contains(userToFollow)) {
      user.getFollowed().remove(userToFollow);
      userToFollow.getFollowers().remove(user);
    } else {
      return userToFollow;
    }
    this.userRepository.save(user);
    return this.userRepository.save(userToFollow);
  }

  @Override
  public UserRepository getUserRepository() {
    return userRepository;
  }

  private void passwordValidation(String password, String repeatPassword) {
    if (!(password.equals(repeatPassword))) {
      throw new InvalidException("Password don't match!");
    }
    boolean containsUpperCaseLetter = false;
    boolean containsLowerCaseLetter = false;
    boolean containsSpecialSymbol = false;
    boolean containsDigit = false;
    boolean containsWhiteSpace = false;
    for (char c : password.toCharArray()) {
      if (Character.isUpperCase(c)) {
        containsUpperCaseLetter = true;
      }
      if (Character.isLowerCase(c)) {
        containsLowerCaseLetter = true;
      }
      if (!Character.isAlphabetic(c) && (!Character.isDigit(c)) && (!Character.isWhitespace(c))) {
        containsSpecialSymbol = true;
      }
      if (Character.isDigit(c)) {
        containsDigit = true;
      }
      if (Character.isWhitespace(c)) {
        containsWhiteSpace = true;
      }
    }
    if (!containsSpecialSymbol) {
      throw new InvalidException("Password should contain special symbol!");
    } else if (!containsDigit) {
      throw new InvalidException("Password should contain digit!");
    } else if (!containsUpperCaseLetter) {
      throw new InvalidException("Password should contain uppercase letter!");
    } else if (!containsLowerCaseLetter) {
      throw new InvalidException("Password should contain lowercase letter!");
    } else if (containsWhiteSpace) {
      throw new InvalidException("Password should not contain whitespaces!");
    } else if (password.length() < 7) {
      throw new InvalidException("Password should be minimum 7 symbols!");
    }
  }

  private void emailAndUsernameValidation(String email, String username) {
    if (this.userRepository.findUserByEmail(email) != null) {
      throw new InvalidException("Email already exist!");
    } else if (!(email.matches("([^-_][a-zA-Z0-9\\._]+[^-_])@([a-zA-Z0-9]*\\.[a-zA-Z0-9]+)+"))) {
      throw new InvalidException("You must enter valid email!");
    } else if (this.userRepository.findUserByUsername(username) != null) {
      throw new InvalidException("Username already exist!");
    } else if (!(username.matches("^[a-z0-9.\\-]+$"))) {
      throw new InvalidException("You must enter valid username!");
    }
  }

  @Override
  public User getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return userRepository.findUserByEmail(auth.getName());
  }

  //search in list of all users, by substring in username or full name
  @Override
  public List<User> searchUsersBySubstring(String substring, Boolean f) {
    //select right list of users
    List<User> listOfUsers;
    if (f) {
      listOfUsers = getCurrentUser().getFollowed().stream()
          .sorted(User.userComparator)
          .collect(Collectors.toList());
    } else {
      listOfUsers = this.userRepository.findAll().stream()
          .sorted(User.userComparator)
          .collect(Collectors.toList());
    }
    //select users by substring
    List<User> selectedUsers = new ArrayList<>();
    for (User user : listOfUsers) {
      if (user.getUsername().contains(substring) || user.getFullName().contains(substring)) {
        selectedUsers.add(user);
      }
    }
    if (selectedUsers.isEmpty()) {
      throw new InvalidException("There are no such users in the program");
    }
    return selectedUsers;
  }
}


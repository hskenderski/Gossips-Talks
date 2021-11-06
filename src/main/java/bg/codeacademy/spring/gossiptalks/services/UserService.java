package bg.codeacademy.spring.gossiptalks.services;

import bg.codeacademy.spring.gossiptalks.entities.User;
import bg.codeacademy.spring.gossiptalks.repositories.UserRepository;
import java.util.List;
import org.modelmapper.ModelMapper;

public interface UserService {

  User findUserByUsername(String username);

  User findUserByEmail(String email);

  ModelMapper getModelMapper();

  User register(String email, String username, String fullName, String password,
      String repeatPassword);

  User changePassword(String email,
      String password, String passwordConfirm, String oldPassword);

  User followUser(String username, String otherUserUsername);

  User unfollowUser(String username, String otherUserUsername);

  UserRepository getUserRepository();

  List<User> searchUsersBySubstring(String name, Boolean f);

  User getCurrentUser();

}
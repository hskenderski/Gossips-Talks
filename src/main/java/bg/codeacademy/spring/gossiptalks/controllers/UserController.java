package bg.codeacademy.spring.gossiptalks.controllers;

import bg.codeacademy.spring.gossiptalks.dto.ChangePasswordRequest;
import bg.codeacademy.spring.gossiptalks.dto.CreateUserRequest;
import bg.codeacademy.spring.gossiptalks.dto.FollowRequest;
import bg.codeacademy.spring.gossiptalks.dto.UserResponse;
import bg.codeacademy.spring.gossiptalks.entities.User;
import bg.codeacademy.spring.gossiptalks.services.UserService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }


  @PostMapping(path = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @ResponseStatus(value = HttpStatus.OK)
  public UserResponse register(@Valid CreateUserRequest createUserRequest) {
    User user = this.userService.register(createUserRequest.getEmail(),
        createUserRequest.getUsername(), createUserRequest.getName(),
        createUserRequest.getPassword(), createUserRequest.getPasswordConfirmation());
    return this.userService.getModelMapper().map(user, UserResponse.class);
  }


  @PreAuthorize("hasRole('USER')")
  @PostMapping(path = "/users/me", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @ResponseStatus(value = HttpStatus.OK)
  public UserResponse changePassword(@Valid ChangePasswordRequest changePasswordRequest) {
    User user = userService.changePassword(userService.getCurrentUser().getEmail(),
        changePasswordRequest.getPassword(), changePasswordRequest.getPasswordConfirmation(),
        changePasswordRequest.getOldPassword());
    return this.userService.getModelMapper().map(user, UserResponse.class);
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping(path = "/users/{username}/follow", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @ResponseStatus(value = HttpStatus.OK)
  public UserResponse followOrUnfollowUser(@Valid @PathVariable(name = "username") String username,
      FollowRequest followRequest) {
    User user;
    if (followRequest.isFollow()) {
      user = userService.followUser(userService.getCurrentUser().getEmail(), username);
    } else {
      user = userService.unfollowUser(userService.getCurrentUser().getEmail(), username);
    }
    return this.userService.getModelMapper().map(user, UserResponse.class);
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/users")
  @ResponseStatus(value = HttpStatus.OK)
  public List<UserResponse> searchUsersByString(@Valid @RequestParam String name,
      @RequestParam Boolean f) {

    List<UserResponse> userResponses = new ArrayList<>();
    return mapToListUserResponse(userService.searchUsersBySubstring(name, f), userResponses);
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/users/me")
  @ResponseStatus(value = HttpStatus.OK)
  public UserResponse currentUser() {
    return userService.getModelMapper().map(this.userService.getCurrentUser(), UserResponse.class);
  }

  public List<UserResponse> mapToListUserResponse(List<User> users,
      List<UserResponse> userResponses) {
    for (User user : users) {
      UserResponse userResponse = userService.getModelMapper()
          .map(user, UserResponse.class);
      userResponses.add(userResponse);
    }
    return userResponses;
  }
}

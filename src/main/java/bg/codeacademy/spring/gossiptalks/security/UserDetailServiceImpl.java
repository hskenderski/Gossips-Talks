package bg.codeacademy.spring.gossiptalks.security;

import bg.codeacademy.spring.gossiptalks.entities.User;
import bg.codeacademy.spring.gossiptalks.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserDetailServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = this.userRepository.findUserByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("No such user found in database!");
    }
    return org.springframework.security.core.userdetails.User.withUsername(email)
        .password(user.getPassword())
        .roles(user.getRoleName().name())
        .build();

  }
}

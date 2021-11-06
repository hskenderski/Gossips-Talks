package bg.codeacademy.spring.gossiptalks.repositories;

import bg.codeacademy.spring.gossiptalks.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import javax.transaction.Transactional;
import java.util.List;

import static org.testng.Assert.*;

@DataJpaTest
@ContextConfiguration
@Transactional
public class UserRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

  public Long TEST_USER1_ID;

  @Autowired
  UserRepository userRepository;

  @BeforeClass
  public void setUp(){
    User user = new User("ErikTest@mail.com", "Erik123","Erik Nikolov","123123");
    user = this.userRepository.save(user);
    TEST_USER1_ID = user.getUserId();
  }
  @AfterClass
  public void tearDown(){
    this.userRepository.delete(this.userRepository.findUserByEmail("ErikTest@mail.com"));
  }

  @BeforeSuite
  @Override
  protected void springTestContextPrepareTestInstance() throws Exception {
    super.springTestContextPrepareTestInstance();
  }

  @Test
  public void saveUserTest(){
    User user = new User("ErikTestt@mail.com", "Erik1234","Erik Nikolov","123123");
    assertNull(user.getUserId());
    assertTrue(user.getEmail().equals("ErikTestt@mail.com"));
    assertTrue(user.getUsername().equals("Erik1234"));
    assertTrue(user.getFullName().equals("Erik Nikolov"));
    assertTrue(user.getPassword().equals("123123"));

  }

  @Test
  public void findAll(){
    User user = userRepository.findUserByEmail("ErikTest@mail.com");
    List<User> allUsers = userRepository.findAll();
    assertTrue(allUsers.size() == 1);
  }

  @Test
  public void findUserById(){
    User user = userRepository.findUserByEmail("ErikTest@mail.com");
    assertNotNull(user.getUserId());
    assertTrue(user.getUserId() == TEST_USER1_ID);
  }

  @Test
  public void findUserByUsername(){
    User user = userRepository.findUserByEmail("ErikTest@mail.com");
    assertNotNull(user.getUsername());
    assertTrue(user.getUsername().equals(user.getUsername()));
  }

  @Test
  public void findUserByEmail(){
    User user = userRepository.findUserByEmail("ErikTest@mail.com");
    assertNotNull(user.getEmail());
    assertTrue(user.getEmail().equals(user.getEmail()));
  }

  @Test
  public void findUserByFullName(){
    User user = userRepository.findUserByEmail("ErikTest@mail.com");
    assertNotNull(user.getFullName());
    assertTrue(user.getFullName().equals(user.getFullName()));
  }

  @Test
  public void changePassword(){
    User user = userRepository.findUserByEmail("ErikTest@mail.com");
    assertNotNull(user.getPassword());
    assertTrue(user.getPassword().equals("123123"));
    user.setPassword("131313");
    assertFalse(user.getPassword().equals("123123"));
    assertTrue(user.getPassword().equals("131313"));
  }
}
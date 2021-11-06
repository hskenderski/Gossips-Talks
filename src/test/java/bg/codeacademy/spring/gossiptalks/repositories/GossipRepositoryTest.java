package bg.codeacademy.spring.gossiptalks.repositories;

import bg.codeacademy.spring.gossiptalks.entities.Gossips;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import javax.transaction.Transactional;
import static org.testng.Assert.*;

@DataJpaTest
@Transactional
@ContextConfiguration
public class GossipRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

  @Autowired
  GossipRepository gossipRepository;
  @Autowired
  UserRepository userRepository;

  @BeforeMethod
  public void setUp() {
  }

  @AfterMethod
  public void tearDown() {
    gossipRepository.deleteAll();
    userRepository.deleteAll();
  }

  @BeforeSuite
  @Override
  protected void springTestContextPrepareTestInstance() throws Exception {
    super.springTestContextPrepareTestInstance();
  }

  @Test
  public void saveGossipTest(){
    Gossips gossips = new Gossips("Test tekst", "Erik test gossip");
    assertNull(gossips.getId());
    gossips.setId(15L);
    gossipRepository.save(gossips);
    assertNotNull(gossips.getId());
    assertTrue(gossips.getId() == 15);
    assertTrue(gossips.getUsername().equals("Erik test gossip"));
    System.out.println(gossips.getId() + gossips.getUsername() + gossips.getText());
  }

  @Test
  public void findGossipById(){
    Gossips gossips = new Gossips("Test tekst", "Erik test gossip");
    assertNull(gossips.getId());
    gossips.setId(99L);
    assertTrue(gossips.getId() == 99L);
  }
}
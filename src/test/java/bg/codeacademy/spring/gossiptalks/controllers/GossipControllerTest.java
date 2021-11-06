package bg.codeacademy.spring.gossiptalks.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bg.codeacademy.spring.gossiptalks.entities.Gossips;
import bg.codeacademy.spring.gossiptalks.entities.User;
import bg.codeacademy.spring.gossiptalks.repositories.GossipRepository;
import bg.codeacademy.spring.gossiptalks.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners({WithSecurityContextTestExecutionListener.class})
public class GossipControllerTest extends AbstractTestNGSpringContextTests {

  @Autowired
  public MockMvc mockMvc;

  @Autowired
  private GossipRepository gossipRepository;

  @Autowired
  private UserRepository userRepository;

  public Long TEST_USER1_ID;
  public static final String TEST_USER1_NAMES = "Hristiyan Ivanov Skenderski";
  public static final String TEST_USER1_USERNAME = "h.skenderskii";
  public static final String TEST_USER1_EMAIL = "h.skenderskii@abv.bg";
  public static final String TEST_USER1_PASSWORD = "Hrisi1ha!";

  public Long TEST_GOSSIP1_ID, TEST_GOSSIP2_ID, TEST_GOSSIP3_ID;
  public static final String TEST_GOSSIP1_TEXT = "Hello dear public!", TEST_GOSSIP2_TEXT = "Second <message>...";
  public static final String TEST_GOSSIP1_USERNAME = "h.skenderskii", TEST_GOSSIP2_USERNAME = "i.ivanov";


  @BeforeClass
  public void setUp() {
    User user1 = new User(TEST_USER1_EMAIL, TEST_USER1_USERNAME, TEST_USER1_NAMES,
        TEST_USER1_PASSWORD);
    user1 = this.userRepository.save(user1);
    TEST_USER1_ID = user1.getUserId();

    Gossips gossips1 = new Gossips(TEST_GOSSIP1_TEXT, TEST_GOSSIP1_USERNAME);
    gossips1 = this.gossipRepository.save(gossips1);
    TEST_GOSSIP1_ID = gossips1.getId();

    Gossips gossips2 = new Gossips(TEST_GOSSIP2_TEXT, TEST_GOSSIP2_USERNAME);
    gossips2 = this.gossipRepository.save(gossips2);
    TEST_GOSSIP2_ID = gossips2.getId();
  }

  @AfterClass
  public void tearDown() {
    this.gossipRepository.delete(this.gossipRepository.getOne(TEST_GOSSIP1_ID));
    this.gossipRepository.delete(this.gossipRepository.getOne(TEST_GOSSIP2_ID));
    this.userRepository.delete(userRepository.findUserByEmail(TEST_USER1_EMAIL));
  }

  @Test
  @WithMockUser(username = TEST_USER1_EMAIL)
  public void write_gossip() throws Exception {
    mockMvc.perform(multipart("/api/v1/gossips")
            .param("text", "opa"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.text").value("opa"))
        .andExpect(jsonPath("$.username", is(TEST_USER1_USERNAME)));
  }

  @Test
  @WithMockUser(username = TEST_USER1_EMAIL)
  public void write_gossip_empty() throws Exception {
    mockMvc.perform(multipart("/api/v1/gossips")
            .param("text", ""))
        .andDo(print())
        .andExpect(status()
            .isBadRequest());
  }

  @Test
  @WithMockUser(username = TEST_USER1_EMAIL)
  public void get_user_gossips() throws Exception {
    mockMvc.perform(
            get("/api/v1/users/{username}/gossips?pageNumber=0&pageCapacity=100", TEST_USER1_USERNAME))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pageNumber").value("0"))
        .andExpect(jsonPath("$.pageSize").value("100"))
        .andExpect(jsonPath("$.count").value("1"))
        .andExpect(jsonPath("$.total").value("1"));
  }

  @Test
  @WithMockUser(username = TEST_USER1_EMAIL)
  public void get_all() throws Exception {
    mockMvc.perform(get("/api/v1/gossips?pageNumber=0&pageCapacity=100"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pageNumber").value("0"))
        .andExpect(jsonPath("$.pageSize").value("100"))
        .andExpect(jsonPath("$.count").value("0"))
        .andExpect(jsonPath("$.total").value("0"));
  }

}
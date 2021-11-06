package bg.codeacademy.spring.gossiptalks.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bg.codeacademy.spring.gossiptalks.entities.User;
import bg.codeacademy.spring.gossiptalks.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.util.NestedServletException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners({WithSecurityContextTestExecutionListener.class})
public class UserControllerTest extends AbstractTestNGSpringContextTests {

  @Autowired
  public MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  public Long TEST_USER1_ID, TEST_USER2_ID;
  public static final String TEST_USER1_NAMES = "Hristiyan Ivanov Skenderski", TEST_USER2_NAMES = "Ivan Ivanov";
  public static final String TEST_USER1_USERNAME = "h.skenderskii", TEST_USER2_USERNAME = "i.ivanov";
  public static final String TEST_USER1_EMAIL = "h.skenderskii@abv.bg", TEST_USER2_EMAIL = "i.ivanov@abv.bg";
  public static final String TEST_USER1_PASSWORD = "Hrisi1ha!", TEST_USER2_PASSWORD = "Hrisi1ha!";

  @BeforeClass
  public void setUp() {
    User user1 = new User(TEST_USER1_EMAIL, TEST_USER1_USERNAME, TEST_USER1_NAMES,
        TEST_USER1_PASSWORD);
    user1 = this.userRepository.save(user1);
    TEST_USER1_ID = user1.getUserId();

    User user2 = new User(TEST_USER2_EMAIL, TEST_USER2_USERNAME, TEST_USER2_NAMES,
        TEST_USER2_PASSWORD);
    user2 = this.userRepository.save(user2);
    TEST_USER2_ID = user2.getUserId();

    user1.getFollowed().add(user2);
    this.userRepository.save(user1);
  }

  @AfterClass
  public void tearDown() {
    this.userRepository.delete(userRepository.findUserByEmail(TEST_USER1_EMAIL));
    this.userRepository.delete(userRepository.findUserByEmail(TEST_USER2_EMAIL));
    if (this.userRepository.findUserByEmail("hristiyan@abv.bg") != null) {
      this.userRepository.delete(userRepository.findUserByEmail("hristiyan@abv.bg"));
    }
  }

  @Test
  @WithMockUser(username = TEST_USER1_EMAIL)
  public void get_logged_user() throws Exception {
    mockMvc.
        perform(get("/api/v1/users/me"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email", is(TEST_USER1_EMAIL)))
        .andExpect(jsonPath("$.username", is(TEST_USER1_USERNAME)))
        .andExpect(jsonPath("$.fullName", is(TEST_USER1_NAMES)));
  }

  @Test
  public void get_not_logged_user() throws Exception {
    mockMvc.
        perform(get("/api/v1/users/me"))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test(expectedExceptions = NestedServletException.class)
  @WithMockUser(username = "fake")
  public void get_fake_logged_user() throws Exception {
    mockMvc.
        perform(get("/api/v1/users/me"))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void register_user() throws Exception {
    mockMvc.perform(multipart("/api/v1/users")
            .param("email", "hristiyan@abv.bg")
            .param("username", "hristiyan")
            .param("name", "Hristiyan")
            .param("password", "Hrisi1ha!")
            .param("passwordConfirmation", "Hrisi1ha!"))
        .andDo(print())
        .andExpect(status()
            .isOk());
  }

  @Test
  public void register_user_with_wrong_email() throws Exception {
    mockMvc.perform(multipart("/api/v1/users")
            .param("email", "hristiyanabv.bg")
            .param("username", "hristiyan")
            .param("name", "Hristiyan")
            .param("password", "Hrisi1ha!")
            .param("passwordConfirmation", "Hrisi1ha!"))
        .andDo(print())
        .andExpect(status()
            .isBadRequest());
  }

  @Test
  public void register_user_with_wrong_username() throws Exception {
    mockMvc.perform(multipart("/api/v1/users")
            .param("email", "hristiyan@abv.bg")
            .param("username", "Hristiyan")
            .param("name", "Hristiyan")
            .param("password", "Hrisi1ha!")
            .param("passwordConfirmation", "Hrisi1ha!"))
        .andDo(print())
        .andExpect(status()
            .isBadRequest());
  }

  @Test
  public void register_user_with_different_password() throws Exception {
    mockMvc.perform(multipart("/api/v1/users")
            .param("email", "hristiyan@abv.bg")
            .param("username", "hristiyan")
            .param("name", "Hristiyan")
            .param("password", "Hrisi1ha")
            .param("passwordConfirmation", "Hrisi1ha!"))
        .andDo(print())
        .andExpect(status()
            .isBadRequest());
  }

  @Test
  public void register_user_with_wrong_password_MustHave_UpperCase() throws Exception {
    mockMvc.perform(multipart("/api/v1/users")
            .param("email", "hristiyan@abv.bg")
            .param("username", "hristiyan")
            .param("name", "Hristiyan")
            .param("password", "hrisi1ha!")
            .param("passwordConfirmation", "hrisi1ha!"))
        .andDo(print())
        .andExpect(status()
            .isBadRequest());
  }

  @Test
  public void register_user_with_wrong_password_MustHave_SpecialSymbol() throws Exception {
    mockMvc.perform(multipart("/api/v1/users")
            .param("email", "hristiyan@abv.bg")
            .param("username", "hristiyan")
            .param("name", "Hristiyan")
            .param("password", "Hrisi1ha")
            .param("passwordConfirmation", "Hrisi1ha"))
        .andDo(print())
        .andExpect(status()
            .isBadRequest());
  }

  @Test
  public void register_user_with_wrong_password_MustHave_LowCase() throws Exception {
    mockMvc.perform(multipart("/api/v1/users")
            .param("email", "hristiyan@abv.bg")
            .param("username", "hristiyan")
            .param("name", "Hristiyan")
            .param("password", "HRISI1HA!")
            .param("passwordConfirmation", "HRISI1HA!"))
        .andDo(print())
        .andExpect(status()
            .isBadRequest());
  }

  @Test
  public void register_user_with_wrong_password_MustHave_Digit() throws Exception {
    mockMvc.perform(multipart("/api/v1/users")
            .param("email", "hristiyan@abv.bg")
            .param("username", "hristiyan")
            .param("name", "Hristiyan")
            .param("password", "Hrisiha!")
            .param("passwordConfirmation", "Hrisiha!"))
        .andDo(print())
        .andExpect(status()
            .isBadRequest());
  }

  @Test
  public void register_user_with_wrong_password_MustHave_Minimum_7_Symbols() throws Exception {
    mockMvc.perform(multipart("/api/v1/users")
            .param("email", "hristiyan@abv.bg")
            .param("username", "hristiyan")
            .param("name", "Hristiyan")
            .param("password", "Hris1!")
            .param("passwordConfirmation", "Hris1!"))
        .andDo(print())
        .andExpect(status()
            .isBadRequest());
  }

  @Test
  public void register_user_with_wrong_password_MustHave_CantHave_WhiteSpace() throws Exception {
    mockMvc.perform(multipart("/api/v1/users")
            .param("email", "hristiyan@abv.bg")
            .param("username", "hristiyan")
            .param("name", "Hristiyan")
            .param("password", "H ris1!")
            .param("passwordConfirmation", "H ris1!"))
        .andDo(print())
        .andExpect(status()
            .isBadRequest());
  }

  @Test
  @WithMockUser(username = TEST_USER2_EMAIL)
  public void follow_user_with_correct_username() throws Exception {
    mockMvc.perform(multipart("/api/v1/users/{username}/follow", "h.skenderskii")
            .param("FollowRequest", "true")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email", is(TEST_USER1_EMAIL)))
        .andExpect(jsonPath("$.username", is(TEST_USER1_USERNAME)))
        .andExpect(jsonPath("$.fullName", is(TEST_USER1_NAMES)));
  }

  @Test
  @WithMockUser(username = TEST_USER2_EMAIL)
  public void unfollow_user_with_correct_username() throws Exception {
    mockMvc.perform(multipart("/api/v1/users/{username}/follow", "h.skenderskii")
            .param("FollowRequest", "false")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email", is(TEST_USER1_EMAIL)))
        .andExpect(jsonPath("$.username", is(TEST_USER1_USERNAME)))
        .andExpect(jsonPath("$.fullName", is(TEST_USER1_NAMES)));
  }

  @Test
  @WithMockUser(username = TEST_USER1_EMAIL)
  public void getAllUsers_With_False() throws Exception {
    mockMvc.perform(get("/api/v1/users")
            .param("name", "i")
            .param("f", "false")
        )
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = TEST_USER1_EMAIL)
  public void getAllUsers_With_True() throws Exception {
    mockMvc.perform(get("/api/v1/users")
            .param("name", "i")
            .param("f", "true")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].email", is(TEST_USER2_EMAIL)))
        .andExpect(jsonPath("$.[0].username", is(TEST_USER2_USERNAME)))
        .andExpect(jsonPath("$.[0].fullName", is(TEST_USER2_NAMES)));
  }

  @Test
  @WithMockUser(username = TEST_USER1_EMAIL)
  public void getAllUsers_With_No_Such_String_False() throws Exception {
    mockMvc.perform(get("/api/v1/users")
            .param("name", "i")
            .param("f", "false")
        )
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = TEST_USER1_EMAIL)
  public void getAllUsers_With_No_Such_String_True() throws Exception {
    mockMvc.perform(get("/api/v1/users")
            .param("name", "i")
            .param("f", "true")
        )
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test(expectedExceptions = AssertionError.class)
  public void getAllUsers_Without_Authorization() throws Exception {
    mockMvc.perform(get("/api/v1/users")
            .param("name", "i")
            .param("f", "true")
        )
        .andDo(print())
        .andExpect(status().isOk());
  }


  @Test
  @WithMockUser(username = TEST_USER1_EMAIL)
  public void change_User_Password() throws Exception {
    mockMvc.perform(multipart("/api/v1/users/me")
            .param("password", "Hrisi1ha!!")
            .param("passwordConfirmation", "Hrisi1ha!!")
            .param("oldPassword", "Hrisi1ha!")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email", is(TEST_USER1_EMAIL)))
        .andExpect(jsonPath("$.username", is(TEST_USER1_USERNAME)))
        .andExpect(jsonPath("$.fullName", is(TEST_USER1_NAMES)));
  }

  @Test
  @WithMockUser(username = TEST_USER1_EMAIL)
  public void change_User_Password_With_Dont_Match_Passwords() throws Exception {
    mockMvc.perform(multipart("/api/v1/users/me")
            .param("password", "Hrisi1ha!!")
            .param("passwordConfirmation", "Hrisi1ha!")
            .param("oldPassword", "Hrisi1ha!")
        )
        .andDo(print())
        .andExpect(status()
            .isBadRequest());
  }
}
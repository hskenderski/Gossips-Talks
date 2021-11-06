package bg.codeacademy.spring.gossiptalks;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.oneOf;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;


// Enable the 'dev' profile - uses in-memory DB so data is cleared
// after each test class.
@ActiveProfiles("dev")
// Run the spring application on a random web port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled
public class GossipTalksIntegrationTests {

  private static final String DEFAULT_PASS = "p@ssworD1longenough";

  // get the random port, used by the spring application
  @LocalServerPort
  int port;

  @BeforeClass
  public void beforeEachTest() {
    // init port and logging
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @AfterClass
  public void afterEachTest() {
    RestAssured.reset();
  }

  @Test
  public void createUser_with_InvalidUsername_should_Fail() {
    given()
        // prepare
        .multiPart("email", "u1@mail.com")
        .multiPart("username", "UserIvan")//'^[a-z0-8\\.\\-]+$'
        .multiPart("password", DEFAULT_PASS)
        .multiPart("passwordConfirmation", DEFAULT_PASS)
        // do
        .when()
        .post("/api/v1/users")
        // test
        .then()
        .statusCode(not(oneOf(OK.value(), CREATED.value())));
  }

  @Test
  public void createUser_with_InvalidEmail_should_Fail() {
    given()
        // prepare
        .multiPart("email", "u1")
        .multiPart("username", "userivan")
        .multiPart("password", DEFAULT_PASS)
        .multiPart("passwordConfirmation", DEFAULT_PASS)
        // do
        .when()
        .post("/api/v1/users")
        // test
        .then()
        .statusCode(not(oneOf(OK.value(), CREATED.value())));
  }

  @Test
  public void createUser_with_NotMatchingPasswords_should_Fail() {
    given()
        // prepare
        .multiPart("email", "u1@mail.com")
        .multiPart("username", "userivan")
        .multiPart("password", DEFAULT_PASS)
        .multiPart("passwordConfirmation", "hello")
        // do
        .when()
        .post("/api/v1/users")
        // test
        .then()
        .statusCode(not(oneOf(OK.value(), CREATED.value())));
  }

  @Test
  public void createUser_with_UsernameWithDot_should_Pass() {
    createValidUser("username.with.dots");
  }


  @Test
  public void createGossip_with_InvalidContent_should_Fail() {
    createValidUser("cgic");

    given()
        // prepare
        .multiPart("text", "** Header\n<script type=\"javascript\">alert(1);</script>")
        // auth
        .auth()
        .basic("cgic", DEFAULT_PASS)
        // do
        .when()
        .post("/api/v1/gossips")
        // test
        .then()
        .statusCode(not(OK.value()));
  }


  private void createValidUser(String name) {
    given()
        // prepare
        .multiPart("email", name + "@mail.com")
        .multiPart("username", name)//'^[a-z0-8\\.\\-]+$'
        .multiPart("password", DEFAULT_PASS)
        .multiPart("passwordConfirmation", DEFAULT_PASS)
        // do
        .when()
        .post("/api/v1/users")
        // test
        .then()
        .statusCode(oneOf(OK.value(), CREATED.value()));
  }
}

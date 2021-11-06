package bg.codeacademy.spring.gossiptalks.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

import bg.codeacademy.spring.gossiptalks.entities.Gossips;
import bg.codeacademy.spring.gossiptalks.entities.User;
import bg.codeacademy.spring.gossiptalks.exceptions.InvalidException;
import bg.codeacademy.spring.gossiptalks.repositories.GossipRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GossipServiceImpTest {

  @Mock
  private GossipRepository gossipRepository;

  private GossipServiceImp gossipServiceImp;

  @Mock
  private Authentication authentication;

  @Mock
  private SecurityContext securityContext;

  @Mock
  UserService userService;

  private Map<Long, Gossips> fakeDb = new HashMap<>();

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    gossipServiceImp = new GossipServiceImp(gossipRepository, userService);
    authentication = Mockito.mock(Authentication.class);
    securityContext = Mockito.mock(SecurityContext.class);
  }

  @Test
  public void createGossip() {
    User user = new User("h.skenderski@abv.bg", "Julkin", "Hristiyan Skenderski", "Hrisi1ha!");

    Gossips expectedGossip = new Gossips("alabaal", user.getUsername());

    when(gossipRepository.save(any(Gossips.class)))
        .thenReturn(expectedGossip);

    when(gossipRepository.findById(any()))
        .thenReturn(Optional.of(expectedGossip));

    user.getGossips().add(expectedGossip);
    String text = expectedGossip.getText();
    Gossips actualGossip = gossipServiceImp.createGossip(text, user.getUsername());

    verify(gossipRepository, times(1)).save(any(expectedGossip.getClass()));

    assertEquals(actualGossip, expectedGossip);
    assertEquals(actualGossip.getUsername(), expectedGossip.getUsername());
    assertEquals(actualGossip.getText(), expectedGossip.getText());
  }

  @Test(expectedExceptions = InvalidException.class)
  public void createGossip_And_Return_Empty_Text() {
    User user = new User("h.skenderski@abv.bg", "Julkin", "Hristiyan Skenderski", "Hrisi1ha!");

    Gossips expectedGossip = new Gossips("", user.getUsername());

    when(gossipRepository.save(any(Gossips.class)))
        .thenReturn(expectedGossip);

    when(gossipRepository.findById(any()))
        .thenReturn(Optional.of(expectedGossip));

    user.getGossips().add(expectedGossip);
    String text = expectedGossip.getText();
    Gossips actualGossip = gossipServiceImp.createGossip(text, user.getUsername());

    verify(gossipRepository, times(1)).save(any(expectedGossip.getClass()));

    assertEquals(actualGossip, expectedGossip);
    assertEquals(actualGossip.getUsername(), expectedGossip.getUsername());
    assertEquals(actualGossip.getText(), expectedGossip.getText());
  }

  @Test(expectedExceptions = InvalidException.class)
  public void createGossip_And_Return_Length_Limit() {
    User user = new User("h.skenderski@abv.bg", "Julkin", "Hristiyan Skenderski", "Hrisi1ha!");

    Gossips expectedGossip = new Gossips(
        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        user.getUsername());

    when(gossipRepository.save(any(Gossips.class)))
        .thenReturn(expectedGossip);

    when(gossipRepository.findById(any()))
        .thenReturn(Optional.of(expectedGossip));

    user.getGossips().add(expectedGossip);
    String text = expectedGossip.getText();
    Gossips actualGossip = gossipServiceImp.createGossip(text, user.getUsername());

    verify(gossipRepository, times(1)).save(any(expectedGossip.getClass()));

    assertEquals(actualGossip, expectedGossip);
    assertEquals(actualGossip.getUsername(), expectedGossip.getUsername());
    assertEquals(actualGossip.getText(), expectedGossip.getText());
  }

}
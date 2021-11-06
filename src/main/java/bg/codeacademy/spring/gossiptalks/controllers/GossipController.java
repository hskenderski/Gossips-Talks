package bg.codeacademy.spring.gossiptalks.controllers;

import bg.codeacademy.spring.gossiptalks.dto.CreateGossipRequest;
import bg.codeacademy.spring.gossiptalks.dto.GossipDto;
import bg.codeacademy.spring.gossiptalks.dto.GossipListDto;
import bg.codeacademy.spring.gossiptalks.entities.Gossips;
import bg.codeacademy.spring.gossiptalks.entities.User;
import bg.codeacademy.spring.gossiptalks.services.GossipService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

@RestController
@Validated
@RequestMapping("/api/v1")
public class GossipController {

  private final GossipService gossipService;

  @Autowired
  public GossipController(GossipService gossipService) {
    this.gossipService = gossipService;
  }


  @PreAuthorize("hasRole('USER')")
  @PostMapping(path = "/gossips", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @ResponseStatus(value = HttpStatus.OK)
  public GossipDto writeGossip(@Valid CreateGossipRequest createGossipRequest) {
    User user = gossipService.getUserService()
        .findUserByEmail(this.gossipService.getUserService().getCurrentUser().getEmail());
    Gossips gossip = gossipService.createGossip(createGossipRequest.getText(), user.getUsername());
    user.getGossips().add(gossip);
    return gossipService.getUserService().getModelMapper()
        .map(gossip, GossipDto.class);
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/gossips")
  @ResponseStatus(value = HttpStatus.OK)
  public GossipListDto showFollowersPost(
      @RequestParam(defaultValue = "0") @Min(value = 0) Integer pageNo,
      @Min(value = 1) @Max(value = 100) @RequestParam(defaultValue = "100") Integer pageSize) {

    User user = this.gossipService.getUserService()
        .findUserByEmail(this.gossipService.getUserService().getCurrentUser().getEmail());

    List<Gossips> history = this.gossipService.getHistory(user.getEmail(), user.getUserId(), pageNo,
        pageNo);

    List<GossipDto> historyDto = this.gossipService.getUserService().getModelMapper()
        .map(history, this.gossipService.getListType());

    Page<GossipDto> page = this.gossipService.paging(pageNo, pageSize, historyDto);

    return this.gossipService.setGossipListDtoParameters(pageNo, pageSize, page);
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/users/{username}/gossips")
  @ResponseStatus(value = HttpStatus.OK)
  public GossipListDto showUserPost(@Valid @PathVariable(name = "username") String username,
      @RequestParam(defaultValue = "0") @Min(value = 0) Integer pageNo,
      @Min(value = 1) @Max(value = 100) @RequestParam(defaultValue = "100") Integer pageSize) {

    List<Gossips> userGossips = this.gossipService.getUserGossips(username, pageNo, pageSize);

    List<GossipDto> userGossipsDto = this.gossipService.getUserService().getModelMapper()
        .map(userGossips, this.gossipService.getListType());

    Page<GossipDto> page = this.gossipService.paging(pageNo, pageSize, userGossipsDto);

    return this.gossipService.setGossipListDtoParameters(pageNo, pageSize, page);
  }


}
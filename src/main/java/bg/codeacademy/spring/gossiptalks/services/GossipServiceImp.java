package bg.codeacademy.spring.gossiptalks.services;

import bg.codeacademy.spring.gossiptalks.dto.GossipDto;
import bg.codeacademy.spring.gossiptalks.dto.GossipListDto;
import bg.codeacademy.spring.gossiptalks.entities.Gossips;
import bg.codeacademy.spring.gossiptalks.exceptions.InvalidException;
import bg.codeacademy.spring.gossiptalks.repositories.GossipRepository;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class GossipServiceImp implements GossipService {

  private final GossipRepository gossipRepository;
  private final UserService userService;

  @Autowired
  public GossipServiceImp(GossipRepository gossipRepository, UserService userService) {
    this.gossipRepository = gossipRepository;
    this.userService = userService;
  }

  @Override
  public UserService getUserService() {
    return userService;
  }

  @Override
  public Gossips createGossip(String text, String username) {
    if (text.isEmpty()) {
      throw new InvalidException("You can not write empty gossip!");
    } else if (text.length() > 255) {
      throw new InvalidException("The message is too long!");
    }
    //replace HTML entities
    text = text.replaceAll("\\<.*?\\>", "");
    Gossips gossip = new Gossips(text, username);
    return this.gossipRepository.save(gossip);
  }

  @Override
  public List<Gossips> getHistory(String email, Long user_id, Integer pageNo, Integer pageSize) {
    List<Integer> index = getStartAndEndIndex(pageNo, pageSize);
    return this.gossipRepository.getHistory(email, user_id, index.get(0), index.get(1));
  }

  @Override
  public List<Gossips> getUserGossips(String username, Integer pageNo, Integer pageSize) {
    if (this.userService.getUserRepository().findUserByUsername(username) == null) {
      throw new InvalidException(String.format("There is not user with username: %s", username));
    }
    List<Integer> index = getStartAndEndIndex(pageNo, pageSize);
    return this.gossipRepository.getUserGossips(username, index.get(0), index.get(1));
  }

  public Page<GossipDto> paging(Integer pageNo, Integer pageSize, List<GossipDto> historyDto) {
    //Paging the List<GossipDto> historyDto
    Pageable paging = PageRequest.of(pageNo, pageSize);
    int start = Math.min((int) paging.getOffset(), historyDto.size());
    int end = Math.min((start + paging.getPageSize()), historyDto.size());
    //return the Page
    return new PageImpl<>(historyDto.subList(start, end), paging,
        historyDto.size());
  }

  public GossipListDto setGossipListDtoParameters(Integer pageNo, Integer pageSize,
      Page<GossipDto> page) {
    //Add the page parameters and the collection to the GossipListDto
    GossipListDto gossipLists = new GossipListDto();
    gossipLists.setPageNumber(pageNo);
    gossipLists.setPageSize(pageSize);
    gossipLists.setCount(Math.toIntExact(page.getTotalElements()));
    gossipLists.setTotal(page.getTotalPages());
    gossipLists.setContent(page);
    return gossipLists;
  }

  private List<Integer> getStartAndEndIndex(Integer pageNo, Integer pageSize) {
    List<Integer> list = new ArrayList<>();
    int start = 0;
    int end = 0;
    int count = 0;
    //The count is the number of gossips from previous pages
    for (int i = 0; i < pageNo; i++) {
      count += pageSize;
    }
    //Check if the previous gossips is bellow 100 set Limit from 0-100
    if (count <= 100) {
      end = 100;
    } else {     //if not set the Limit from previous gossips to previous gossips+100
      start = count;
      end = count + 100;
    }
    //Add them to list and then return it index[0] for start index[1] for end
    list.add(start);
    list.add(end);
    return list;
  }

  @Override
  public Type getListType() {
    return new TypeToken<List<GossipDto>>() {
    }.getType();
  }
}


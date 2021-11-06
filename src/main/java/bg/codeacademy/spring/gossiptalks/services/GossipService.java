package bg.codeacademy.spring.gossiptalks.services;

import bg.codeacademy.spring.gossiptalks.dto.GossipDto;
import bg.codeacademy.spring.gossiptalks.dto.GossipListDto;
import bg.codeacademy.spring.gossiptalks.entities.Gossips;
import java.lang.reflect.Type;
import java.util.List;
import org.springframework.data.domain.Page;

public interface GossipService {

  UserService getUserService();

  Gossips createGossip(String text, String username);

  List<Gossips> getHistory(String email, Long user_id, Integer start, Integer end);

  List<Gossips> getUserGossips(String username, Integer pageNo, Integer pageSize);

  Page<GossipDto> paging(Integer pageNo, Integer pageSize, List<GossipDto> historyDto);

  GossipListDto setGossipListDtoParameters(Integer pageNo, Integer pageSize, Page<GossipDto> page);

  Type getListType();
}

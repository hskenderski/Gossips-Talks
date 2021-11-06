package bg.codeacademy.spring.gossiptalks.dto;

import org.springframework.data.domain.Page;

public class GossipListDto {

  private Integer pageNumber;
  private Integer pageSize;
  private Integer count;
  private Integer total;
  private Page<GossipDto> content;

  public Integer getPageNumber() {
    return pageNumber;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public Integer getCount() {
    return count;
  }

  public Integer getTotal() {
    return total;
  }

  public Page<GossipDto> getContent() {
    return content;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public void setContent(Page<GossipDto> content) {
    this.content = content;
  }

}

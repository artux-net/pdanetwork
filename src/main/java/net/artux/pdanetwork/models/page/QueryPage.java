package net.artux.pdanetwork.models.page;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;

@Getter
@Setter
public class QueryPage {
  @Min(value = 1, message = "{queryPage.numberPage.lessThan}")
  private int number = 1;

  @Min(value = 1, message = "{queryPage.sizePage.lessThan}")
  private int size = 15;

  private Sort.Direction sortDirection = Sort.Direction.DESC;

  private String sortBy = "id";
}

package net.artux.pdanetwork.dto.page;

import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@EqualsAndHashCode
public class QueryPage {
  @Min(value = 1, message = "{queryPage.numberPage.lessThan}")
  private int number = 1;

  @Min(value = 1, message = "{queryPage.sizePage.lessThan}")
  private int size = 15;

  private Sort.Direction sortDirection = Sort.Direction.DESC;

  private String sortBy = "id";
}

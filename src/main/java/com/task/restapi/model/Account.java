package com.task.restapi.model;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class Account {
  @NotEmpty
  @Size(min = 8, max = 8)
  private String accountNumber;

  private List<String> providers;
}

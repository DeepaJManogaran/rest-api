package com.task.restapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderResponse {
  private String provider;

  @JsonProperty(value = "isValid")
  private boolean isValid;
}

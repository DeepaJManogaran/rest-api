package com.task.restapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
  @JsonProperty(value = "result", required = true)
  private List<ProviderResponse> responseList;

  public ValidationResult() {
    responseList = new ArrayList();
  }

  public void addResponseList(ProviderResponse response) {
    responseList.add(response);
  }

  public List<ProviderResponse> getResponseList() {
    return responseList;
  }
}

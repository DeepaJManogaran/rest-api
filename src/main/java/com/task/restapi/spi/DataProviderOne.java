package com.task.restapi.spi;

import com.task.restapi.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class DataProviderOne {

  @Value("${spring.provider1.url}")
  private String serviceUrl;

  @Autowired RestTemplate restTemplate;

  public boolean accountLookUp(String acctNumber) throws DataException {
    boolean isValidAcct = false;
    try {
      isValidAcct = restTemplate.postForObject(serviceUrl, acctNumber, Boolean.class);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new DataException("Unable to reach the Data provider 1");
    }
    return isValidAcct;
  }
}

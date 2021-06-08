package com.task.restapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.task.restapi.exception.DataException;
import com.task.restapi.exception.ServiceException;
import com.task.restapi.model.ProviderResponse;
import com.task.restapi.spi.DataProviderOne;
import com.task.restapi.spi.DataProviderTwo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccountValidationServiceTest {

  @TestConfiguration
  static class AccountValidationServiceTestConfiguration {

    @Bean
    public AccountValidationService validationService() {
      return new AccountValidationServiceImpl();
    }
  }

  @Autowired AccountValidationService validationService;

  @MockBean DataProviderOne dataproviderOne;

  @MockBean DataProviderTwo dataproviderTwo;

  @Test
  public void validateAcctFromProviderOne_isValid_Success() throws Exception {
    // Given
    String accountNumber = "1234";
    Mockito.when(dataproviderOne.accountLookUp(accountNumber)).thenReturn(true);
    // when
    ProviderResponse actualResponse = validationService.validateAcctFromProviderOne(accountNumber);

    // then
    ProviderResponse expectedResponse = new ProviderResponse("provider1", true);
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void validateAcctFromProviderTwo_isValid_Success() throws Exception {
    // Given
    String accountNumber = "1234";
    Mockito.when(dataproviderTwo.accountLookUp(accountNumber)).thenReturn(true);
    // when
    ProviderResponse actualResponse = validationService.validateAcctFromProviderTwo(accountNumber);

    // then
    ProviderResponse expectedResponse = new ProviderResponse("provider2", true);
    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = ServiceException.class)
  public void validateAcctFromProviderOne_isNotValid_ThrowsException() throws Exception {
    // Given
    String accountNumber = "1234";
    Mockito.when(dataproviderOne.accountLookUp(accountNumber)).thenThrow(DataException.class);
    // when
    validationService.validateAcctFromProviderOne(accountNumber);
  }

  @Test(expected = ServiceException.class)
  public void validateAcctFromProviderTwo_isNotValid_ThrowsException() throws Exception {
    // Given
    String accountNumber = "1234";
    Mockito.when(dataproviderTwo.accountLookUp(accountNumber)).thenThrow(DataException.class);
    // when
    validationService.validateAcctFromProviderTwo(accountNumber);
  }
}

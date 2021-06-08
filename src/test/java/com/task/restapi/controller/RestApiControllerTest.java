package com.task.restapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.restapi.model.Account;
import com.task.restapi.model.ProviderResponse;
import com.task.restapi.model.ValidationResult;
import com.task.restapi.service.AccountValidationService;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RestApiController.class)
class RestApiControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AccountValidationService validationService;

  private String urlTemplate = "/validateAcct";
  private ObjectMapper mapper = new ObjectMapper();

  @Test
  void validateAccount_WithNoProviders_Success() throws Exception {
    // Given
    Account account = new Account();
    account.setAccountNumber("12345678");

    ProviderResponse response1 = new ProviderResponse("provider1", true);
    ProviderResponse response2 = new ProviderResponse("provider2", true);

    when(validationService.validateAcctFromProviderOne(account.getAccountNumber()))
        .thenReturn(response1);
    when(validationService.validateAcctFromProviderTwo(account.getAccountNumber()))
        .thenReturn(response2);

    // When
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(urlTemplate)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(account))
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();

    // then
    assertEquals(HttpStatus.OK.value(), response.getStatus());

    ValidationResult validationResult = new ValidationResult();
    validationResult.addResponseList(response1);
    validationResult.addResponseList(response2);
    String expected = mapper.writeValueAsString(validationResult);
    JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
  }

  @Test
  void validateAccount_WithNullObject_BadRequest() throws Exception {
    // Given
    Account account = null;

    // When
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(urlTemplate)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(account))
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();

    // then
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  void validateAccount_WithBothProviders_Success() throws Exception {
    // Given
    Account account = new Account();
    account.setAccountNumber("12345678");
    account.setProviders(Arrays.asList("provider1", "provider2"));

    ProviderResponse response1 = new ProviderResponse("provider1", true);
    ProviderResponse response2 = new ProviderResponse("provider2", true);

    when(validationService.validateAcctFromProviderOne(account.getAccountNumber()))
        .thenReturn(response1);
    when(validationService.validateAcctFromProviderTwo(account.getAccountNumber()))
        .thenReturn(response2);

    // When
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(urlTemplate)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(account))
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();

    // then
    assertEquals(HttpStatus.OK.value(), response.getStatus());

    ValidationResult validationResult = new ValidationResult();
    validationResult.addResponseList(response1);
    validationResult.addResponseList(response2);
    String expected = mapper.writeValueAsString(validationResult);
    JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
  }

  @Test
  void validateAccount_WithBothProviders_SecondProviderFailed() throws Exception {
    // Given
    Account account = new Account();
    account.setAccountNumber("12345678");
    account.setProviders(Arrays.asList("provider1", "provider2"));

    ProviderResponse response1 = new ProviderResponse("provider1", true);
    ProviderResponse response2 = new ProviderResponse("provider2", false);

    when(validationService.validateAcctFromProviderOne(account.getAccountNumber()))
        .thenReturn(response1);
    when(validationService.validateAcctFromProviderTwo(account.getAccountNumber()))
        .thenReturn(response2);

    // When
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(urlTemplate)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(account))
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();

    // then
    assertEquals(HttpStatus.OK.value(), response.getStatus());

    ValidationResult validationResult = new ValidationResult();
    validationResult.addResponseList(response1);
    validationResult.addResponseList(response2);
    String expected = mapper.writeValueAsString(validationResult);
    JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
  }

  @Test
  void validateAccount_WithOneProvider_Success() throws Exception {
    // Given
    Account account = new Account();
    account.setAccountNumber("12345678");
    account.setProviders(Arrays.asList("provider1"));

    ProviderResponse response1 = new ProviderResponse("provider1", true);

    when(validationService.validateAcctFromProviderOne(account.getAccountNumber()))
        .thenReturn(response1);

    // When
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(urlTemplate)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(account))
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();

    // then
    assertEquals(HttpStatus.OK.value(), response.getStatus());

    ValidationResult validationResult = new ValidationResult();
    validationResult.addResponseList(new ProviderResponse("provider1", true));
    String expected = mapper.writeValueAsString(validationResult);
    JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
  }

  @Test
  void validateAccount_WithEmptyAccountNumber_ErrorMessage() throws Exception {
    // Given
    Account account = new Account();
    account.setAccountNumber("");

    // When
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(urlTemplate)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(account))
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();

    // then
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  void validateAccount_WithInValidAccountNumber_ErrorMessage() throws Exception {
    // Given
    Account account = new Account();
    account.setAccountNumber("12312");

    // When
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(urlTemplate)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(account))
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();

    // then
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }
}

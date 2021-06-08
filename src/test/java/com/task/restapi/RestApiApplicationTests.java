package com.task.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.restapi.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class RestApiApplicationTests {
  @Autowired private MockMvc mockMvc;
  @MockBean private RestTemplate restTemplate;

  @Value("${spring.provider1.url}")
  private String serviceUrl1;

  @Value("${spring.provider2.url}")
  private String serviceUrl2;

  @Test
  public void happyPathTest() throws Exception {
    // given
    Account account = new Account();
    account.setAccountNumber("12345678");
    Mockito.when(restTemplate.postForObject(serviceUrl1, account.getAccountNumber(), Boolean.class))
        .thenReturn(true);
    Mockito.when(restTemplate.postForObject(serviceUrl2, account.getAccountNumber(), Boolean.class))
        .thenReturn(true);

    // when
    MockHttpServletResponse actual =
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.post("/validateAcct")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(account)))
            .andReturn()
            .getResponse();

    // then
    String expected =
        "{\"result\":[{\"provider\":\"provider1\",\"isValid\":true},{\"provider\":\"provider2\",\"isValid\":true}]}";
    assertEquals(200, actual.getStatus());
    JSONAssert.assertEquals(expected, actual.getContentAsString(), false);
  }
}

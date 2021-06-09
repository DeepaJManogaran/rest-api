package com.task.restapi.controller;

import com.task.restapi.exception.ServiceException;
import com.task.restapi.model.Account;
import com.task.restapi.model.ProviderResponse;
import com.task.restapi.model.ValidationResult;
import com.task.restapi.service.AccountValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class RestApiController {

    @Value("${spring.provider.list}")
    private List<String> providerList;

    @Value("${spring.provider1.name}")
    private String provider1;

    @Value("${spring.provider2.name}")
    private String provider2;

    @Autowired
    AccountValidationService validationService;

    @PostMapping(
            path = "/validateAcct",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity validateAccount(@Valid @RequestBody Account account) {
        String acctNumber = account.getAccountNumber();
        if (ObjectUtils.isEmpty(account)) {
            return new ResponseEntity<>("No request Parameters Found ", HttpStatus.BAD_REQUEST);
        } else {
            log.info("validateAcct Rest call for account Number {} and providers {}", acctNumber, account.getProviders());
            ValidationResult result = validate(acctNumber, account.getProviders());
            if (result != null) {
                log.info("Response for accountNumber {} :", acctNumber, result);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                log.info("Error Response for accountNumber {} ", acctNumber);
                return new ResponseEntity<>("Error in Processing request ", HttpStatus.BAD_REQUEST);
            }
        }
    }

    public ValidationResult validate(String acctNumber, List<String> providers) {
        ValidationResult result = null;
        try {
            if (CollectionUtils.isEmpty(providers) || providerList.equals(providers)) {
                result = createResponseWithDefaults(acctNumber);
            } else if (providers.contains(provider1)) {
                ProviderResponse response = validationService.validateAcctFromProviderOne(acctNumber);
                result = new ValidationResult();
                result.addResponseList(response);
            } else if (providers.contains(provider2)) {
                ProviderResponse response = validationService.validateAcctFromProviderTwo(acctNumber);
                result = new ValidationResult();
                result.addResponseList(response);
            }
            log.info("Validation Result for accountNumber {} :", acctNumber, result);
        } catch (ServiceException e) {
            log.error("Service Exception : " + e.getLocalizedMessage());
        }
        return result;
    }

    private ValidationResult createResponseWithDefaults(String acctNumber) throws ServiceException {
        ProviderResponse spiResponseOne = validationService.validateAcctFromProviderOne(acctNumber);
        ProviderResponse spiResponseTwo = validationService.validateAcctFromProviderTwo(acctNumber);

        ValidationResult result = new ValidationResult();
        result.getResponseList().add(spiResponseOne);
        result.getResponseList().add(spiResponseTwo);
        return result;
    }
}

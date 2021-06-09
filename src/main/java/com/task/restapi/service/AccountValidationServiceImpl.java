package com.task.restapi.service;

import com.task.restapi.exception.DataException;
import com.task.restapi.exception.ServiceException;
import com.task.restapi.model.ProviderResponse;
import com.task.restapi.spi.DataProviderOne;
import com.task.restapi.spi.DataProviderTwo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountValidationServiceImpl implements AccountValidationService {

    @Autowired
    DataProviderOne dataproviderOne;

    @Autowired
    DataProviderTwo dataproviderTwo;

    @Value("${spring.provider1.name}")
    private String provider1;

    @Value("${spring.provider2.name}")
    private String provider2;

    @Override
    public ProviderResponse validateAcctFromProviderOne(String accountNumber)
            throws ServiceException {
        ProviderResponse response = null;
        boolean isValid = false;
        log.info("Calling validateAcctFromProviderOne for accountNumber {} ", accountNumber);
        try {
            isValid = dataproviderOne.accountLookUp(accountNumber);
            response = new ProviderResponse();
            response.setProvider(provider1);
            response.setValid(isValid);
        } catch (DataException e) {
            log.error("Exception in validateAcctFromProviderOne : {} ", e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        log.info("Response after validateAcctFromProviderOne for accountNumber {} : {}", accountNumber, response);
        return response;
    }

    @Override
    public ProviderResponse validateAcctFromProviderTwo(String accountNumber)
            throws ServiceException {
        boolean isValid = false;
        ProviderResponse response = null;
        log.info("Calling validateAcctFromProviderTwo for accountNumber {} ", accountNumber);
        try {
            isValid = dataproviderTwo.accountLookUp(accountNumber);
            response = new ProviderResponse();
            response.setProvider(provider2);
            response.setValid(isValid);
        } catch (DataException e) {
            log.error("Exception in validateAcctFromProviderTwo  : {} ", e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        log.info("Response after validateAcctFromProviderTwo for accountNumber {} : {}", accountNumber, response);
        return response;
    }
}

package com.task.restapi.service;

import com.task.restapi.exception.ServiceException;
import com.task.restapi.model.ProviderResponse;

public interface AccountValidationService {
  ProviderResponse validateAcctFromProviderOne(String accountNumber) throws ServiceException;

  ProviderResponse validateAcctFromProviderTwo(String accountNumber) throws ServiceException;
}

package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.BalanceDTO.Request;
import com.fastcampus.loan.dto.BalanceDTO.Response;
public interface BalanceService {

    Response create(Long applicationId, Request request);
}

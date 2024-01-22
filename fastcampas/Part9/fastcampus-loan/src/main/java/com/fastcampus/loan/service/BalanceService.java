package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Repayment;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.dto.BalanceDTO.Request;
import com.fastcampus.loan.dto.BalanceDTO.RepaymentRequest;
import com.fastcampus.loan.dto.BalanceDTO.UpdateRequest;
import com.fastcampus.loan.dto.BalanceDTO.Response;
public interface BalanceService {

    Response create(Long applicationId, Request request);

    Response get(Long applicationId);

    Response update(Long applicationId, UpdateRequest request);

    Response repaymentUpdate(Long applicationId, RepaymentRequest request);

    void delete(Long applicationId);
}

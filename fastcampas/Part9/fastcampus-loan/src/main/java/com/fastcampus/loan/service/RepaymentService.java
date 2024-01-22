package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.ApplicationDTO;
import com.fastcampus.loan.dto.RepaymentDTO;
import com.fastcampus.loan.dto.RepaymentDTO.Request;
import com.fastcampus.loan.dto.RepaymentDTO.Response;
import com.fastcampus.loan.dto.RepaymentDTO.UpdateResponse;

import java.util.List;

public interface RepaymentService {
    Response create(Long applicationId, Request request);

    List<Response> get(Long applicationId);

    UpdateResponse update(Long repaymentId, Request request);
}
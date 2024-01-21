package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.EntryDTO.Request;
import com.fastcampus.loan.dto.EntryDTO.Response;

public interface EntryService {

    Response create(Long applicationId, Request request);

    Response get(Long applicationId);
}

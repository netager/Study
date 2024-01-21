package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Entry;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.dto.EntryDTO.Request;
import com.fastcampus.loan.dto.EntryDTO.Response;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final BalanceService balanceService;

    private final ApplicationRepository applicationRepository;
    private final EntryRepository entryRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, Request request) {
        // 계약 체결 검증
        System.out.println("[EntryServiceImpl] request: " + request.getEntryAmount());
        if (!isContractedApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Entry entry = modelMapper.map(request, Entry.class);
        entry.setApplicationId(applicationId);

        System.out.println("[EntryServiceImpl] Entry : " + entry.getEntryId() + ":" + entry.getApplicationId() + ":" + entry.getEntryAmount());

        entryRepository.save(entry);

        // 대출 잔고 관리
        balanceService.create(applicationId,
                BalanceDTO.Request.builder()
                    .entryAmount(request.getEntryAmount())
                    .build());

        return modelMapper.map(entry, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Optional<Entry> entry = entryRepository.findByApplicationId(applicationId);
        if (entry.isPresent()) {
            return modelMapper.map(entry, Response.class);
        } else {
            return null;
        }
    }

    private boolean isContractedApplication(Long applicationId) {
        Optional<Application> existed = applicationRepository.findById(applicationId);
        if (existed.isEmpty()) {
            return false;
        }

        return existed.get().getCreatedAt() != null;
    }

}

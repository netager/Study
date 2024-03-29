package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Judgment;
import com.fastcampus.loan.dto.ApplicationDTO.GrantAmount;
import com.fastcampus.loan.dto.JudgmentDTO.Response;
import com.fastcampus.loan.dto.JudgmentDTO.Request;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.JudgmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class JudgmentServiceImpl implements JudgmentService {

    private final JudgmentRepository judgmentRepository;
    private final ApplicationRepository applicationRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Request request) {
        //신청정보 검증
        Long applicationId = request.getApplicationId();
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        //request dto -> entity -> save
        Judgment judgment = modelMapper.map(request, Judgment.class);

        // save -> response dto
        Judgment saved = judgmentRepository.save(judgment);

        return modelMapper.map(saved, Response.class);

    }

    @Override
    public Response get(Long judgmentId) {
        Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
            throw new BaseException((ResultType.SYSTEM_ERROR));
        });
        return modelMapper.map(judgment, Response.class);
    }

    @Override
    public Response getJudgmentOfApplication(Long applicationId) {
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Judgment judgment = judgmentRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        return modelMapper.map(judgment, Response.class);
    }

    @Override
    public Response update(Long judgmentId, Request request) {
        Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
            throw new BaseException((ResultType.SYSTEM_ERROR));
        });

        judgment.setApplicationId(request.getApplicationId());
        judgment.setName(request.getName());
        judgment.setApprovalAmount(request.getApprovalAmount());

        judgmentRepository.save(judgment);

        return modelMapper.map(judgment, Response.class);
    }

    @Override
    public void delete(Long judgmentId) {
        Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        judgment.setIsDeleted(true);

        judgmentRepository.save(judgment);
    }

    @Override
    public GrantAmount grant(Long judgmentId) {
        Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = judgment.getApplicationId();
        Application application = applicationRepository.findById(applicationId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR));

        BigDecimal approvalAmount = judgment.getApprovalAmount();
        application.setApprovalAmount(approvalAmount);

        applicationRepository.save(application);

        return modelMapper.map(application, GrantAmount.class);

    }

    private boolean isPresentApplication(Long applicationId) {
        return applicationRepository.findById(applicationId).isPresent();
    }

}

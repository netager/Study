package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Entry;
import com.fastcampus.loan.domain.Repayment;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.dto.RepaymentDTO;
import com.fastcampus.loan.dto.RepaymentDTO.Request;
import com.fastcampus.loan.dto.RepaymentDTO.Response;
import com.fastcampus.loan.dto.RepaymentDTO.UpdateResponse;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.EntryRepository;
import com.fastcampus.loan.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentRepository repaymentRepository;
    private final ApplicationRepository applicationRepository;
    private final EntryRepository entryRepository;

    private final BalanceService balanceService;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, RepaymentDTO.Request request) {
        //validation
        // 1. 계약을 완료한 신청 정보
        // 2. 집행이 되어 있어야 함
        if (!isRepayableApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }



        // 잔고 관리
        // balance : 500 -> 100 = 400
        BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                    .repaymentAmount(request.getRepaymentAmount())
                    .type(BalanceDTO.RepaymentRequest.RepaymentType.REMOVE)
                    .build());


        Repayment repayment = modelMapper.map(request, Repayment.class);
        repayment.setApplicationId(applicationId);
        repayment.setRepaymentAmount(request.getRepaymentAmount());
        repayment.setBalance(updatedBalance.getBalance());

        repaymentRepository.save(repayment);

        Response response = modelMapper.map(repayment, Response.class);

        response.setBalance(updatedBalance.getBalance());

        return response;

    }

    @Override
    public List<Response> get(Long applicationId) {
        List<Repayment> repayments = repaymentRepository.findAllByApplicationId(applicationId);
        return repayments.stream().map(r -> modelMapper.map(r, Response.class)).collect(Collectors.toList());
    }

    @Override
    public UpdateResponse update(Long repaymentId, Request request) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = repayment.getApplicationId();
        BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();

        return null;
    }

    private boolean isRepayableApplication(Long applicationId) {
        Optional<Application> existedApplication = applicationRepository.findById(applicationId);
        if (existedApplication.isEmpty()) {
            return false;
        }

        if (existedApplication.get().getContractedAt() == null) {
            return false;
        }

        Optional<Entry>  existedEntry = entryRepository.findByApplicationId(applicationId);
        return existedEntry.isPresent();
    }
}

//    @Override
//    public Response create(Request request) {
//        Application application = modelMapper.map(request, Application.class);
//        application.setAppliedAt(LocalDateTime.now());
//
//        Application applied = applicationRepository.save(application);
//
//        return modelMapper.map(applied, Response.class);
//    }
//
//    @Override
//    public Response get(Long applicationId) {
//        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
//            throw new BaseException((ResultType.SYSTEM_ERROR));
//        });
//        return modelMapper.map(application, Response.class);
//    }
//
//    @Override
//    public Response update(Long applicationId, Request request) {
//        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
//            throw new BaseException((ResultType.SYSTEM_ERROR));
//        });
//
//        application.setName(request.getName());
//        application.setCellPhone(request.getCellPhone());
//        application.setEmail(request.getEmail());
//        application.setHopeAmount(request.getHopeAmount());
//        application.setInterestRate(request.getInterestRate());
//
//        applicationRepository.save(application);
//
//        return modelMapper.map(application, Response.class);
//    }
//
//    @Override
//    public void delete(Long applicationId) {
//        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
//            throw new BaseException(ResultType.SYSTEM_ERROR);
//        });
//
//        application.setIsDeleted(true);
//
//        applicationRepository.save(application);
//    }
//
//    @Override
//    public Boolean acceptTerms(Long applicationId, ApplicationDTO.AcceptTerms request) {
//        applicationRepository.findById(applicationId).orElseThrow(() -> {
//            throw new BaseException(ResultType.SYSTEM_ERROR);
//        });
//
//        List<Terms> termsList = termsRepository.findAll(Sort.by(Direction.ASC, "termsId"));
//        if(termsList.isEmpty()) {
//            throw new BaseException(ResultType.SYSTEM_ERROR);
//        }
//
//        List<Long> acceptTermsIds = request.getAcceptTermsIds();
//        if(termsList.size() != acceptTermsIds.size()) {
//            throw new BaseException(ResultType.SYSTEM_ERROR);
//        }
//
//        List<Long> termsIds = termsList.stream().map(Terms::getTermsId).collect(Collectors.toList());
//        Collections.sort(acceptTermsIds);
//
//        if(!termsIds.containsAll(acceptTermsIds)) {
//            throw new BaseException(ResultType.SYSTEM_ERROR);
//        }
//
//        for(Long termsId : acceptTermsIds) {
//            com.fastcampus.loan.domain.AcceptTerms accepted = com.fastcampus.loan.domain.AcceptTerms.builder()
//                    .termsId(termsId)
//                    .applicationId(applicationId)
//                    .build();
//
//            acceptTermsRepository.save(accepted);
//        }
//
//        return true;
//    }
//
//    @Override
//    public Response contract(Long applicationId) {
//        // 신청 정보 있는지
//        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
//            throw new BaseException(ResultType.SYSTEM_ERROR);
//        });
//
//        // 심사 정보 있는지
//        judgmentRepository.findByApplicationId(applicationId).orElseThrow(() -> {
//           throw new BaseException(ResultType.SYSTEM_ERROR);
//        });
//
//        // 승인된 금액이 > 0
//        if (application.getApprovalAmount() == null
//                || application.getApprovalAmount().compareTo(BigDecimal.ZERO) == 0) {
//            throw new BaseException(ResultType.SYSTEM_ERROR);
//        }
//
//        // 계약 체결
//        application.setContractedAt(LocalDateTime.now());
//        applicationRepository.save(application);
//
//        return null;
//    }
//}
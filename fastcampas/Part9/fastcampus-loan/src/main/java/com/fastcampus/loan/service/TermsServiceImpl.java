package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Terms;
import com.fastcampus.loan.dto.TermsDTO.Request;
import com.fastcampus.loan.dto.TermsDTO.Response;
import com.fastcampus.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;
    private final ModelMapper modelMapper;


    @Override
    public Response create(Request request) {
        Terms terms = modelMapper.map(request, Terms.class);

        Terms applied = termsRepository.save(terms);

        return modelMapper.map(applied, Response.class);
    }

    @Override
    public List<Response> getAll() {
        List<Terms> termsList = termsRepository.findAll();

        return termsList.stream().map(t -> modelMapper.map(t, Response.class)).collect(Collectors.toList());
    }


//
//    @Override
//    public ApplicationDTO.Response update(Long applicationId, ApplicationDTO.Request request) {
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
//        return modelMapper.map(application, ApplicationDTO.Response.class);
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

}

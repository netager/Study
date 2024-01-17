package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Terms;
import com.fastcampus.loan.dto.TermsDTO.Request;
import com.fastcampus.loan.dto.TermsDTO.Response;
import com.fastcampus.loan.repository.TermsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TermsServiceTest {

    @InjectMocks
    TermsServiceImpl termsService;

    @Mock
    private TermsRepository termsRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void Should_ReturnTermsOfNewTermsEntity_When_RequestTerms() {
        Terms entity = Terms.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("http://www.jbbank.co.kr/loanterm")
                .build();

        Request request = Request.builder()
                .name("Terms1")
                .termsDetailUrl("http://www.jbbank.co.kr")
                .build();

        // Mocking 처리
        when(termsRepository.save(ArgumentMatchers.any(Terms.class))).thenReturn(entity);

        Response actual = termsService.create(request);

        assertThat(actual.getName()).isSameAs(entity.getName());
        assertThat(actual.getTermsDetailUrl()).isSameAs(entity.getTermsDetailUrl());
    }

    @Test
    void Should_ReturnAllResponseOfExistTermsEntity_When_RequestTermsList() {
        Terms entityA = Terms.builder()
                .name("대출 이용 약관1")
                .termsDetailUrl("http://www.jbbank.co.kr/loanterm1")
                .build();

        Terms entityB = Terms.builder()
                .name("대출 이용 약관2")
                .termsDetailUrl("http://www.jbbank.co.kr/loanterm2")
                .build();


        Request request = Request.builder()
                .name("Terms1")
                .termsDetailUrl("http://www.jbbank.co.kr")
                .build();

        List<Terms> list = new ArrayList<>(Arrays.asList(entityA, entityB));

        // Mocking 처리
        when(termsRepository.findAll()).thenReturn(list);

        List<Response> actual = termsService.getAll();

        assertThat(actual.size()).isSameAs(list.size());
    }

}

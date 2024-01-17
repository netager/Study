package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.AcceptTerms;
import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Terms;
import com.fastcampus.loan.dto.ApplicationDTO;
import com.fastcampus.loan.dto.ApplicationDTO.Request;
import com.fastcampus.loan.dto.ApplicationDTO.Response;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.repository.AcceptTermsRepository;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.TermsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @InjectMocks
    ApplicationServiceImpl applicationService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private TermsRepository termsRepository;

    @Mock
    private AcceptTermsRepository acceptTermsRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void Should_ReturnApplicationOfNewApplicationEntity_When_RequestApplication() {
        Application entity = Application.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("abc@def.kr")
                .hopeAmount(BigDecimal.valueOf(500000000))
                .build();

        Request request = Request.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("abc@def.kr")
                .hopeAmount(BigDecimal.valueOf(500000000))
                .build();

        // Mocking 처리
        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(entity);

        Response actual = applicationService.create(request);

        assertThat(actual.getHopeAmount()).isSameAs(entity.getHopeAmount());
        assertThat(actual.getName()).isSameAs(entity.getName());
    }

    @Test
    void Should_ReturnApplicationOfExistApplicationEntity_When_RequestExistApplication() {

        Long findId = 1L;

        Application entity = Application.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("abc@def.kr")
                .hopeAmount(BigDecimal.valueOf(500000000))
                .build();

        // Mocking 처리
        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        Response actual = applicationService.get(findId);

        assertThat(actual.getApplicationId()).isSameAs(entity.getApplicationId());
        assertThat(actual.getName()).isSameAs(entity.getName());
        assertThat(actual.getHopeAmount()).isSameAs(entity.getHopeAmount());
    }

    @Test
    void Should_ReturnUpdatedResponseOfExistApplicationEntity_When_RequestUpdateExistApplicationInfo() {
        long findId = 1L;

        Application entity = Application.builder()
                .applicationId(1L)
                .name("Member Kim")
                .hopeAmount(BigDecimal.valueOf(50000))
                .build();

        Request request = Request.builder()
                .name("LEE")
                .hopeAmount(BigDecimal.valueOf(5000))
                .build();

        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(entity);
        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        Response actual = applicationService.update(findId, request);

        assertThat(actual.getApplicationId()).isSameAs(findId);
        assertThat(actual.getHopeAmount()).isSameAs(request.getHopeAmount());
//        assertThat(actual.getName()).isSameAs("Member Lee");
    }

    @Test
    void Should_ReturnDeletedApplicationEntity_When_RequestDeleteExistApplicationInfo() {
        long findId = 1L;

        Application entity = Application.builder()
                .applicationId(1L)
                .build();

        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(entity);
        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        applicationService.delete(findId);

        assertThat(entity.getIsDeleted()).isSameAs(true);
    }

    @Test
    void Should_AddAcceptTerms_When_RequestAcceptTermsOfApplication() {

        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("약관 1")
                .termsDetailUrl("https://abcdef.com/abc1")
                .build();

        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("약관 2")
                .termsDetailUrl("https://abcdef.com/abc2")
                .build();

        List<Long> acceptTerms = Arrays.asList(1L, 2L);

        ApplicationDTO.AcceptTerms request = ApplicationDTO.AcceptTerms.builder()
                .acceptTermsIds(acceptTerms)
                .build();

        Long findId = 1L;

        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(termsRepository.findAll(Sort.by(Direction.ASC, "termsId"))).thenReturn(Arrays.asList(entityA, entityB));
        when(acceptTermsRepository.save(ArgumentMatchers.any(AcceptTerms.class))).thenReturn(AcceptTerms.builder().build());

        Boolean actual = applicationService.acceptTerms(findId, request);
        assertThat(actual).isTrue();

    }

    @Test
    void Should_ThrowException_When_RequestNotAllAcceptTermsOfApplication() {

        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("약관 1")
                .termsDetailUrl("https://abcdef.com/abc1")
                .build();

        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("약관 2")
                .termsDetailUrl("https://abcdef.com/abc2")
                .build();

        List<Long> acceptTerms = Arrays.asList(1L);


        ApplicationDTO.AcceptTerms request = ApplicationDTO.AcceptTerms.builder()
                .acceptTermsIds(acceptTerms)
                .build();

        Long findId = 1L;

        // Mocking Process
        when(applicationRepository.findById(findId)).thenReturn(
                Optional.ofNullable(Application.builder().build()));
        when(termsRepository.findAll(Sort.by(Direction.ASC, "termsId"))).thenReturn(Arrays.asList(entityA, entityB));

        Assertions.assertThrows(BaseException.class, () -> applicationService.acceptTerms(findId, request));

    }

    @Test
    void Should_ThrowException_When_RequestNotExistAcceptTermsOfApplication() {

        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("약관 1")
                .termsDetailUrl("https://abcdef.com/abc1")
                .build();

        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("약관 2")
                .termsDetailUrl("https://abcdef.com/abc2")
                .build();

        List<Long> acceptTerms = Arrays.asList(1L, 3L);


        ApplicationDTO.AcceptTerms request = ApplicationDTO.AcceptTerms.builder()
                .acceptTermsIds(acceptTerms)
                .build();

        Long findId = 1L;

        // Mocking Process
        when(applicationRepository.findById(findId)).thenReturn(
                Optional.ofNullable(Application.builder().build()));
        when(termsRepository.findAll(Sort.by(Direction.ASC, "termsId"))).thenReturn(Arrays.asList(entityA, entityB));

        Assertions.assertThrows(BaseException.class, () -> applicationService.acceptTerms(findId, request));
    }
}
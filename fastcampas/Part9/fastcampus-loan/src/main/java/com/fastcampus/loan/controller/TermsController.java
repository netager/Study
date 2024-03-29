package com.fastcampus.loan.controller;

import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.dto.TermsDTO.Request;
import com.fastcampus.loan.dto.TermsDTO.Response;
import com.fastcampus.loan.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/terms")
public class TermsController extends AbstractController {
    private final TermsService termsService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {

        return ok(termsService.create(request));
    }

    @GetMapping
    public ResponseDTO<List<Response>> getAll() {
        return ok(termsService.getAll());
    }
//
//    @PutMapping("/{applicationId}")
//    public ResponseDTO<Response> update(@PathVariable Long applicationId, @RequestBody Request request) {
//        return ok(applicationService.update(applicationId, request));
//    }
//
//    @DeleteMapping("/{applicationId}")
//    public ResponseDTO<Response> delete(@PathVariable Long applicationId) {
//        applicationService.delete(applicationId);
//        return ok();
//    }

}

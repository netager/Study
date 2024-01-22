package com.fastcampus.loan.controller;

import com.fastcampus.loan.dto.EntryDTO;
import com.fastcampus.loan.dto.EntryDTO.Response;
import com.fastcampus.loan.dto.EntryDTO.UpdateResponse;
import com.fastcampus.loan.dto.EntryDTO.Request;
import com.fastcampus.loan.dto.RepaymentDTO;
import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.repository.RepaymentRepository;
import com.fastcampus.loan.service.EntryService;
import com.fastcampus.loan.service.RepaymentService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/applications")
public class InternalController extends AbstractController {

    private final EntryService entryService;
    private final RepaymentService repaymentService;

    @PostMapping("{applicationId}/entries")
    public ResponseDTO<Response> create(@PathVariable Long applicationId, @RequestBody Request request) {
        System.out.println("[InternalController] request : " + request.getEntryAmount());
        return ok(entryService.create(applicationId, request));
    }

    @GetMapping("{applicationId}/entries")
    public ResponseDTO<Response> get(@PathVariable Long applicationId) {

        return ok(entryService.get(applicationId));
    }

    @PutMapping("/entries/{entryId}")
    public ResponseDTO<UpdateResponse> update(@PathVariable Long entryId, @RequestBody Request request) {

        return ok(entryService.update(entryId, request));
    }

    @DeleteMapping("/entries/{entryId}")
    public ResponseDTO<Void> update(@PathVariable Long entryId) {
        entryService.delete(entryId);
        return ok();

    }

    @PostMapping("{applicationId}/repayments")
    public ResponseDTO<RepaymentDTO.Response> create(@PathVariable Long applicationId, @RequestBody RepaymentDTO.Request request) {
        return ok(repaymentService.create(applicationId, request));
    }

    @GetMapping("{applicationId}/repayments")
    public ResponseDTO<List<RepaymentDTO.Response>> getPayments(@PathVariable Long applicationId) {

        return ok(repaymentService.get(applicationId));
    }

}
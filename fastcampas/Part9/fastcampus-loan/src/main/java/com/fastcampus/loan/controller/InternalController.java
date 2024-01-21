package com.fastcampus.loan.controller;

import com.fastcampus.loan.dto.EntryDTO.Response;
import com.fastcampus.loan.dto.EntryDTO.Request;
import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/applications")
public class InternalController extends AbstractController {

    private final EntryService entryService;

    @PostMapping("{applicationId}/entries")
    public ResponseDTO<Response> create(@PathVariable Long applicationId, @RequestBody Request request) {
        System.out.println("[InternalController] request : " + request.getEntryAmount());
        return ok(entryService.create(applicationId, request));
    }

    @GetMapping("{applicationId}/entries")
    public ResponseDTO<Response> get(@PathVariable Long applicationId) {
        return ok(entryService.get(applicationId));
    }
}

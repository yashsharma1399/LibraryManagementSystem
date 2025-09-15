package com.lms.lms.controller;

import com.lms.lms.entity.Copy;
import com.lms.lms.service.CopyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/copies")
public class CopyController {
    private final CopyService copies;
    public CopyController(CopyService copies) { this.copies = copies; }

    public record CreateCopyRequest(Long bookId, String barcode) {}

    @PostMapping
    public Copy create(@RequestBody CreateCopyRequest req) {
        return copies.create(req.bookId(), req.barcode());
    }
}

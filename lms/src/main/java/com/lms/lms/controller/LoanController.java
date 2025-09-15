// com/lms/lms/controller/LoanController.java
package com.lms.lms.controller;

import com.lms.lms.entity.Loan;
import com.lms.lms.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    public record CheckoutRequest(Long memberId, Long copyId) {}
    public record LoanResponse(
            Long id,
            Long memberId,
            Long copyId,
            LocalDate checkoutDate,
            LocalDate dueDate,
            LocalDate checkinDate,
            Long fineCents
    ) {}

    @PostMapping("/checkout")
    public ResponseEntity<LoanResponse> checkout(@RequestBody CheckoutRequest req) {
        Loan loan = loanService.checkout(req.memberId(), req.copyId());
        return ResponseEntity.ok(toDto(loan));
    }

    @PostMapping("/{loanId}/checkin")
    public ResponseEntity<LoanResponse> checkin(@PathVariable Long loanId) {
        Loan loan = loanService.checkin(loanId);
        return ResponseEntity.ok(toDto(loan));
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<LoanResponse> get(@PathVariable Long loanId) {
        Loan loan = loanService.get(loanId);
        return ResponseEntity.ok(toDto(loan));
    }

    private LoanResponse toDto(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getMember().getId(),
                loan.getCopy().getId(),
                loan.getCheckoutDate(),
                loan.getDueDate(),
                loan.getCheckinDate(),
                loan.getFineCents()
        );
    }
}

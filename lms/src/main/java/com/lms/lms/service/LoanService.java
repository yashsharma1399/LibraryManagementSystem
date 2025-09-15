package com.lms.lms.service;

import com.lms.lms.entity.*;
import com.lms.lms.repository.CopyRepository;
import com.lms.lms.repository.LoanRepository;
import com.lms.lms.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class LoanService {
    private static final int LOAN_DAYS = 14;
    private static final long FINE_PER_DAY_CENTS = 50; // $0.50/day (tweak later)

    private final LoanRepository loans;
    private final CopyRepository copies;
    private final MemberRepository members;

    public LoanService(LoanRepository loans, CopyRepository copies, MemberRepository members) {
        this.loans = loans; this.copies = copies; this.members = members;
    }

    @Transactional
    public Loan checkout(Long memberId, Long copyId) {
        Member m = members.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        Copy c = copies.findById(copyId).orElseThrow(() -> new IllegalArgumentException("Copy not found: " + copyId));
        if (c.getStatus() == CopyStatus.LOANED) throw new IllegalStateException("Copy already loaned");
        LocalDate today = LocalDate.now();

        Loan loan = new Loan();
        loan.setMember(m);
        loan.setCopy(c);
        loan.setCheckoutDate(today);
        loan.setDueDate(today.plusDays(LOAN_DAYS));
        loan = loans.save(loan);

        c.setStatus(CopyStatus.LOANED);
        copies.save(c);
        return loan;
    }

    @Transactional
    public Loan checkin(Long loanId) {
        Loan loan = loans.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));
        if (loan.getCheckinDate() != null) return loan; // already closed

        LocalDate today = LocalDate.now();
        loan.setCheckinDate(today);

        long overdueDays = Math.max(0, ChronoUnit.DAYS.between(loan.getDueDate(), today));
        long fine = overdueDays * FINE_PER_DAY_CENTS;
        loan.setFineCents(fine);
        loans.save(loan);

        Copy c = loan.getCopy();
        c.setStatus(CopyStatus.AVAILABLE);
        copies.save(c);
        return loan;
    }

    @Transactional(readOnly = true)
    public Loan get(Long id) {
        return loans.findById(id).orElseThrow(() -> new IllegalArgumentException("Loan not found: " + id));
    }
}

package com.lms.lms.service;

import com.lms.lms.entity.Copy;
import com.lms.lms.entity.CopyStatus;
import com.lms.lms.entity.Loan;
import com.lms.lms.entity.Member;
import com.lms.lms.repository.CopyRepository;
import com.lms.lms.repository.LoanRepository;
import com.lms.lms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class LoanService {

    @Value("${lms.loan.days:14}")
    private int loanDays;      // how many days a loan lasts

    @Value("${lms.fine.per-day-cents:50}")
    private long finePerDayCents;   // fine charged per overdue day (in cents)

    private final LoanRepository loans;
    private final CopyRepository copies;
    private final MemberRepository members;

    public LoanService(LoanRepository loans, CopyRepository copies, MemberRepository members) {
        this.loans = loans;
        this.copies = copies;
        this.members = members;
    }

    @Transactional
    public Loan checkout(Long memberId, Long copyId) {
        Member m = members.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        Copy c = copies.findById(copyId)
                .orElseThrow(() -> new IllegalArgumentException("Copy not found: " + copyId));

        if (c.getStatus() == CopyStatus.LOANED) {
            throw new IllegalStateException("Copy already loaned");
        }

        LocalDate today = LocalDate.now();

        Loan loan = new Loan();
        loan.setMember(m);
        loan.setCopy(c);
        loan.setCheckoutDate(today);
        loan.setDueDate(today.plusDays(loanDays));  // use configured loan length
        loan.setCheckinDate(null);
        loan.setFineCents(null);

        loan = loans.save(loan);

        c.setStatus(CopyStatus.LOANED);
        copies.save(c);

        return loan;
    }

    @Transactional
    public Loan checkin(Long loanId) {
        Loan loan = loans.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));

        if (loan.getCheckinDate() != null) {
            return loan; // already closed
        }

        LocalDate today = LocalDate.now();
        loan.setCheckinDate(today);

        long overdueDays = 0;
        if (loan.getDueDate() != null && today.isAfter(loan.getDueDate())) {
            overdueDays = ChronoUnit.DAYS.between(loan.getDueDate(), today);
        }

        long fine = overdueDays * finePerDayCents;
        loan.setFineCents(fine);

        // free the copy
        Copy c = loan.getCopy();
        c.setStatus(CopyStatus.AVAILABLE);
        copies.save(c);

        return loans.save(loan);
    }

    @Transactional(readOnly = true)
    public Loan get(Long id) {
        return loans.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + id));
    }
}

package com.lms.lms.repository;

import com.lms.lms.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> { }

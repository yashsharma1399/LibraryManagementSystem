package com.lms.lms.repository;

import com.lms.lms.entity.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CopyRepository extends JpaRepository<Copy, Long> {
    Optional<Copy> findByBarcode(String barcode);
}

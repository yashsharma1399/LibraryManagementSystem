package com.lms.lms.service;

import com.lms.lms.entity.Copy;
import com.lms.lms.entity.CopyStatus;
import com.lms.lms.entity.Book;
import com.lms.lms.repository.CopyRepository;
import com.lms.lms.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CopyService {
    private final CopyRepository copies;
    private final BookRepository books;

    public CopyService(CopyRepository copies, BookRepository books) {
        this.copies = copies; this.books = books;
    }

    @Transactional
    public Copy create(Long bookId, String barcode) {
        Book b = books.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        Copy c = new Copy();
        c.setBook(b);
        c.setBarcode(barcode);
        c.setStatus(CopyStatus.AVAILABLE);
        return copies.save(c);
    }
}

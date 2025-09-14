package com.lms.lms.service;

import com.lms.lms.entity.Book;
import com.lms.lms.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {
    private final BookRepository repo;
    public BookService(BookRepository repo) { this.repo = repo; }

    @Transactional
    public Book create(Book b) { return repo.save(b); }

    public List<Book> list() { return repo.findAll(); }

    public Book get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
    }

    @Transactional
    public Book update(Long id, Book incoming) {
        Book existing = get(id);
        if (incoming.getIsbn() != null) existing.setIsbn(incoming.getIsbn());
        if (incoming.getTitle() != null) existing.setTitle(incoming.getTitle());
        if (incoming.getAuthor() != null) existing.setAuthor(incoming.getAuthor());
        if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Book not found: " + id);
        repo.deleteById(id);
    }
}

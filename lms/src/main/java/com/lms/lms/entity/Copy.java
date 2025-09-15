package com.lms.lms.entity;

import jakarta.persistence.*;

@Entity @Table(name = "copies")
public class Copy {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Physical identifier (sticker on the book). */
    @Column(unique = true, nullable = false)
    private String barcode;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Book book;

    @Enumerated(EnumType.STRING)
    private CopyStatus status = CopyStatus.AVAILABLE;

    // getters/setters
    public Long getId() { return id; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public CopyStatus getStatus() { return status; }
    public void setStatus(CopyStatus status) { this.status = status; }
}

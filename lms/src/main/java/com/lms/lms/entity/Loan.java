package com.lms.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Table(name = "loans")
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    private Copy copy;

    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    private Member member;

    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate checkinDate;

    /** store cents to avoid floating point */
    private Long fineCents;

    // getters/setters
    public Long getId() { return id; }
    public Copy getCopy() { return copy; }
    public void setCopy(Copy copy) { this.copy = copy; }
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
    public void setCheckoutDate(LocalDate checkoutDate) { this.checkoutDate = checkoutDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getCheckinDate() { return checkinDate; }
    public void setCheckinDate(LocalDate checkinDate) { this.checkinDate = checkinDate; }
    public Long getFineCents() { return fineCents; }
    public void setFineCents(Long fineCents) { this.fineCents = fineCents; }
}

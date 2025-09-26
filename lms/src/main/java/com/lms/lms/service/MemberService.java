package com.lms.lms.service;

import com.lms.lms.entity.Member;
import com.lms.lms.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository members;
    public MemberService(MemberRepository members) { this.members = members; }

    @Transactional
    public Member create(String name, String email) {
        Member m = new Member();
        m.setName(name); m.setEmail(email);
        return members.save(m);
    }

    @Transactional(readOnly = true)
    public List<Member> list() {
        return members.findAll();   // simple; you can add sort later if you want
    }

    // optional (nice to have)
    @Transactional(readOnly = true)
    public Member get(Long id) {
        return members.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
    }

}

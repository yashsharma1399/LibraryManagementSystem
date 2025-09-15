package com.lms.lms.service;

import com.lms.lms.entity.Member;
import com.lms.lms.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

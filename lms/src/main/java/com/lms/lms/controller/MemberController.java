package com.lms.lms.controller;

import com.lms.lms.entity.Member;
import com.lms.lms.service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService members;
    public MemberController(MemberService members) { this.members = members; }

    public record CreateMemberRequest(String name, String email) {}

    @PostMapping
    public Member create(@RequestBody CreateMemberRequest req) {
        return members.create(req.name(), req.email());
    }
}

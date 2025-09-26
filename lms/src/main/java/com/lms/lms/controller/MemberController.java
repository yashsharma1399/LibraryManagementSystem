package com.lms.lms.controller;

import com.lms.lms.entity.Member;
import com.lms.lms.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService members;
    public MemberController(MemberService members) {
        this.members = members;
    }

    // simple Request/Response DTOs to keep responses clean
    public record CreateMemberRequest(String name, String email) {}
    public record MemberResponse(Long id, String name, String email) {}

    // POST /api/members  -> create a member
    @PostMapping
    public ResponseEntity<MemberResponse> create(@RequestBody CreateMemberRequest req) {
        Member created = members.create(req.name(), req.email());
        return ResponseEntity.ok(toDto(created));
    }


    // GET /api/members  -> list all members
    @GetMapping
    public ResponseEntity<List<MemberResponse>> list() {
        List<MemberResponse> result = members.list()
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    // GET /api/members/{id}  -> get one member by id
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> get(@PathVariable Long id) {
        Member m = members.get(id);
        return ResponseEntity.ok(toDto(m));
    }

    // mapper
    private MemberResponse toDto(Member m) {
        return new MemberResponse(m.getId(), m.getName(), m.getEmail());
    }


}

package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.project.model.Member;
import com.example.demo.services.MemberService;

@RestController
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @PostMapping("/members")
    public Member addMember(@RequestBody Member member) {
        return service.addMember(member);
    }

    @GetMapping("/members")
    public List<Member> getMembers() {
        return service.getMembers();
    }

    @GetMapping("/members/{id}")
    public Member getMember(@PathVariable Long id) {
        return service.getMember(id);
    }

    @PutMapping("/members/{id}")
    public Member updateMember(@PathVariable Long id, @RequestBody Member updatedMember) {
        return service.updateMember(id, updatedMember);
    }

    @DeleteMapping("/members/{id}")
    public String deleteMember(@PathVariable Long id) {
        service.deleteMember(id);
        return "Member deleted successfully";
    }
}

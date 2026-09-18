package com.example.demo.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.project.model.Member;
import com.example.demo.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public Member addMember(Member member) {
        return repository.save(member);
    }

    public List<Member> getMembers() {
        return repository.findAll();
    }

    public Member getMember(Long id) {
        return findMember(id);
    }

    public Member updateMember(Long id, Member updatedMember) {
        Member existingMember = findMember(id);
        existingMember.setMail(updatedMember.getMail());
        existingMember.setName(updatedMember.getName());
        existingMember.setPhoneNumber(updatedMember.getPhoneNumber());
        return repository.save(existingMember);
    }

    public void deleteMember(Long id) {
        repository.delete(findMember(id));
    }

    private Member findMember(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
    }
}

package com.project.api.library.controller;

import com.project.api.library.dto.MemberDTO;
import com.project.api.library.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<Page<MemberDTO>> findAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size ){
        Page<MemberDTO> MemberDTO = memberService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok().body(MemberDTO);

    }

    @GetMapping("/member/{id}")
    public Optional<MemberDTO> findById(@PathVariable("id") Long id){
        return memberService.findById(id);
    }

    @PostMapping("/member")
    public ResponseEntity<MemberDTO> save(@Valid  @RequestBody MemberDTO memberDTO) {
        MemberDTO savedMemberDTO = memberService.save(memberDTO);
        return ResponseEntity.ok().body(savedMemberDTO);

    }

    @PutMapping("/member/{id}")
    public ResponseEntity<MemberDTO> update(@PathVariable("id") Long id,@Valid  @RequestBody MemberDTO memberDTO){
        memberDTO = memberService.update(id, memberDTO);
        return ResponseEntity.ok().body(memberDTO);
    }

    @DeleteMapping("/member/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }


}

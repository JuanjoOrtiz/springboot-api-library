package com.springboot.api.library.services.impl;

import com.springboot.api.library.dtos.MemberRequestDTO;
import com.springboot.api.library.dtos.MemberResponseDTO;
import com.springboot.api.library.exceptions.ResourceNotFoundException;
import com.springboot.api.library.mappers.MemberMapper;
import com.springboot.api.library.repositories.MemberRepository;
import com.springboot.api.library.services.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;


    @Override
    @Transactional(readOnly = true)
    public Page<MemberResponseDTO> findAll(Pageable pageable) {
        log.debug("Fetching all members with pagination: {}", pageable);
    return memberRepository.findAll(pageable).map(memberMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberResponseDTO> findById(Long id) {
        log.debug("Searching member by Id: {}", id);
        return memberRepository.findById(id).map(memberMapper::toDTO);
    }

    @Override
    @Transactional
    public MemberResponseDTO create(MemberRequestDTO memberRequestDTO) {
        log.debug("Creating new member with data: {}", memberRequestDTO);

        var member = memberMapper.toEntity(memberRequestDTO);
        member = memberRepository.save(member);

        log.info("Created new member with id: {}", member.getId());
        return memberMapper.toDTO(member);
    }

    @Override
    @Transactional
    public MemberResponseDTO update(MemberRequestDTO memberRequestDTO, Long id) {
        log.debug("Updating member with id {} with data: {}", id, memberRequestDTO);

        var existingMember = memberRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Member not found with id: {}", id);
                    return new ResourceNotFoundException("Member not found with id: " + id);
                });

        memberMapper.updateEntityFromDto(memberRequestDTO, existingMember);
        var updatedMember = memberRepository.save(existingMember);

        log.info("Updated member with id: {}", id);
        return memberMapper.toDTO(updatedMember);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        log.debug("Deleting member with id: {}", id);

        if (!memberRepository.existsById(id)) {
            log.error("Attempt to delete non-existent member with id: {}", id);
            throw new RuntimeException("Bember not found with id: " + id);
        }

        memberRepository.deleteById(id);
        log.info("Deleted member with id: {}", id);

        return false;
    }
}

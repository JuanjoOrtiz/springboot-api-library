package com.project.api.library.service;


import com.project.api.library.dto.MemberDTO;
import com.project.api.library.entity.Member;
import com.project.api.library.exceptions.ResourceNotFoundException;
import com.project.api.library.exceptions.SaveErrorException;
import com.project.api.library.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {

    private static final String NOT_FOUND = " not found!";

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<MemberDTO> findAll(Pageable pageable) {
        try {
            Page<Member> memberPage = memberRepository.findAll(pageable);
            List<MemberDTO> memberDTOS = memberPage.getContent().stream()
                    .map(entity -> modelMapper.map(entity, MemberDTO.class))
                    .toList();

            return new PageImpl<>(memberDTOS, memberPage.getPageable(), memberPage.getTotalElements());

        } catch (ResourceNotFoundException e) {
            log.info(e.getMessage(), e);
            throw new ResourceNotFoundException("Members " + NOT_FOUND);
        }
    }

    @Override
    public Optional<MemberDTO> findById(Long id) {
        return Optional.ofNullable(memberRepository.findById(id)
                .map(entity -> modelMapper.map(entity, MemberDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("¡Member with " + id + NOT_FOUND)));
    }

    @Override
    public MemberDTO save(MemberDTO memberDTO) {
        try {
            Member member = modelMapper.map(memberDTO, Member.class);
            member = memberRepository.save(member);
            return modelMapper.map(member, MemberDTO.class);
        } catch (SaveErrorException e) {
            throw new SaveErrorException("Failed to save the book: " + e.getMessage());
        }
    }

    @Override
    public MemberDTO update(Long id, MemberDTO memberDTO) {
        try {

            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Member with " + id + " not found"));

            modelMapper.typeMap(MemberDTO.class, Member.class).addMappings(mapper -> mapper.skip(Member::setId));

            modelMapper.map(memberDTO, member);

            memberRepository.save(member);

            return modelMapper.map(member, MemberDTO.class);
        } catch (SaveErrorException e) {
            throw new SaveErrorException("Failed to update the member: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member " + id + " not found"));
        memberRepository.delete(member);
    }
}

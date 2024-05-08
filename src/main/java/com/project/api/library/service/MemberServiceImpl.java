package com.project.api.library.service;


import com.project.api.library.dto.MemberDTO;
import com.project.api.library.entity.Member;
import com.project.api.library.exceptions.GeneralServiceException;
import com.project.api.library.exceptions.NoResourceFoundException;
import com.project.api.library.exceptions.ValidateServiceException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<MemberDTO> findAll(Pageable pageable) {

        try {
            Page<Member> memberPage = memberRepository.findAll(pageable);
            List<MemberDTO> memberDTOS = memberPage.getContent().stream()
                    .map(entity -> modelMapper.map(entity, MemberDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(memberDTOS, memberPage.getPageable(), memberPage.getTotalElements());

        }catch (ValidateServiceException | NoResourceFoundException e){
            log.info(e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<MemberDTO> findById(Long id) {
        Optional<MemberDTO> memberDTO = memberRepository.findById(id).map(entity -> modelMapper.map(entity, MemberDTO.class));

        if(memberDTO.isPresent()){
            return memberDTO;
        }
        throw new NoResourceFoundException("¡Member with "+ id +" not found!");
    }

    @Override
    public MemberDTO save(MemberDTO MemberDTO) {

        // Convertir DTO a entidad
        Member  member = modelMapper.map(MemberDTO, Member.class);

        // Guardar entidad en la base de datos
        member = memberRepository.save(member); // Aquí estaba el error

        // Convertir entidad guardada a DTO
        MemberDTO savedMemberDTO = modelMapper.map(member, MemberDTO.class);


        return savedMemberDTO;
    }

    @Override
    public MemberDTO update(Long id, MemberDTO memberDTO) {

        // Buscar el libro en la base de datos
        Member  member= memberRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("Member with "+ id +" not found"));

        // Configurar ModelMapper para ignorar el campo 'id'
        modelMapper.typeMap(MemberDTO.class, Member.class).addMappings(mapper -> mapper.skip(Member::setId));

        // Actualizar el libro con los datos del DTO
        modelMapper.map(memberDTO, member);

        // Guardar el libro actualizado en la base de datos
        memberRepository.save(member);

        // Convertir el libro actualizado a DTO
        MemberDTO updatedMemberDTO = modelMapper.map(member, MemberDTO.class);

        return updatedMemberDTO;
    }

    @Override
    public void delete(Long id) {
       Member member =  memberRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("Member "+ id +" not found"));
        memberRepository.delete(member);
    }
}

package com.project.api.library.service;

import com.project.api.library.dto.LoanDTO;
import com.project.api.library.entity.Loan;
import com.project.api.library.exceptions.ResourceNotFoundException;
import com.project.api.library.repository.LoanRepository;
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
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<LoanDTO> findAll(Pageable pageable) {
     //   try {
            Page<Loan> loanPage = loanRepository.findAll(pageable);
            List<LoanDTO> loanDTOs = loanPage.getContent().stream()
                    .map(entity -> {
                        LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
                        loanDTO.setBook(entity.getBook().getTitle());
                        return loanDTO;
                    })
                    .collect(Collectors.toList());

            return new PageImpl<>(loanDTOs, loanPage.getPageable(), loanPage.getTotalElements());

       /* }catch (ValidateServiceException | NoResourceFoundException e){
            log.info(e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }*/
    }

    @Override
    public Optional<LoanDTO> findById(Long id) {
        Optional<LoanDTO> loanDTO = loanRepository.findById(id).map(entity -> modelMapper.map(entity, LoanDTO.class));

        if(loanDTO.isPresent()){
            return loanDTO;
        }
        throw new ResourceNotFoundException("¡Book width "+ id +" not found!");
    }

    @Override
    public LoanDTO save(LoanDTO loanDTO) {
        return null;
    }

    @Override
    public LoanDTO update(Long id, LoanDTO loanDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}

package com.project.api.library.service;

import com.project.api.library.dto.LoanDTO;
import com.project.api.library.entity.Loan;
import com.project.api.library.exceptions.GeneralServiceException;
import com.project.api.library.exceptions.NoResourceFoundException;
import com.project.api.library.exceptions.ValidateServiceException;
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
        try {
            Page<Loan> loanPage = loanRepository.findAll(pageable);
            List<LoanDTO> loanDTOs = loanPage.getContent().stream()
                    .map(entity -> {
                        LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
                        loanDTO.setBook(entity.getBook().getTitle());
                        return loanDTO;
                    })
                    .collect(Collectors.toList());

            return new PageImpl<>(loanDTOs, loanPage.getPageable(), loanPage.getTotalElements());

        }catch (ValidateServiceException | NoResourceFoundException e){
            log.info(e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<LoanDTO> findById(Long id) {
        Optional<LoanDTO> loanDTO = loanRepository.findById(id)
                .map(entity -> {
                    LoanDTO dto = modelMapper.map(entity, LoanDTO.class);
                    dto.setBook(entity.getBook().getTitle());
                    return dto;
                });


        if(loanDTO.isPresent()){

            return loanDTO;
        }
        throw new NoResourceFoundException("¡Loan with "+ id +" not found!");
    }

    @Override
    public LoanDTO save(LoanDTO loanDTO) {
        // Convertir DTO a entidad
       Loan loan = modelMapper.map(loanDTO, Loan.class);

        // Guardar entidad en la base de datos
        loan = loanRepository.save(loan); // Aquí estaba el error

        // Convertir entidad guardada a DTO
        LoanDTO savedLoanDTO = modelMapper.map(loan, LoanDTO.class);


        return savedLoanDTO;
    }

    @Override
    public LoanDTO update(Long id, LoanDTO loanDTO) {

        // Buscar el pretamo en la base de datos
        Loan loan= loanRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("Member with "+ id +" not found"));

        // Configurar ModelMapper para ignorar el campo 'id'
        modelMapper.typeMap(LoanDTO.class, Loan.class).addMappings(mapper -> mapper.skip(Loan::setId));

        // Actualizar el libro con los datos del DTO
        modelMapper.map(loanDTO, loan);

        // Guardar el libro actualizado en la base de datos
        loanRepository.save(loan);

        // Convertir el libro actualizado a DTO
       LoanDTO updatedLoanDTO = modelMapper.map(loan, LoanDTO.class);

        return updatedLoanDTO;


    }

    @Override
    public void delete(Long id) {
        Loan loan =  loanRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("Loan "+ id +" not found"));
        loanRepository.delete(loan);
    }
}

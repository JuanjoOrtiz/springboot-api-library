package com.project.api.library.service;

import com.project.api.library.dto.LoanDTO;
import com.project.api.library.entity.Book;
import com.project.api.library.entity.Loan;
import com.project.api.library.entity.Member;
import com.project.api.library.exceptions.ResourceNotFoundException;
import com.project.api.library.repository.BookRepository;
import com.project.api.library.repository.LoanRepository;
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
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<LoanDTO> findAll(Pageable pageable) {

            Page<Loan> loanPage = loanRepository.findAll(pageable);
            List<LoanDTO> loanDTOs = loanPage.getContent().stream()
                    .map(entity -> {
                        LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);

                        return loanDTO;
                    })
                    .collect(Collectors.toList());

            return new PageImpl<>(loanDTOs, loanPage.getPageable(), loanPage.getTotalElements());
    }

    @Override
    public Optional<LoanDTO> findById(Long id) {
        Optional<LoanDTO> loanDTO = loanRepository.findById(id)
                .map(entity ->  modelMapper.map(entity, LoanDTO.class));


                    if(loanDTO.isPresent() ){
                        return loanDTO;
                    }

                throw new ResourceNotFoundException("¡Loan with "+ id +" not found!");

    }

    @Override
    public LoanDTO save(LoanDTO loanDTO) {
        // Convertir DTO a entidad
        Loan loan = modelMapper.map(loanDTO, Loan.class);

        // Rellenar member y book
      Member member = memberRepository.findByMemberShipNumber(loanDTO.getMemberShipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un miembro con el número de socio " + loanDTO.getMemberShipNumber()));
        Book book = bookRepository.findByTitle(loanDTO.getBook())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un libro con el titulo " + loanDTO.getBook()));

        loan.setMember(member);
        loan.setBook(book);

        // Comprobar si ya existe un préstamo para el libro
        Loan existingLoan = loanRepository.findByBookTitle(loanDTO.getBook());
        if (existingLoan != null) {
            throw new IllegalStateException("Ya existe un préstamo para el libro con el título " + book.getTitle());
        }

        // Guardar entidad en la base de datos
        loan = loanRepository.save(loan);

        // Convertir entidad guardada a DTO
        LoanDTO savedLoanDTO = modelMapper.map(loan, LoanDTO.class);

        return savedLoanDTO;
    }

    @Override
    public LoanDTO update(Long id, LoanDTO loanDTO) {
        // Buscar el préstamo en la base de datos
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo con id " + id + " no encontrado"));

        // Configurar ModelMapper para ignorar el campo 'id'
        modelMapper.typeMap(LoanDTO.class, Loan.class).addMappings(mapper -> mapper.skip(Loan::setId));

        // Rellenar member y book
        Member member = memberRepository.findByMemberShipNumber(loanDTO.getMemberShipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un miembro con el número de socio " + loanDTO.getMemberShipNumber()));
        Book book = bookRepository.findByTitle(loanDTO.getBook())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un libro con el titulo " + loanDTO.getBook()));

        loan.setMember(member);
        loan.setBook(book);

        // Actualizar el préstamo con los datos del DTO
        modelMapper.map(loanDTO, loan);

        // Comprobar si ya existe un préstamo para el libro
        Loan existingLoan = loanRepository.findByBookTitle(book.getTitle());
        if (existingLoan != null && !existingLoan.getId().equals(id)) {
            throw new IllegalStateException("Ya existe un préstamo para el libro con el título " + book.getTitle());
        }

        // Guardar el préstamo actualizado en la base de datos
        loanRepository.save(loan);

        // Convertir el préstamo actualizado a DTO
        LoanDTO updatedLoanDTO = modelMapper.map(loan, LoanDTO.class);

        return updatedLoanDTO;

    }

    @Override
    public void delete(Long id) {
        Loan loan =  loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan "+ id +" not found"));
        loan.setBook(null);
        loan.setMember(null);
        loanRepository.save(loan);
        loanRepository.delete(loan);
    }
}

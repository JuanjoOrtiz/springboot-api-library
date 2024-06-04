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

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LoanServiceImpl implements LoanService {

    private static final String NOT_FOUND = " not found!";

    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<LoanDTO> findAll(Pageable pageable) {
        try {
            Page<Loan> loanPage = loanRepository.findAll(pageable);
            List<LoanDTO> loanDTOs = loanPage.getContent().stream()
                    .map(entity -> {
                        LoanDTO loans = modelMapper.map(entity, LoanDTO.class);
                        return loans;
                    }).toList();

            return new PageImpl<>(loanDTOs, loanPage.getPageable(), loanPage.getTotalElements());
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ResourceNotFoundException("Loans " + NOT_FOUND);
        }
    }

   @Override
    public Optional<LoanDTO> findById(Long id) {
        return Optional.ofNullable(loanRepository.findById(id)
                .map(loan -> modelMapper.map(loan, LoanDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("¡Loan with id" + id + NOT_FOUND)));

    }
    @Override
    public LoanDTO save(LoanDTO loanDTO) {
        // Convertir DTO a entidad
        Loan loan = modelMapper.map(loanDTO, Loan.class);

        // Almacenar el número de socio en una variable final
        final String memberShipNumber = loan.getMember().getMemberShipNumber();
        final String title = loan.getBook() != null ? loan.getBook().getTitle() : null;

        // Verificar si el miembro existe
        Member member = memberRepository.findByMemberShipNumber(memberShipNumber)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un miembro con el número de socio " + memberShipNumber));
        loan.setMember(member);

        // Verificar si el libro existe y si ya está prestado
        if (title != null && !title.isEmpty()) {
            Book book = bookRepository.findByTitle(title)
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontró un libro con el titulo " + title));
            Optional<Loan> existingLoan = loanRepository.findByBookTitle(title);
            if (existingLoan.isPresent()) {
                throw new IllegalStateException("El libro con el título " + title + " ya está prestado");
            }
            loan.setBook(book);
        } else {
            throw new IllegalArgumentException("El título del libro en el préstamo no puede ser null o vacío");
        }

        // Guardar el préstamo
        loan = loanRepository.save(loan);

        // Convertir entidad a DTO y devolver
        return modelMapper.map(loan, LoanDTO.class);
    }


    @Override
    public LoanDTO update(Long id, LoanDTO loanDTO) {
        // Buscar el préstamo en la base de datos
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo con id " + id + " no encontrado"));

        // Configurar ModelMapper para ignorar el campo 'id'
        modelMapper.typeMap(LoanDTO.class, Loan.class).addMappings(mapper -> mapper.skip(Loan::setId));

        // Verificar si el miembro existe
        Member member = memberRepository.findByMemberShipNumber(loanDTO.getMemberShipNumber())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un miembro con el número de socio " + loanDTO.getMemberShipNumber()));
        loan.setMember(member);

        // Verificar si el libro existe y si ya está prestado
        if (loanDTO.getBookTitle() != null && !loanDTO.getBookTitle().isEmpty()) {
            Book book = bookRepository.findByTitle(loanDTO.getBookTitle())
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontró un libro con el titulo " + loanDTO.getBookTitle()));
            Optional<Loan> existingLoan = loanRepository.findByBookTitle(loanDTO.getBookTitle());
            if (existingLoan.isPresent() && !existingLoan.get().getId().equals(id)) {
                throw new IllegalStateException("El libro con el título " + loanDTO.getBookTitle() + " ya está prestado");
            }
            loan.setBook(book);
        } else {
            throw new IllegalArgumentException("El título del libro en el préstamo no puede ser null o vacío");
        }

        // Actualizar el préstamo con los datos del DTO
        modelMapper.map(loanDTO, loan);

        // Guardar el préstamo
        loanRepository.save(loan);

        // Convertir entidad a DTO y devolver
        return modelMapper.map(loan, LoanDTO.class);
    }



    @Override
    public void delete(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan " + id + " not found"));
        loan.setBook(null);
        loan.setMember(null);
        loanRepository.save(loan);
        loanRepository.delete(loan);
    }
}

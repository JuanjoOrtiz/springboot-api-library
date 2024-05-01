package com.project.api.library.service;


import com.project.api.library.dto.BookDTO;

import com.project.api.library.entity.Book;
import com.project.api.library.exceptions.GeneralServiceException;
import com.project.api.library.exceptions.NoResourceFoundException;
import com.project.api.library.exceptions.ValidateServiceException;
import com.project.api.library.repository.BookRepository;
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
public class BookServiceImpl implements BookService{


    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public Page<BookDTO> findAll(Pageable pageable) {
        try {
            Page<Book> bookPage = bookRepository.findAll(pageable);
            List<BookDTO> bookDTOs = bookPage.getContent().stream()
                    .map(entity -> modelMapper.map(entity, BookDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(bookDTOs, pageable, bookPage.getTotalElements());

        }catch (ValidateServiceException | NoResourceFoundException e){
            log.info(e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }


    }

    @Override
    public Optional<BookDTO> findById(Long id) {
Optional<BookDTO> bookDTO = bookRepository.findById(id).map(entity -> modelMapper.map(entity, BookDTO.class));

        if(bookDTO.isPresent()){
            return bookDTO;
        }
       throw new NoResourceFoundException("¡Book width "+ id +" not found!");
    }

   @Override
    public BookDTO save(BookDTO bookDTO) {

       // Convertir DTO a entidad
       Book book = modelMapper.map(bookDTO, Book.class);

       // Guardar entidad en la base de datos
       book = bookRepository.save(book); // Aquí estaba el error

       // Convertir entidad guardada a DTO
       BookDTO savedBookDTO = modelMapper.map(book, BookDTO.class);


       return savedBookDTO;
    }

    @Override
    public BookDTO update(Long id, BookDTO bookDTO) {
        // Buscar el libro en la base de datos
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("Book "+ id +" not found"));

        // Configurar ModelMapper para ignorar el campo 'id'
        modelMapper.typeMap(BookDTO.class, Book.class).addMappings(mapper -> mapper.skip(Book::setId));

        // Actualizar el libro con los datos del DTO
        modelMapper.map(bookDTO, book);

        // Guardar el libro actualizado en la base de datos
        bookRepository.save(book);

        // Convertir el libro actualizado a DTO
        BookDTO updatedBookDTO = modelMapper.map(book, BookDTO.class);

        return updatedBookDTO;
    }

  @Override
    public void delete(Long id) {
        Book book =  bookRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("Book "+ id +" not found"));
        bookRepository.delete(book);
    }
}

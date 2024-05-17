package com.project.api.library.service;


import com.project.api.library.dto.*;
import com.project.api.library.entity.*;
import com.project.api.library.exceptions.ResourceNotFoundException;
import com.project.api.library.repository.*;
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
    private final AuthorRepository authorRepository;
    private final GenderRepository genderRepository;
    private final PublisherRepository publisherRepository;
    private final ShelfRepository shelfRepository;
    private final ModelMapper modelMapper;

    public Page<BookDTO> findAll(Pageable pageable) {
        try {
            Page<Book> bookPage = bookRepository.findAll(pageable);
            List<BookDTO> bookDTOs = bookPage.getContent().stream()
                    .filter(entity -> {
                        if (entity.getAuthor() == null) {
                            log.error("Author with id "+ entity.getId()+" not found! ");
                            return false;
                        }
                        if (entity.getGender() == null) {
                            log.error("Gender with id "+ entity.getId()+" not found! ");
                            return false;
                        }
                        if (entity.getPublisher() == null) {
                            log.error("Publisher with id "+ entity.getId()+" not found! ");
                            return false;
                        }
                        if (entity.getShelf() == null) {
                            log.error("Shelf with id "+ entity.getId()+" not found! ");
                            return false;
                        }
                        return true;
                    })
                    .map(entity -> {
                        BookDTO dto = modelMapper.map(entity, BookDTO.class);
                        dto.setAuthor(modelMapper.map(entity.getAuthor(), AuthorDTO.class));
                        dto.setGender(modelMapper.map(entity.getGender(), GenderDTO.class));
                        dto.setPublisher(modelMapper.map(entity.getPublisher(), PublisherDTO.class));
                        dto.setShelf(modelMapper.map(entity.getShelf(), ShelfDTO.class));
                        return dto;
                    })
                    .collect(Collectors.toList());

            return new PageImpl<>(bookDTOs, bookPage.getPageable(), bookPage.getTotalElements());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
                throw new ResourceNotFoundException("Books ",e);
        }

    }

    @Override
    public Optional<BookDTO> findById(Long id) {
Optional<BookDTO> bookDTO = bookRepository.findById(id).map(entity -> modelMapper.map(entity, BookDTO.class));

        if(bookDTO.isPresent()){
            return bookDTO;
        }
       throw new ResourceNotFoundException("¡Book with "+ id +" not found!");
    }

    @Override
    public BookDTO save(BookDTO bookDTO) {
        // Convertir DTO a entidad
        Book book = modelMapper.map(bookDTO, Book.class);

        // Si el libro tiene un autor, asegúrate de que también se guarde
        if (book.getAuthor() != null && book.getAuthor().getName() != null) {
            Author existingAuthor = authorRepository.findByName(book.getAuthor().getName());
            if (existingAuthor == null) {
                authorRepository.save(book.getAuthor());
            } else {
                book.setAuthor(existingAuthor);
            }
        }


        if (book.getGender() != null && book.getGender().getName() != null) {
            Gender existingGender = genderRepository.findByName(book.getGender().getName());
            if (existingGender == null) {
                genderRepository.save(book.getGender());
            } else {
                book.setGender(existingGender);
            }
        }

        if (book.getPublisher() != null && book.getPublisher().getName() != null) {
            Publisher existingPublisher =  publisherRepository.findByName(book.getPublisher().getName());
            if (existingPublisher == null) {
                publisherRepository.save(book.getPublisher());
            } else {
                book.setPublisher(existingPublisher);
            }
        }
        // Si el libro tiene una estantería, asegúrate de que también se guarde
        if (book.getShelf() != null && book.getShelf().getCode() != null) {
            Shelf existingShelf = shelfRepository.findByCode(book.getShelf().getCode());
            if (existingShelf == null) {
                shelfRepository.save(book.getShelf());
            } else {
                book.setShelf(existingShelf);
            }
        }
        // Guardar entidad en la base de datos
        book = bookRepository.save(book); // save the book entity

        // Convertir la entidad de libro devuelta a DTO
        BookDTO bookDTOUpdated = modelMapper.map(book, BookDTO.class);

        return bookDTOUpdated; // return the updated DTO
    }

    @Override
    public BookDTO update(Long id, BookDTO bookDTO) {
        // Configurar ModelMapper para ignorar el campo 'id'
        modelMapper.typeMap(BookDTO.class, Book.class).addMappings(mapper -> {
            mapper.skip(Book::setId);
            mapper.skip(Book::setAuthor);
            mapper.skip(Book::setGender);
            mapper.skip(Book::setPublisher);
            mapper.skip(Book::setShelf);
        });

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book "+ id +" not found"));

        Author author = authorRepository.findByName(bookDTO.getAuthor().getName());
        if (author == null) {
            Author newAuthor = new Author();
            newAuthor.setName(bookDTO.getAuthor().getName());
            author = authorRepository.save(newAuthor);
        }
        book.setAuthor(author);



        Gender gender = genderRepository.findByName(bookDTO.getGender().getName());
        if (gender == null) {
            Gender newGender = new Gender();
            newGender.setName(bookDTO.getGender().getName());
            gender = genderRepository.save(newGender);
        }
        book.setGender(gender);

        Publisher publisher = publisherRepository.findByName(bookDTO.getPublisher().getName());
        if (publisher == null) {
            Publisher newPublisher = new Publisher();
            newPublisher.setName(bookDTO.getPublisher().getName());
            publisher = publisherRepository.save(newPublisher);
        }
        book.setPublisher(publisher);


        Shelf shelf = shelfRepository.findByCode(bookDTO.getShelf().getCode());
        if (shelf == null) {
            Shelf newShelf = new Shelf();
            newShelf.setCode(bookDTO.getShelf().getCode());
            shelf = shelfRepository.save(newShelf);
        }
        book.setShelf(shelf);


        // Guardar el libro actualizado en la base de datos
        bookRepository.save(book);

        // Convertir el libro actualizado a DTO
        BookDTO updatedBookDTO = modelMapper.map(book, BookDTO.class);

        return updatedBookDTO;
    }


    @Override
    public void delete(Long id) {
        Book book =  bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book "+ id +" not found")); // Si el libro no se encuentra, lanza una excepción
        book.setAuthor(null);
        book.setGender(null);
        book.setPublisher(null);
        book.setShelf(null);
        bookRepository.save(book); // Guarda el libro con el author_id establecido a null

        bookRepository.delete(book); // Si el libro se encuentra, lo elimina del repositorio
    }
}

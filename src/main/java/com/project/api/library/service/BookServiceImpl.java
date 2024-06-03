package com.project.api.library.service;


import com.project.api.library.dto.*;
import com.project.api.library.entity.*;
import com.project.api.library.exceptions.ResourceNotFoundException;
import com.project.api.library.exceptions.SaveErrorException;
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


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookServiceImpl implements BookService {

    private static final String NOT_FOUND = " not found!";


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
                            throw new ResourceNotFoundException("Author with id " + entity.getId() + NOT_FOUND);
                        }
                        if (entity.getGender() == null) {
                            throw new ResourceNotFoundException("Gender with id " + entity.getId() + NOT_FOUND);
                        }
                        if (entity.getPublisher() == null) {
                            throw new ResourceNotFoundException("Publisher with id " + entity.getId() + NOT_FOUND);
                        }
                        if (entity.getShelf() == null) {
                            throw new ResourceNotFoundException("Shelf with id " + entity.getId() + NOT_FOUND);
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
                    }).toList();

            return new PageImpl<>(bookDTOs, pageable, bookPage.getTotalElements());

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ResourceNotFoundException("Books "+NOT_FOUND);
        }
    }


    @Override
    public Optional<BookDTO> findById(Long id) {
        return Optional.ofNullable(bookRepository.findById(id)
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("¡Book with " + id + NOT_FOUND)));
    }

    @Override
    public BookDTO save(BookDTO bookDTO) {
        try {
            Book book = modelMapper.map(bookDTO, Book.class);

            book.setAuthor(getExistingAuthor(book.getAuthor()));
            book.setGender(getExistingGender(book.getGender()));
            book.setPublisher(getExistingPublisher(book.getPublisher()));
            book.setShelf(getExistingShelf(book.getShelf()));

            book = bookRepository.save(book);

            return modelMapper.map(book, BookDTO.class);
        }catch (Exception e){
            throw new SaveErrorException("Failed to save the book: " + e.getMessage());
        }
    }

    @Override
    public BookDTO update(Long id, BookDTO bookDTO) {
        try {
            // Configurar ModelMapper para ignorar el campo 'id'
            modelMapper.typeMap(BookDTO.class, Book.class).addMappings(mapper -> {
                mapper.skip(Book::setId);
                mapper.skip(Book::setAuthor);
                mapper.skip(Book::setGender);
                mapper.skip(Book::setPublisher);
                mapper.skip(Book::setShelf);
            });

            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Book " + id + " not found"));

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

            bookRepository.save(book);

            return modelMapper.map(book, BookDTO.class);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SaveErrorException("Failed to update the book: " + e.getMessage());
        }
    }


    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book " + id + " not found")); // Si el libro no se encuentra, lanza una excepción
        book.setAuthor(null);
        book.setGender(null);
        book.setPublisher(null);
        book.setShelf(null);
        bookRepository.save(book); // Guarda el libro con el author_id establecido a null

        bookRepository.delete(book); // Si el libro se encuentra, lo elimina del repositorio
    }

    private Author getExistingAuthor(Author author) {
        if (author != null && author.getName() != null) {
            Author existingAuthor = authorRepository.findByName(author.getName());
            if (existingAuthor != null) {
                return existingAuthor;
            }
            throw new ResourceNotFoundException("Author " + author.getName() + NOT_FOUND);
        }
        return null;
    }

    private Gender getExistingGender(Gender gender) {
        if (gender != null && gender.getName() != null) {
            Gender existingGender = genderRepository.findByName(gender.getName());
            if (existingGender != null) {
                return existingGender;
            }
            throw new ResourceNotFoundException("Gender " + gender.getName() + NOT_FOUND);
        }
        return null;
    }

    private Publisher getExistingPublisher(Publisher publisher) {
        if (publisher != null && publisher.getName() != null) {
            Publisher existingPublisher = publisherRepository.findByName(publisher.getName());
            if (existingPublisher != null) {
                return existingPublisher;
            }
            throw new ResourceNotFoundException("Publisher " + publisher.getName() + NOT_FOUND);
        }
        return null;
    }

    private Shelf getExistingShelf(Shelf shelf) {
        if (shelf != null && shelf.getCode() != null) {
            Shelf existingShelf = shelfRepository.findByCode(shelf.getCode());
            if (existingShelf != null) {
                return existingShelf;
            }
            throw new ResourceNotFoundException("Shelf with code " + shelf.getCode() + NOT_FOUND);
        }
        return null;
    }

}

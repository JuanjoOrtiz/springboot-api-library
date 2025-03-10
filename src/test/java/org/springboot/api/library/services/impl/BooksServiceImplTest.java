package org.springboot.api.library.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springboot.api.library.dtos.BookDTO;
import org.springboot.api.library.entities.Book;
import org.springboot.api.library.exceptions.NotFoundException;
import org.springboot.api.library.exceptions.ServiceException;
import org.springboot.api.library.mapper.BookMapper;
import org.springboot.api.library.repositories.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BooksServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BooksServiceImpl booksService;

    private BookDTO testBookDTO;
    private Book testBook;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        testBookDTO = new BookDTO(
                1L, "Test Book", "Test Author", "1234567890",
                "Test Publisher", LocalDate.of(2023, 1, 1),
                "Fiction", 10, "/images/test.jpg"
        );

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setIsbn("1234567890");

        pageable = PageRequest.of(0, 20);

        // Setup BookMapper mock behavior
        when(BookMapper.INSTANCE.toDTO(any(Book.class))).thenReturn(testBookDTO);
        when(BookMapper.INSTANCE.toEntity(any(BookDTO.class))).thenReturn(testBook);
    }

    // getAllBooks tests
    @Test
    void getAllBooks_Success_ReturnsBooksPage() {
        Page<Book> booksPage = new PageImpl<>(List.of(testBook));
        when(bookRepository.findAll(pageable)).thenReturn(booksPage);

        Page<BookDTO> result = booksService.getAllBooks(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testBookDTO, result.getContent().get(0));
        verify(bookRepository).findAll(pageable);
    }

    @Test
    void getAllBooks_RepositoryError_ThrowsServiceException() {
        when(bookRepository.findAll(pageable)).thenThrow(new RuntimeException("DB error"));

        assertThrows(ServiceException.class, () -> booksService.getAllBooks(pageable));
    }

    // getBookById tests
    @Test
    void getBookById_Success_ReturnsBookDTO() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        Optional<BookDTO> result = booksService.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals(testBookDTO, result.get());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_InvalidId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> booksService.getBookById(0L));
        assertThrows(IllegalArgumentException.class, () -> booksService.getBookById(null));
        verify(bookRepository, never()).findById(any());
    }

    // createBook tests
    @Test
    void createBook_Success_ReturnsCreatedBookDTO() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookDTO result = booksService.createBook(testBookDTO);

        assertNotNull(result);
        assertEquals(testBookDTO, result);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_NullDTO_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> booksService.createBook(null));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void createBook_InvalidFields_ThrowsIllegalArgumentException() {
        BookDTO invalidDTO = new BookDTO(1L, "", "Author", "ISBN", "Publisher",
                LocalDate.now(), "Category", 1, "route");

        assertThrows(IllegalArgumentException.class, () -> booksService.createBook(invalidDTO));
    }

    // updateBook tests
    @Test
    void updateBook_Success_ReturnsUpdatedBookDTO() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookDTO result = booksService.updateBook(1L, testBookDTO);

        assertNotNull(result);
        assertEquals(testBookDTO, result);
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBook_NotFound_ThrowsNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> booksService.updateBook(1L, testBookDTO));
    }

    @Test
    void updateBook_InvalidId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> booksService.updateBook(0L, testBookDTO));
    }

    @Test
    void deleteBook_Success_DeletesBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        doNothing().when(bookRepository).deleteById(1L);

        booksService.deleteBook(1L);

        verify(bookRepository).findById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_NotFound_ThrowsNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> booksService.deleteBook(1L));
    }

    @Test
    void deleteBook_InvalidId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> booksService.deleteBook(-1L));
        verify(bookRepository, never()).deleteById(any());
    }
}
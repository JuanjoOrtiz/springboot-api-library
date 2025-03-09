package org.springboot.api.library.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springboot.api.library.dtos.BookDTO;
import org.springboot.api.library.exceptions.CreationException;
import org.springboot.api.library.exceptions.InvalidDataException;
import org.springboot.api.library.exceptions.InvalidPaginationException;
import org.springboot.api.library.exceptions.NotFoundException;
import org.springboot.api.library.services.BooksService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BooksControllerTest {
    @Mock
    private BooksService booksService;

    @InjectMocks
    private BooksController booksController;

    private BookDTO testBookDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testBookDTO = new BookDTO(
                1L, "Test Book", "Test Author", "1234567890",
                "Test Publisher", LocalDate.of(2023, 1, 1),
                "Fiction", 10, "/images/test.jpg"
        );
        pageable = PageRequest.of(0, 20);
    }

    // GET all books tests
    @Test
    void getAllBooks_Success_ReturnsBooksPage() {
        Page<BookDTO> booksPage = new PageImpl<>(List.of(testBookDTO));
        when(booksService.getAllBooks(pageable)).thenReturn(booksPage);

        ResponseEntity<Page<BookDTO>> response = booksController.getAllBooks(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(booksPage, response.getBody());
        verify(booksService).getAllBooks(pageable);
    }

    @Test
    void getAllBooks_NoBooksFound_ThrowsNotFoundException() {
        when(booksService.getAllBooks(pageable)).thenReturn(Page.empty());

        assertThrows(NotFoundException.class, () -> booksController.getAllBooks(pageable));
        verify(booksService).getAllBooks(pageable);
    }

    @Test
    void getAllBooks_InvalidPagination_ThrowsInvalidPaginationException() {
        when(booksService.getAllBooks(pageable)).thenThrow(new IllegalArgumentException("Invalid page"));

        assertThrows(InvalidPaginationException.class, () -> booksController.getAllBooks(pageable));
    }

    // GET book by ID tests
    @Test
    void getBookById_Success_ReturnsBook() {
        when(booksService.getBookById(1L)).thenReturn(Optional.of(testBookDTO));

        ResponseEntity<BookDTO> response = booksController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testBookDTO, response.getBody());
        verify(booksService).getBookById(1L);
    }

    @Test
    void getBookById_NotFound_ThrowsNotFoundException() {
        when(booksService.getBookById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> booksController.getBookById(1L));
    }

    // POST create book tests
    @Test
    void createBook_Success_ReturnsCreatedBook() {
        when(booksService.createBook(any(BookDTO.class))).thenReturn(testBookDTO);

        BookDTO response = booksController.createBook(testBookDTO);

        assertEquals(testBookDTO, response);
        verify(booksService).createBook(testBookDTO);
    }

    @Test
    void createBook_NullInput_ThrowsInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> booksController.createBook(null));
        verify(booksService, never()).createBook(any());
    }

    @Test
    void createBook_DuplicateEntry_ThrowsCreationException() {
        when(booksService.createBook(testBookDTO)).thenThrow(new DataIntegrityViolationException("Duplicate ISBN"));

        assertThrows(CreationException.class, () -> booksController.createBook(testBookDTO));
    }

    // PUT update book tests
    @Test
    void updateBook_Success_ReturnsUpdatedBook() {
        when(booksService.updateBook(1L, testBookDTO)).thenReturn(testBookDTO);

        ResponseEntity<BookDTO> response = booksController.updateBook(1L, testBookDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testBookDTO, response.getBody());
        verify(booksService).updateBook(1L, testBookDTO);
    }

    @Test
    void updateBook_NotFound_ThrowsNotFoundException() {
        when(booksService.updateBook(1L, testBookDTO)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> booksController.updateBook(1L, testBookDTO));
    }

    @Test
    void updateBook_NullInput_ThrowsInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> booksController.updateBook(1L, null));
        verify(booksService, never()).updateBook(anyLong(), any());
    }

    // DELETE book tests
    @Test
    void deleteBook_Success_ReturnsNoContent() {
        doNothing().when(booksService).deleteBook(1L);

        ResponseEntity<Void> response = booksController.deleteBook(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(booksService).deleteBook(1L);
    }

    @Test
    void deleteBook_NotFound_ThrowsNotFoundException() {
        doThrow(new NotFoundException("Book not found")).when(booksService).deleteBook(1L);

        assertThrows(NotFoundException.class, () -> booksController.deleteBook(1L));
    }
}
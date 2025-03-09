package org.springboot.api.library.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springboot.api.library.entities.Book;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookRepositoryTest {
    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        // Arrange
        Book book = createSampleBook();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        Optional<Book> result = bookRepository.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("The Great Gatsby", result.get().getTitle());
        assertEquals("F. Scott Fitzgerald", result.get().getAuthor());
        assertEquals("978-0743273565", result.get().getIsbn());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Book> books = Arrays.asList(
                createSampleBook(),
                new Book(2L, "1984", "George Orwell", "978-0451524935",
                        "Secker & Warburg", LocalDate.of(1949, 6, 8),
                        "Dystopian", 5, "images/1984.jpg")
        );
        when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<Book> result = bookRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("The Great Gatsby", result.get(0).getTitle());
        assertEquals("1984", result.get(1).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testSave() {
        // Arrange
        Book book = createSampleBook();
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        Book result = bookRepository.save(book);

        // Assert
        assertNotNull(result);
        assertEquals("The Great Gatsby", result.getTitle());
        assertEquals(3, result.getAvailable_quantity());
        assertEquals("Fiction", result.getCategory());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        Long bookId = 1L;
        doNothing().when(bookRepository).deleteById(bookId);

        // Act
        bookRepository.deleteById(bookId);

        // Assert
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    public void testExistsById() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // Act
        boolean exists = bookRepository.existsById(bookId);

        // Assert
        assertTrue(exists);
        verify(bookRepository, times(1)).existsById(bookId);
    }

    // Helper method to create a sample book with valid data
    private Book createSampleBook() {
        return new Book(
                1L,
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                "978-0743273565",
                "Scribner",
                LocalDate.of(1925, 4, 10),
                "Fiction",
                3,
                "images/gatsby.jpg"
        );
    }
}
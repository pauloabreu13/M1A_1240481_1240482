package pt.psoft.g1.psoftg1.bookmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookServiceImpl;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class BookServiceIT {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    private final String validIsbn = "9782826012092";
    private final String validTitle = "Encantos de contar";
    private final Author validAuthor1 = new Author("João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null);
    private final Author validAuthor2 = new Author("Maria José", "A Maria José nasceu em Viseu e só come laranjas às segundas feiras.", null);
    private final Genre validGenre = new Genre("Fantasia");
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBookWithExistingIsbn() {
        // Arrange
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(validTitle);
        request.setDescription("Uma descrição para o livro.");
        request.setGenre(validGenre.getGenre());
        request.setAuthors(Arrays.asList(validAuthor1.getId()));

        // Mock to simulate that the book already exists
        when(bookRepository.findByIsbn(validIsbn)).thenReturn(Optional.of(new Book()));

        // Act & Assert
        ConflictException thrown = assertThrows(ConflictException.class, () -> {
            bookService.create(request, validIsbn);
        });
        assertEquals("Book with ISBN " + validIsbn + " already exists", thrown.getMessage());
    }


    @Test
    void testFindByGenreWithNoBooks() {
        // Arrange
        String genre = validGenre.getGenre();

        // Mock to simulate that there are no books with the specified genre
        when(bookRepository.findByGenre(genre)).thenReturn(Arrays.asList());

        // Act
        List<Book> books = bookService.findByGenre(genre);

        // Assert
        assertNotNull(books);
        assertTrue(books.isEmpty());
        verify(bookRepository).findByGenre(genre);
    }


    @Test
    void testFindByAuthorNameWithNoBooks() {
        // Arrange
        String authorName = validAuthor1.getName();

        // Mock to simulate that there are no books with the specified author
        when(bookRepository.findByAuthorName(authorName + "%")).thenReturn(Arrays.asList());

        // Act
        List<Book> books = bookService.findByAuthorName(authorName);

        // Assert
        assertNotNull(books);
        assertTrue(books.isEmpty());
        verify(bookRepository).findByAuthorName(authorName + "%");
    }

}

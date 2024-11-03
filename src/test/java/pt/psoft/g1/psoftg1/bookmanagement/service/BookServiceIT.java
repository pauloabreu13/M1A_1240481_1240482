package pt.psoft.g1.psoftg1.bookmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookServiceImpl;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class BookServiceIT {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    private final String validTitle = "Encantos de contar";
    private final Author validAuthor1 = new Author("João Alberto", "Descrição do autor", null);
    private final Genre validGenre = new Genre("Fantasia");

    @BeforeEach
    void setUp() {
        // Salva o autor e o gênero no repositório, se necessário
        authorRepository.save(validAuthor1);
        genreRepository.save(validGenre);
    }

    @Test
    void testFindByAuthorNameWithNoBooks() {
        // Arrange
        String authorName = validAuthor1.getName();

        // Act
        List<Book> books = bookService.findByAuthorName(authorName);

        // Assert
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }



    @Test
    void testFindByGenreWithNoBooks() {
        // Arrange
        String genreName = validGenre.toString();

        // Act
        List<Book> books = bookService.findByGenre(genreName);

        // Assert
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }


    @Test
    void testFindByTitleWithNoBooks() {
        // Arrange
        String title = validTitle;

        // Act
        List<Book> books = bookService.findByTitle(title);

        // Assert
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }

    @Test
    void testCreateBookWithNonExistingGenre() {
        // Arrange
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(validTitle);
        request.setDescription("Uma descrição para o livro.");
        request.setGenre("NonExistingGenre"); // Gênero que não existe
        request.setAuthors(Arrays.asList(validAuthor1.getId()));

        // Act & Assert
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bookService.create(request, "9789720706386");
        });
        assertEquals("Genre not found", thrown.getMessage());
    }


//    Testes Unitários ao Service
//    @Test
//    void testFindByAuthorNameWithNoBooks() {
//        // Arrange
//        String authorName = validAuthor1.getName();
//
//        // Mock to simulate that there are no books with the specified author
//        when(bookRepository.findByAuthorName(authorName + "%")).thenReturn(Arrays.asList());
//
//        // Act
//        List<Book> books = bookService.findByAuthorName(authorName);
//
//        // Assert
//        assertNotNull(books);
//        assertTrue(books.isEmpty());
//        verify(bookRepository).findByAuthorName(authorName + "%");}

//    @Test
//    void testCreateBookWithExistingIsbn() {
//        // Arrange
//        CreateBookRequest request = new CreateBookRequest();
//        request.setTitle(validTitle);
//        request.setDescription("Uma descrição para o livro.");
//        request.setGenre(validGenre.getGenre());
//        request.setAuthors(Arrays.asList(validAuthor1.getId()));
//
//        // Mock to simulate that the book already exists
//        when(bookRepository.findByIsbn(validIsbn)).thenReturn(Optional.of(new Book()));
//
//        // Act & Assert
//        ConflictException thrown = assertThrows(ConflictException.class, () -> {
//            bookService.create(request, validIsbn);
//        });
//        assertEquals("Book with ISBN " + validIsbn + " already exists", thrown.getMessage());
//    }



}



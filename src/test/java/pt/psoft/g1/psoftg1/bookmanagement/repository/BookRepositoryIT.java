package pt.psoft.g1.psoftg1.bookmanagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook;

    @BeforeEach
    void setUp() {
        Genre genre = new Genre("Fiction");
        Author author = new Author("Author Name", "Bio", null);
        testBook = new Book("9789720706386", "Test Book", "Description", genre, List.of(author), "http://example.com/photo.jpg");
        // testBook.setVersion(1L);

        entityManager.persist(genre);
        entityManager.persist(author);
        entityManager.persist(testBook);
        entityManager.flush();
    }

    @Test
    void whenFindByIsbn_thenReturnBook() {
        // Act
        Book found = bookRepository.findByIsbn(testBook.getIsbn()).orElse(null);

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getIsbn()).isEqualTo(testBook.getIsbn());
        assertThat(found.getTitle()).isEqualTo(testBook.getTitle());
    }


    @Test
    void whenFindByGenre_thenReturnBooks() {
        // Act
        List<Book> foundBooks = bookRepository.findByGenre(testBook.getGenre().toString());

        // Assert
        assertThat(foundBooks).isNotEmpty();
        assertThat(foundBooks.get(0).getGenre().toString()).isEqualTo(testBook.getGenre().toString());
    }

    @Test
    void whenDeleteBook_thenBookShouldNotExist() {
        // Act
        bookRepository.delete(testBook);
        entityManager.flush();

        // Assert
        Book found = bookRepository.findByIsbn(testBook.getIsbn()).orElse(null);
        assertThat(found).isNull();
    }



}
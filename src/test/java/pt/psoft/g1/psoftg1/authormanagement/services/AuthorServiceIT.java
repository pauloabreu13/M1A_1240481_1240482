package pt.psoft.g1.psoftg1.authormanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

/**
 * Based on https://www.baeldung.com/spring-boot-testing
 * <p>Adaptations to Junit 5 with ChatGPT
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthorServiceIT {
    @Autowired
    private AuthorService authorService;
    @MockBean
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        List<Author> list = new ArrayList<>();
        list.add(alex);

        when(authorRepository.searchByNameName(alex.getName()))
                .thenReturn(list);
    }

    @Test
    public void whenValidAuthor_thenAuthorShouldBeCreated() {
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        CreateAuthorRequest request = new CreateAuthorRequest(alex.getName(), alex.getBio(), null, null);

        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(alex);

        Author created = authorService.create(request);

        assertThat(created.getName()).isEqualTo("Alex");
        assertThat(created.getBio()).isEqualTo("O Alex escreveu livros");
    }

    @Test
    public void whenFindByAuthorNumber_thenReturnAuthor() {
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        when(authorRepository.findByAuthorNumber(alex.getAuthorNumber())).thenReturn(Optional.of(alex));

        Optional<Author> found = authorService.findByAuthorNumber(alex.getAuthorNumber());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getName()).isEqualTo("Alex");
        assertThat(found.get().getBio()).isEqualTo("O Alex escreveu livros");
    }


    @Test
    public void whenFindAll_thenReturnAuthorsList() {
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        Author maria = new Author("Maria", "Maria também é autora", null);
        List<Author> authors = new ArrayList<>();
        authors.add(alex);
        authors.add(maria);

        when(authorRepository.findAll()).thenReturn(authors);

        Iterable<Author> found = authorService.findAll();

        assertThat(found).isNotNull();
        assertThat(((List<Author>) found).size()).isEqualTo(2);
    }

    @Test
    public void whenFindByName_thenReturnAuthorsList() {
        when(authorRepository.searchByNameNameStartsWith("Alex"))
                .thenReturn(List.of(new Author("Alex", "O Alex escreveu livros", null)));

        List<Author> found = authorService.findByName("Alex");

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getName()).isEqualTo("Alex");
    }


    @Test
    public void whenFindByName_thenReturnEmptyList() {
        when(authorRepository.searchByNameName("Alex")).thenReturn(new ArrayList<>());

        List<Author> found = authorService.findByName("Alex");

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(0);
    }

    @Test
    public void whenRemoveAuthorPhoto_thenReturnAuthorWithoutPhoto() {
        Author alex = new Author("Alex", "O Alex escreveu livros", "foto.png");
        when(authorRepository.findByAuthorNumber(1L)).thenReturn(Optional.of(alex));

        when(authorRepository.save(any(Author.class))).thenReturn(alex);

        Optional<Author> updatedAuthor = authorService.removeAuthorPhoto(1L, alex.getVersion());

        assertThat(updatedAuthor.isPresent()).isTrue();
        assertThat(updatedAuthor.get().getPhoto()).isNull();
    }
}


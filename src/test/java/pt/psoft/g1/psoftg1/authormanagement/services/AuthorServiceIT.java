package pt.psoft.g1.psoftg1.authormanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        List<Author> list = new ArrayList<>();
        list.add(alex);

        Mockito.when(authorRepository.searchByNameName(alex.getName()))
                .thenReturn(list);
    }

    @Test
    public void whenValidAuthor_thenAuthorShouldBeCreated() {
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        CreateAuthorRequest request = new CreateAuthorRequest(alex.getName(), alex.getBio(), null, null);

        Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenReturn(alex);

        Author created = authorService.create(request);

        assertThat(created.getName()).isEqualTo("Alex");
        assertThat(created.getBio()).isEqualTo("O Alex escreveu livros");
    }

    @Test
    public void whenFindByAuthorNumber_thenReturnAuthor() {
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        Mockito.when(authorRepository.findByAuthorNumber(alex.getAuthorNumber())).thenReturn(Optional.of(alex));

        Optional<Author> found = authorService.findByAuthorNumber(alex.getAuthorNumber());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getName()).isEqualTo("Alex");
        assertThat(found.get().getBio()).isEqualTo("O Alex escreveu livros");
    }
}


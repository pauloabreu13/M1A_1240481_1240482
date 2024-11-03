package pt.psoft.g1.psoftg1.authormanagement.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Based on https://www.baeldung.com/spring-boot-testing
 * <p>Adaptations to Junit 5 with ChatGPT
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AuthorRepositoryIT {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void whenFindByName_thenReturnAuthor() {
        // given
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        entityManager.persist(alex);
        entityManager.flush();

        // when
        List<Author> list = authorRepository.searchByNameName(alex.getName());

        // then
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getName())
                .isEqualTo(alex.getName());
    }

    @Test
    public void whenSearchingForUnknownAuthor_thenReturnsNoResults() {
        // ARRANGE
        String missingAuthorName = "UnknownAuthor";

        // ACT
        List<Author> resultAuthors = authorRepository.searchByNameName(missingAuthorName);

        // ASSERT
        assertThat(resultAuthors).isNotNull();
        assertThat(resultAuthors).hasSize(0);
    }

    @Test
    public void givenExistingAuthorName_whenSearchByName_thenAuthorIsFound() {
        //ARRANGE
        Author authorSample = new Author("Alex", "O Alex escreveu livros", null);
        entityManager.persist(authorSample);
        entityManager.flush();

        //ACT
        List<Author> foundAuthors = authorRepository.searchByNameName(authorSample.getName());

        //ASSERT
        assertThat(foundAuthors).hasSize(1);
        assertThat(foundAuthors)
                .extracting(Author::getName)
                .containsExactly(authorSample.getName());
    }

}

package pt.psoft.g1.psoftg1.authormanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.shared.model.Photo;


import static org.junit.jupiter.api.Assertions.*;


class AuthorTest {
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";

    private final UpdateAuthorRequest request = new UpdateAuthorRequest(validName, validBio, null, null);

    private static class EntityWithPhotoImpl extends EntityWithPhoto { }
    @BeforeEach
    void setUp() {
    }
    @Test
    void ensureNameNotNull() {
        assertThrows(IllegalArgumentException.class, () -> new Author(null, validBio, null));
    }

    @Test
    void ensureBioNotNull() {
        assertThrows(IllegalArgumentException.class, () -> new Author(validName, null, null));
    }

    @Test
    void whenVersionIsStaleItIsNotPossibleToPatch() {
        final var subject = new Author(validName, validBio, null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void testCreateAuthorWithoutPhoto() {
        Author author = new Author(validName, validBio, null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testCreateAuthorRequestWithPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, "photoTest.jpg");
        Author author = new Author(request.getName(), request.getBio(), "photoTest.jpg");
        assertNotNull(author);
        assertEquals(request.getPhotoURI(), author.getPhoto().getPhotoFile());
    }

    @Test
    void testCreateAuthorRequestWithoutPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, null);
        Author author = new Author(request.getName(), request.getBio(), null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testEntityWithPhotoSetPhotoInternalWithValidURI() {
        EntityWithPhoto entity = new EntityWithPhotoImpl();
        String validPhotoURI = "photoTest.jpg";
        entity.setPhoto(validPhotoURI);
        assertNotNull(entity.getPhoto());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Author author = new Author(validName, validBio, null);
        assertNull(author.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        Author author = new Author(validName, validBio, "photoTest.jpg");
        Photo photo = author.getPhoto();
        assertNotNull(photo);
        assertEquals("photoTest.jpg", photo.getPhotoFile());
    }

    @Test
    void testRemovePhotoThrowsConflictExceptionForInvalidVersion() {
        // ARRANGE
        Author author = new Author(validName, validBio, "photo.jpg");
        long invalidVersion = author.getVersion() + 1;

        // ACT & ASSERT
        assertThrows(ConflictException.class, () -> author.removePhoto(invalidVersion));
    }

    @Test
    void testRemovePhotoWithValidVersion() {
        // ARRANGE
        Author author = new Author(validName, validBio, "photo.jpg");
        long validVersion = author.getVersion();

        // ACT
        author.removePhoto(validVersion);

        // ASSERT
        assertNull(author.getPhoto());
    }

    @Test
    void testGettersForConsistency() {
        // ARRANGE
        String photoURI = "photo.jpg";

        // ACT
        Author author = new Author(validName, validBio, photoURI);

        // ASSERT
        assertEquals(validName, author.getName());
        assertEquals(validBio, author.getBio());
        assertEquals(photoURI, author.getPhoto().getPhotoFile());
    }

    @Test
    void testGetIdReturnsCorrectId() {
        // ARRANGE
        Author author = new Author(validName, validBio, null);
        Long expectedId = author.getId();

        // ACT
        Long actualId = author.getId();

        // ASSERT
        assertEquals(expectedId, actualId);
    }

    @Test
    void testGetAuthorNumber() {
        // ARRANGE
        Author author = new Author(validName, validBio, null);
        Long expectedNumber = author.getAuthorNumber();

        // ACT
        Long actualNumber = author.getAuthorNumber();

        // ASSERT
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    void testApplyPatchUpdatesNameWhenNotNull() {
        // ARRANGE
        Author author = new Author(new String("NomeValido"), validBio, null);
        UpdateAuthorRequest request = new UpdateAuthorRequest(null, "NovoNome", null, null);
        long currentVersion = author.getVersion();

        // ACT
        author.applyPatch(currentVersion, request);

        // ASSERT
        assertEquals("NovoNome", author.getName().toString());
    }

    @Test
    void testApplyPatchUpdatesBioWhenNotNull() {
        // ARRANGE
        Author author = new Author(new String("NomeValido"), "Biografia Antiga", null);
        UpdateAuthorRequest request = new UpdateAuthorRequest("Nova Biografia", null, null, null);
        long currentVersion = author.getVersion();

        // ACT
        author.applyPatch(currentVersion, request);

        // ASSERT
        assertEquals("Nova Biografia", author.getBio());
    }
}


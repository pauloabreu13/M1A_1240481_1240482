package pt.psoft.g1.psoftg1.bookmanagement.model;


import java.util.List;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

public class BookFactory {

    public static Book createBook(String isbn, String title, String description, Genre genre, List<Author> authors, String photoURI) {
        return new Book(isbn, title, description, genre, authors, photoURI);
    }
}


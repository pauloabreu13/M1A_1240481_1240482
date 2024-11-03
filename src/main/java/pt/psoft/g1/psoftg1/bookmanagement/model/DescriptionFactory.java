package pt.psoft.g1.psoftg1.bookmanagement.model;

public class DescriptionFactory {
    public static Description createDescription(String description) {
        return new Description(description);
    }
}

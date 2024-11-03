package pt.psoft.g1.psoftg1.bookmanagement.model;


public class TitleFactory {
    public static Title createTitle(String title) {
        return new Title(title);
    }
}

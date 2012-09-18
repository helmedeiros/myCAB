package app.fourthink.model;

public final class CabKind {

    private CabKind() {}

    public static String label(CabCategory category) {
        switch (category) {
            case NORMAL:   return "Normal";
            case CONFORTO: return "Conforto";
            case GRANDE:   return "Grande";
        }
        return category.name();
    }
}

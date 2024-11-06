package modelo;

public abstract class Ficha {
    protected String color;

    public Ficha (String color) {
        this.color = color;
    }

    public static Ficha crearPieza(String tipoPieza, String color) {
        return switch (tipoPieza) {
            case "R" -> new Torre(color);
            case "B" -> new Alfil(color);
            case "N" -> new Caballo(color);
            case "Q" -> new Dama(color);
            case "K" -> new Rey(color);
            case "P" -> new Peon(color);
            default -> null;
        };
    }

    public String getColor() {
        return color;
    }

    public abstract String getTipoFicha();
}

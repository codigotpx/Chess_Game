package modelo;

public abstract class Ficha {
    protected String color;

    public Ficha (String color) {
        this.color = color;
    }

    public abstract String getTipoFicha();
}

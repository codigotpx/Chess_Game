package modelo;

public class Alfil extends Ficha{

    public Alfil(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "B";
    }
}

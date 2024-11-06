package modelo;

public class Caballo extends Ficha{

    public Caballo(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "N";
    }
}

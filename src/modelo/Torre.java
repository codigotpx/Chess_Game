package modelo;

public class Torre extends Ficha{

    public Torre(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "R";
    }
}

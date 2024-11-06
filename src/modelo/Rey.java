package modelo;

public class Rey extends Ficha{

    public Rey(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "K";
    }
}

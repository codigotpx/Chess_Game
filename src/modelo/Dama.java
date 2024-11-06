package modelo;

public class Dama extends Ficha{

    public Dama(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "Q";
    }
}

package modelo;

public class Casilla {
    Ficha ficha;
    String id;

    public Casilla(String id) {
        this.ficha = null;
        this.id = id;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public String getId() {
        return id;
    }
}

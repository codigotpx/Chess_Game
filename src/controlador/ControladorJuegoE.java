package controlador;

import modelo.Casilla;
import modelo.Ficha;
import modelo.Tablero;
import vista.TableroBoton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControladorJuegoE {
    private TableroBoton tableroBoton;
    private Casilla casilla;
    private boolean esPrimerClick;
    private Ficha fichaInicial;
    private Casilla casillaInicial;
    private Tablero tablero;

    public ControladorJuegoE(TableroBoton tableroBoton, Tablero tablero) {
        this.tableroBoton = tableroBoton;
        this.casilla = null;
        fichaInicial = null;
        casillaInicial = null;
        this.esPrimerClick = false;
        this.tablero = tablero;
        this.tableroBoton.addBotonClick(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obtenerCasillas();
            }
        });
    }

    public void obtenerCasillas() {
        esPrimerClick = !esPrimerClick;
        casilla = tableroBoton.getCasillaClick();
        System.out.printf(casilla.getId());

        if (esPrimerClick) {
            fichaInicial = casilla.getFicha();
            casillaInicial = casilla;
            System.out.printf("Se obtuvo la ficha inicial");
        } else {
            Ficha fichaFinal = casilla.getFicha();
            Casilla casillaFinal = casilla;
            System.out.printf("Se obtuvo la ficha final");
            ejecutarMovimiento(fichaInicial, fichaFinal, casillaInicial, casillaFinal);
        }
    }

    public void ejecutarMovimiento(Ficha fichaInicial, Ficha fichaFinal, Casilla casillaInicial, Casilla casillaFinal) {
        System.out.printf("Se esta ejecutando un movimiento");
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Casilla casilla = tablero.getCasilla(fila, columna);
                Ficha ficha = casilla.getFicha();
                if(ficha != null && ficha.getTipoFicha().equals(fichaInicial.getTipoFicha()) && ficha.getColor()
                        .equals(fichaInicial.getColor())) {
                    String posicionActual = tablero.convertirCoordenadasAId(fila, columna);
                    List<String> movimientosValidos = fichaInicial.movimientosValidos(posicionActual, tablero);

                    if (movimientosValidos.contains(casillaFinal.getId())) {
                        System.out.printf("Se estan haciendo los movimientos");
                        casillaFinal.setFicha(fichaInicial);
                        casillaFinal.getFicha().setPosicion(casillaFinal.getId());
                        casillaInicial.setFicha(null);

                        tableroBoton.actualizarVistaTablero(tablero);
                    }
                }
                }
            }
    }

}

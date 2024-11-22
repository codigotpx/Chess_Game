package controlador;

import modelo.Casilla;
import modelo.Ficha;
import modelo.Tablero;
import vista.TableroBoton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControladorJuegoE {
    private TableroBoton tableroBoton;
    private Tablero tablero;
    private Casilla casillaInicial, casillaFinal;
    private Ficha fichaInicial;
    private boolean esPrimerClick, isBlanca, captura;
    private StringBuilder pgnNotation;
    private int moveNumber;

    public ControladorJuegoE(TableroBoton tableroBoton, Tablero tablero) {
        this.tableroBoton = tableroBoton;
        this.tablero = tablero;
        this.isBlanca = true; // Comienzan las blancas
        this.pgnNotation = new StringBuilder();
        this.moveNumber = 1;
        this.esPrimerClick = true; // Indicador de clic inicial

        this.tableroBoton.addBotonClick(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestionarClic();
            }
        });
    }

    private void gestionarClic() {
        Casilla casillaSeleccionada = tableroBoton.getCasillaClick();
        if (casillaSeleccionada == null) return;

        if (esPrimerClick) {
            manejarPrimerClick(casillaSeleccionada);
        } else {
            manejarSegundoClick(casillaSeleccionada);
        }
    }

    private void manejarPrimerClick(Casilla casilla) {
        fichaInicial = casilla.getFicha();
        if (fichaInicial == null || !fichaInicial.getColor().equals(isBlanca ? "W" : "B")) {
            JOptionPane.showMessageDialog(null, "Selecciona una pieza válida.");
            return;
        }

        casillaInicial = casilla;
        esPrimerClick = false;
    }

    private void manejarSegundoClick(Casilla casilla) {
        casillaFinal = casilla;

        if (ejecutarMovimiento()) {
            actualizarEstado();
        }

        // Reiniciar selección
        esPrimerClick = true;
        fichaInicial = null;
        casillaInicial = null;
    }

    private boolean ejecutarMovimiento() {
        if (!esMovimientoValido()) {
            JOptionPane.showMessageDialog(null, "Movimiento no válido.");
            return false;
        }

        captura = casillaFinal.getFicha() != null;

        // Realizar el movimiento
        casillaFinal.setFicha(fichaInicial);
        casillaFinal.getFicha().setPosicion(casillaFinal.getId());
        casillaInicial.setFicha(null);

        // Manejo de promoción de peones
        if (esPromocion()) {
            manejarPromocion(casillaFinal);
        }

        escribirMovimientos(casillaFinal.getId(), fichaInicial.getTipoFicha(), captura);

        tableroBoton.actualizarVistaTablero(tablero);
        return true;
    }

    private boolean esMovimientoValido() {
        if (fichaInicial == null || casillaInicial == null || casillaFinal == null) return false;

        List<String> movimientosValidos = fichaInicial.movimientosValidos(casillaInicial.getId(), tablero);
        return movimientosValidos.contains(casillaFinal.getId());
    }

    private boolean esPromocion() {
        return fichaInicial.getTipoFicha().equals("P") &&
                (casillaFinal.getId().charAt(1) == '8' || casillaFinal.getId().charAt(1) == '1');
    }

    private void manejarPromocion(Casilla casilla) {
        String[] opciones = {"Reina", "Torre", "Alfil", "Caballo"};
        String eleccion = (String) JOptionPane.showInputDialog(
                null,
                "Elige una pieza para la promoción:",
                "Promoción de Peón",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (eleccion != null) {
            String tipoFicha = switch (eleccion) {
                case "Reina" -> "Q";
                case "Torre" -> "R";
                case "Alfil" -> "B";
                case "Caballo" -> "N";
                default -> "";
            };

            Ficha nuevaFicha = Ficha.crearPieza(tipoFicha, fichaInicial.getColor());
            casilla.setFicha(nuevaFicha);

            // Actualizar notación PGN para promoción
            pgnNotation.append("=").append(tipoFicha).append(" ");
        }
    }

    private void escribirMovimientos(String destino, String tipoFicha, boolean isCaptura) {
        StringBuilder movimiento = new StringBuilder();

        if (isBlanca) {
            movimiento.append(moveNumber).append(". ");
        }

        if (!tipoFicha.equals("P")) {
            movimiento.append(tipoFicha);
        } else if (isCaptura) {
            movimiento.append(casillaInicial.getId().charAt(0));
        }

        if (isCaptura) {
            movimiento.append("x");
        }

        movimiento.append(destino);
        pgnNotation.append(movimiento).append(" ");

        if (!isBlanca) {
            moveNumber++;
        }

        System.out.println("PGN actual: " + pgnNotation.toString().trim());
    }

    private void actualizarEstado() {
        isBlanca = !isBlanca;
    }

    public String getPGNNotation() {
        return pgnNotation.toString().trim();
    }
}

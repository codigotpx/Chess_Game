package controlador;

import modelo.Casilla;
import modelo.Ficha;
import modelo.Tablero;
import vista.TableroBoton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class ControladorJuegoE {
    private TableroBoton tableroBoton;
    private Tablero tablero;
    private Casilla casillaInicial, casillaFinal;
    private Ficha fichaInicial;
    private boolean esPrimerClick, isBlanca, captura, juegoTerminado;
    private StringBuilder pgnNotation;
    private int moveNumber;
    private boolean reyBlancaMovido = false;
    private boolean reyNegraMovido = false;
    private boolean torreBlancaH1Movida = false;
    private boolean torreBlancaA1Movida = false;
    private boolean torreNegraH8Movida = false;
    private boolean torreNegraA8Movida = false;
    private boolean esTablas = false;

    public ControladorJuegoE(TableroBoton tableroBoton, Tablero tablero) {
        this.tableroBoton = tableroBoton;
        this.tablero = tablero;
        this.isBlanca = true;
        this.pgnNotation = new StringBuilder();
        this.moveNumber = 1;
        this.esPrimerClick = true;
        this.juegoTerminado = false;

        this.tableroBoton.addBotonClick(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestionarClic();
            }
        });
    }

    private void gestionarClic() {
        if (juegoTerminado) {
            JOptionPane.showMessageDialog(null, "El juego ha terminado. Inicie una nueva partida.");
            return;
        }

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

        esPrimerClick = true;
        fichaInicial = null;
        casillaInicial = null;
    }

    private boolean ejecutarMovimiento() {
        if (!esMovimientoValido()) {
            JOptionPane.showMessageDialog(null, "Movimiento no válido.");
            return false;
        }

        if (dejaReyEnJaque()) {
            JOptionPane.showMessageDialog(null, "No puedes dejar tu rey en jaque.");
            return false;
        }

        captura = casillaFinal.getFicha() != null;

        boolean esEnroque = esEnroque();
        if (esEnroque) {
            realizarEnroque();
        } else {
            realizarMovimientoNormal();
        }

        actualizarEstadoPiezasEnroque();

        boolean esJaque = esJaque(!isBlanca);
        boolean esJaqueMate = esJaque && esJaqueMate(!isBlanca);
        esTablas = !esJaque && esAhogado(!isBlanca);

        escribirMovimientos(casillaFinal.getId(), fichaInicial.getTipoFicha(), captura, esEnroque, esJaque, esJaqueMate);

        if (esJaqueMate) {
            juegoTerminado = true;
            String ganador = isBlanca ? "Blancas" : "Negras";
            JOptionPane.showMessageDialog(null, "¡Jaque Mate! Ganan las " + ganador);
            guardarPartida();
        } else if (esTablas) {
            juegoTerminado = true;
            JOptionPane.showMessageDialog(null, "¡Tablas por ahogado!");
            guardarPartida();
        }

        tableroBoton.actualizarVistaTablero(tablero);
        return true;
    }

    private boolean esAhogado(boolean colorBlanco) {
        if (esJaque(colorBlanco)) {
            return false;
        }

        for (char columna = 'a'; columna <= 'h'; columna++) {
            for (char fila = '1'; fila <= '8'; fila++) {
                String posicion = "" + columna + fila;
                Casilla casilla = tablero.getCasillaPorId(posicion);
                Ficha ficha = casilla.getFicha();

                if (ficha != null && ficha.getColor().equals(colorBlanco ? "W" : "B")) {
                    List<String> movimientos = ficha.movimientosValidos(casilla.getId(), tablero);

                    for (String movimiento : movimientos) {

                        Casilla destino = tablero.getCasillaPorId(movimiento);

                        // Simular movimiento
                        Ficha fichaDestino = destino.getFicha();
                        destino.setFicha(ficha);
                        casilla.setFicha(null);

                        boolean sigueEnJaque = esJaque(colorBlanco);

                        // Deshacer movimiento
                        casilla.setFicha(ficha);
                        destino.setFicha(fichaDestino);

                        if (!sigueEnJaque) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void realizarMovimientoNormal() {
        casillaFinal.setFicha(fichaInicial);
        casillaFinal.getFicha().setPosicion(casillaFinal.getId());
        casillaInicial.setFicha(null);

        if (esPromocion()) {
            manejarPromocion(casillaFinal);
        }
    }

    private boolean esEnroque() {
        if (fichaInicial.getTipoFicha().equals("K")) {
            String posInicial = casillaInicial.getId();
            String posFinal = casillaFinal.getId();

            if (isBlanca) {
                if (posInicial.equals("e1") && posFinal.equals("g1") && !reyBlancaMovido && !torreBlancaH1Movida) {
                    return puedeEnrocar("corto", true);
                }
                if (posInicial.equals("e1") && posFinal.equals("c1") && !reyBlancaMovido && !torreBlancaA1Movida) {
                    return puedeEnrocar("largo", true);
                }
            } else {
                if (posInicial.equals("e8") && posFinal.equals("g8") && !reyNegraMovido && !torreNegraH8Movida) {
                    return puedeEnrocar("corto", false);
                }
                if (posInicial.equals("e8") && posFinal.equals("c8") && !reyNegraMovido && !torreNegraA8Movida) {
                    return puedeEnrocar("largo", false);
                }
            }
        }
        return false;
    }

    private boolean puedeEnrocar(String tipo, boolean esBlanca) {
        if (esJaque(esBlanca)) {
            return false;
        }

        String fila = esBlanca ? "1" : "8";
        if (tipo.equals("corto")) {
            return casillaVaciaYNoAmenazada("f" + fila, esBlanca) &&
                    casillaVaciaYNoAmenazada("g" + fila, esBlanca);
        } else {
            return casillaVaciaYNoAmenazada("d" + fila, esBlanca) &&
                    casillaVaciaYNoAmenazada("c" + fila, esBlanca) &&
                    casillaVaciaYNoAmenazada("b" + fila, esBlanca);
        }
    }

    private boolean casillaVaciaYNoAmenazada(String posicion, boolean esBlanca) {
        Casilla casilla = tablero.getCasillaPorId(posicion);
        return casilla.getFicha() == null && !casillaBajoAtaque(posicion, esBlanca);
    }

    private void realizarEnroque() {
        String fila = isBlanca ? "1" : "7";
        if (casillaFinal.getId().equals("g" + fila)) { // Enroque corto

            casillaFinal.setFicha(fichaInicial);
            casillaFinal.getFicha().setPosicion("g" + fila);
            casillaInicial.setFicha(null);

            Casilla torreInicial = tablero.getCasillaPorId("h" + fila);
            Casilla torreFinal = tablero.getCasillaPorId("e" + fila);
            torreFinal.setFicha(torreInicial.getFicha());
            torreFinal.getFicha().setPosicion("f" + fila);
            torreInicial.setFicha(null);
        } else {
            casillaFinal.setFicha(fichaInicial);
            casillaFinal.getFicha().setPosicion("c" + fila);
            casillaInicial.setFicha(null);
            // Mover torre
            Casilla torreInicial = tablero.getCasillaPorId("a" + fila);
            Casilla torreFinal = tablero.getCasillaPorId("d" + fila);
            torreFinal.setFicha(torreInicial.getFicha());
            torreFinal.getFicha().setPosicion("d" + fila);
            torreInicial.setFicha(null);
        }
    }

    private void actualizarEstadoPiezasEnroque() {
        if (fichaInicial.getTipoFicha().equals("K")) {
            if (isBlanca) {
                reyBlancaMovido = true;
            } else {
                reyNegraMovido = true;
            }
        } else if (fichaInicial.getTipoFicha().equals("R")) {
            String pos = casillaInicial.getId();
            if (pos.equals("h1")) torreBlancaH1Movida = true;
            if (pos.equals("a1")) torreBlancaA1Movida = true;
            if (pos.equals("h8")) torreNegraH8Movida = true;
            if (pos.equals("a8")) torreNegraA8Movida = true;
        }
    }

    private boolean esJaque(boolean colorBlanco) {
        String posRey = encontrarRey(colorBlanco);
        return posRey != null && casillaBajoAtaque(posRey, colorBlanco);
    }

    private String encontrarRey(boolean colorBlanco) {
        for (char columna = 'a'; columna <= 'h'; columna++) {
            for (char fila = '1'; fila <= '8'; fila++) {
                String posicion = "" + columna + fila;
                Casilla casilla = tablero.getCasillaPorId(posicion);
                Ficha ficha = casilla.getFicha();
                if (ficha != null &&
                        ficha.getTipoFicha().equals("K") &&
                        ficha.getColor().equals(colorBlanco ? "W" : "B")) {
                    return posicion;
                }
            }
        }
        return null;
    }

    private boolean casillaBajoAtaque(String posicion, boolean colorBlanco) {
        for (char columna = 'a'; columna <= 'h'; columna++) {
            for (char fila = '1'; fila <= '8'; fila++) {
                String pos = "" + columna + fila;
                Casilla casilla = tablero.getCasillaPorId(pos);
                Ficha ficha = casilla.getFicha();
                if (ficha != null &&
                        ficha.getColor().equals(colorBlanco ? "B" : "W")) {
                    List<String> movimientos = ficha.movimientosValidos(pos, tablero);
                    if (movimientos.contains(posicion)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean esJaqueMate(boolean colorBlanco) {
        for (char columna = 'a'; columna <= 'h'; columna++) {
            for (char fila = '1'; fila <= '8'; fila++) {
                String posicion = "" + columna + fila;
                Casilla casilla = tablero.getCasillaPorId(posicion);
                Ficha ficha = casilla.getFicha();
                if (ficha != null &&
                        ficha.getColor().equals(colorBlanco ? "W" : "B")) {
                    List<String> movimientos = ficha.movimientosValidos(posicion, tablero);
                    for (String movimiento : movimientos) {

                        Casilla destino = tablero.getCasillaPorId(movimiento);

                        // Simular movimiento
                        Ficha fichaDestino = destino.getFicha();
                        destino.setFicha(ficha);
                        casilla.setFicha(null);

                        boolean sigueEnJaque = esJaque(colorBlanco);

                        // Deshacer movimiento
                        casilla.setFicha(ficha);
                        destino.setFicha(fichaDestino);

                        if (!sigueEnJaque) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean dejaReyEnJaque() {
        // Simular el movimiento
        Ficha fichaDestino = casillaFinal.getFicha();
        casillaFinal.setFicha(fichaInicial);
        casillaInicial.setFicha(null);

        boolean resultado = esJaque(isBlanca);

        // Deshacer el movimiento simulado
        casillaInicial.setFicha(fichaInicial);
        casillaFinal.setFicha(fichaDestino);

        return resultado;
    }

    private void escribirMovimientos(String destino, String tipoFicha, boolean isCaptura,
                                     boolean esEnroque, boolean esJaque, boolean esJaqueMate) {
        StringBuilder movimiento = new StringBuilder();

        if (isBlanca) {
            movimiento.append(moveNumber).append(". ");
        }

        if (esEnroque) {
            movimiento.append(destino.equals("g1") || destino.equals("g8") ? "O-O" : "O-O-O");
        } else {
            if (!tipoFicha.equals("P")) {
                movimiento.append(tipoFicha);
            } else if (isCaptura) {
                movimiento.append(casillaInicial.getId().charAt(0));
            }

            if (isCaptura) {
                movimiento.append("x");
            }

            movimiento.append(destino);

            // Añadir promoción si aplica
            if (esPromocion()) {
                String nuevaPieza = casillaFinal.getFicha().getTipoFicha();
                movimiento.append("=").append(nuevaPieza);
            }
        }

        // Añadir símbolos de jaque o jaque mate
        if (esJaqueMate) {
            movimiento.append("#");
        } else if (esJaque) {
            movimiento.append("+");
        }

        pgnNotation.append(movimiento).append(" ");

        if (!isBlanca) {
            moveNumber++;
        }

        System.out.println("PGN actual: " + pgnNotation.toString().trim());
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
        }
    }

    private void actualizarEstado() {
        isBlanca = !isBlanca;
    }

    private boolean esMovimientoValido() {
        if (fichaInicial == null || casillaInicial == null || casillaFinal == null) return false;

        List<String> movimientosValidos = fichaInicial.movimientosValidos(casillaInicial.getId(), tablero);
        return movimientosValidos.contains(casillaFinal.getId());
    }

    public String getPGNNotation() {
        return pgnNotation.toString().trim();
    }

    private void guardarPartida() {
        int opcion = JOptionPane.showConfirmDialog(null, "¿Deseas guardar la partida?",
                "Guardar Partida", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecciona dónde guardar la partida");

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = fileChooser.getSelectedFile();

                // Lógica para guardar la partida en el archivo
                guardarPartidaEnArchivo(archivoSeleccionado);

                JOptionPane.showMessageDialog(null, "Partida guardada en: " + archivoSeleccionado.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(null, "No se guardó la partida.");
            }
        }
    }

    private void guardarPartidaEnArchivo(File archivo) {
        try (PrintWriter writer = new PrintWriter(archivo)) {
            writer.println("Estado de la partida:");
            writer.println("Turno final: " + (isBlanca ? "Blancas" : "Negras"));
            writer.println("Resultado: " + (esTablas ? "Tablas" : "Ganador: " + (isBlanca ? "Blancas" : "Negras")));

            // Aquí podrías agregar más información sobre las piezas y el tablero
            writer.println("Tablero:");
            writer.println(getPGNNotation());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar la partida: " + e.getMessage());
        }
    }
}

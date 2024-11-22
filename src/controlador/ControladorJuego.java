package controlador;

import funcionalidades.CargarPgn;
import modelo.Casilla;
import modelo.Ficha;
import modelo.Movimiento;
import modelo.Tablero;
import vista.PanelJuego;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ControladorJuego {
    private Tablero tablero;
    private CargarPgn cargarPgn;
    private PanelJuego panelJuego;
    private Movimiento movimiento;
    private int numeroMovimiento;
    private boolean isBlanca; // true: blanca false: negro
    private String colorActual;

    // Lista para almacenar los estados previos del tablero
    private List<Tablero> historialTableros;
    // Lista para almacenar los estados previos de las fichas capturadas
    private List<Casilla> historialCapturadas;

    public ControladorJuego(Tablero tablero, CargarPgn cargarPgn, PanelJuego panelJuego) {
        this.tablero = tablero;
        this.cargarPgn = cargarPgn;
        this.panelJuego = panelJuego;
        this.movimiento = null;
        this.numeroMovimiento = 0;
        this.isBlanca = true;
        this.colorActual = "";

        // Inicializar listas de historial
        this.historialTableros = new ArrayList<>();
        this.historialCapturadas = new ArrayList<>();

        iniciarListener();
    }

    private void iniciarListener() {
        // Listener para avanzar al siguiente movimiento
        this.panelJuego.addBotonSiguienteListener(e -> avanzarMovimeinto());

        // Listener para retroceder al movimiento anterior
        this.panelJuego.addBotonRetrocederListener(e -> retrocederMovimeinto());

        // Listener para cargar el PGN
        this.panelJuego.addBotonCargarPGNListener(e -> cargarArchivoPGN());
    }


    // Método existente de cargar archivo PGN
    public void cargarArchivoPGN() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(panelJuego);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoPGN = fileChooser.getSelectedFile();
            String ruta = archivoPGN.getAbsolutePath();
            movimiento = new Movimiento(ruta);
            JOptionPane.showMessageDialog(panelJuego, "Archivo PGN cargado exitosamente.");

            // Limpiar historial al cargar un nuevo archivo
            historialTableros.clear();
            historialCapturadas.clear();

            tablero.inicializarTablero();
            panelJuego.reiniciarTablero(tablero);
            numeroMovimiento = 0;
            isBlanca = true;
        }
    }

    public void avanzarMovimeinto() {
        if (movimiento == null) {
            JOptionPane.showMessageDialog(panelJuego, "No se ha cargado un archivo PGN.");
            return;
        }
        ejecutarMovimiento(numeroMovimiento);
        numeroMovimiento++;
    }

    // Método modificado para guardar el estado anterior antes de cada movimiento
    public void ejecutarMovimiento(int numeroMovimiento) {
        // Guardar el estado actual del tablero antes de ejecutar el movimiento
        historialTableros.add(copiarTablero(tablero));

        // Procesar el movimiento actual usando la clase Movimiento
        Casilla casillaCapturada = procesarMovimientoConCaptura(numeroMovimiento);

        // Actualizar la vista
        panelJuego.actualizarVistaTablero(tablero);
    }

    // Método para retroceder movimientos
    public void retrocederMovimeinto() {
        if (historialTableros.isEmpty() || numeroMovimiento <= 0) {
            JOptionPane.showMessageDialog(panelJuego, "No hay movimientos anteriores para retroceder.");
            return;
        }

        // Restaurar el último estado del tablero
        tablero = historialTableros.remove(historialTableros.size() - 1);

        // Restaurar ficha capturada si existía
        if (!historialCapturadas.isEmpty()) {
            Casilla casillaCapturada = historialCapturadas.remove(historialCapturadas.size() - 1);
            if (casillaCapturada != null && casillaCapturada.getFicha() != null) {
                // Restaurar la casilla capturada en su posición original
                tablero.getCasillaPorId(casillaCapturada.getId()).setFicha(casillaCapturada.getFicha());
            }
        }

        // Actualizar la vista
        panelJuego.reiniciarTablero(tablero);

        // Decrementar contador de movimientos y cambiar color
        numeroMovimiento--;
        cambiarColorMovimiento();
    }

    // Método auxiliar para copiar el tablero
    private Tablero copiarTablero(Tablero tableroOriginal) {
        Tablero tableroCopiado = new Tablero();
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Casilla casillaOriginal = tableroOriginal.getCasilla(fila, columna);
                Casilla casillaCopia = tableroCopiado.getCasilla(fila, columna);

                if (casillaOriginal.getFicha() != null) {
                    Ficha fichaCopia = Ficha.crearPieza(casillaCopia.getFicha().getTipoFicha(), casillaOriginal.getFicha().getColor());
                    fichaCopia.setPosicion(casillaOriginal.getFicha().getPosicion());
                    casillaCopia.setFicha(fichaCopia);
                }
            }
        }
        return tableroCopiado;
    }

    // Método modificado para manejar capturas
    private Casilla procesarMovimientoConCaptura(int numeroMovimiento) {
        // Procesar el movimiento
        movimiento.procesarMovimiento(numeroMovimiento, tablero);

        // Obtener los datos procesados
        String tipoFicha = movimiento.getTipoFicha();
        String destino = movimiento.getDestino();
        String origenColumna = movimiento.getOrigenColumna();
        String resultado = movimiento.getResultado();
        boolean captura = movimiento.isCaptura();
        boolean enroqueCorto = movimiento.isEnroqueCorto();
        boolean enroqueLargo = movimiento.isEnroqueLargo();

        // Manejar resultados finales
        if (resultado != null && (resultado.equals("1-0") || resultado.equals("0-1") || resultado.equals("1/2-1/2"))) {
            String mensaje = switch (resultado) {
                case "1-0" -> "¡Las blancas ganan!";
                case "0-1" -> "¡Las negras ganan!";
                case "1/2-1/2" -> "¡Es un empate!";
                default -> "Resultado desconocido.";
            };

            JOptionPane.showMessageDialog(panelJuego, mensaje);
            return null;
        }

        // Manejar enroques
        if (enroqueCorto || enroqueLargo) {
            cambiarColorMovimiento();
            manejarEnroque(enroqueCorto, enroqueLargo, isBlanca);
            return null;
        }

        // Obtener las coordenadas de la casilla de destino
        int filaDestino = 8 - Character.getNumericValue(destino.charAt(1));
        int columnaDestino = destino.charAt(0) - 'a';

        // Encontrar la casilla de origen
        Casilla origen = encontrarCasillaOrigen(tipoFicha, origenColumna, filaDestino, columnaDestino, captura);

        if (origen == null) {
            JOptionPane.showMessageDialog(panelJuego, "Movimiento inválido. No se encontró una ficha adecuada para mover.");
            return null;
        }

        // Manejar captura
        Casilla destinoCasilla = tablero.getCasilla(filaDestino, columnaDestino);
        Casilla casillaCapturada = null;
        if (destinoCasilla.getFicha() != null) {
            casillaCapturada = new Casilla(destinoCasilla.getId());
            casillaCapturada.setFicha(destinoCasilla.getFicha());
            historialCapturadas.add(casillaCapturada);
        }

        // Mover la ficha en el modelo
        if (tipoFicha.equals("P")) {
            origen.getFicha().cambiarMovimientoRealizado();
        }
        destinoCasilla.setFicha(origen.getFicha()); // Mover la ficha a la casilla destino
        destinoCasilla.getFicha().setPosicion(destino); // Actualizar la posición de la ficha
        origen.setFicha(null); // Vaciar la casilla de origen

        cambiarColorMovimiento();

        return casillaCapturada;
    }


    private Casilla encontrarCasillaOrigen(String tipoFicha, String origenColumna, int filaDestino, int columnaDestino, boolean captura) {

        colorActual = isBlanca ? "W" : "B";

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Casilla casilla = tablero.getCasilla(fila, columna);
                Ficha ficha = casilla.getFicha();

                if (ficha != null && ficha.getTipoFicha().equals(tipoFicha) && ficha.getColor().equals(colorActual)) {
                    // Validar que la ficha pueda llegar a la casilla destino
                    String posicionActual = tablero.convertirCoordenadasAId(fila, columna);
                    List<String> movimientosValidos = ficha.movimientosValidos(posicionActual, tablero);

                    if (movimientosValidos.contains(tablero.convertirCoordenadasAId(filaDestino, columnaDestino))) {
                        // Si se especificó la columna de origen, valida que coincida
                        if (origenColumna != null && !posicionActual.startsWith(origenColumna)) {
                            continue;
                        }

                        return casilla; // Ficha encontrada
                    }
                }
            }
        }

        return null; // No se encontró una ficha válida
    }

    private void manejarEnroque(boolean enroqueCorto, boolean enroqueLargo, boolean esBlanca) {
        int fila = esBlanca ? 0 : 7; // Fila 7 para blancas, fila 0 para negras

        if (enroqueCorto) {
            // Mover el rey y la torre para el enroque corto
            tablero.getCasilla(fila, 6).setFicha(tablero.getCasilla(fila, 4).getFicha());
            tablero.getCasilla(fila, 6).getFicha().setPosicion(esBlanca ? "g1" : "g8");
            tablero.getCasilla(fila, 4).setFicha(null);

            tablero.getCasilla(fila, 5).setFicha(tablero.getCasilla(fila, 7).getFicha());
            tablero.getCasilla(fila, 5).getFicha().setPosicion(esBlanca ? "f1" : "f8");
            tablero.getCasilla(fila, 7).setFicha(null);
        } else if (enroqueLargo) {
            // Mover el rey y la torre para el enroque largo
            tablero.getCasilla(fila, 2).setFicha(tablero.getCasilla(fila, 4).getFicha());
            tablero.getCasilla(fila, 2).getFicha().setPosicion(esBlanca ? "c1" : "c8");
            tablero.getCasilla(fila, 4).setFicha(null);

            tablero.getCasilla(fila, 3).setFicha(tablero.getCasilla(fila, 0).getFicha());
            tablero.getCasilla(fila, 3).getFicha().setPosicion(esBlanca ? "d1" : "d8");
            tablero.getCasilla(fila, 0).setFicha(null);
        }


        // Actualizar la vista
        panelJuego.actualizarVistaTablero(tablero);
        JOptionPane.showMessageDialog(panelJuego, "Enroque realizado.");
    }

    public void cambiarColorMovimiento() {
        isBlanca = !isBlanca;
    }
}
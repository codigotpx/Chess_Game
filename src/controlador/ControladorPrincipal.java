package controlador;

import funcionalidades.CargarIcon;
import funcionalidades.CargarPgn;

import modelo.*;
import vista.PanelJuego;

import javax.swing.*;
import java.util.List;

public class ControladorPrincipal {
    private JFrame marco;

    public ControladorPrincipal(JFrame marco) {
        this.marco= marco;
        iniciarComponentes();
    }

    public void iniciarComponentes() {
        Tablero tablero = new Tablero();
        CargarIcon cargarIcon = new CargarIcon();
        CargarPgn cargarPgn = new CargarPgn();
//        Movimiento movimiento = new Movimiento();
        PanelJuego panelJuego = new PanelJuego(tablero, cargarIcon);
        ControladorJuego controladorJuego = new ControladorJuego(tablero, cargarPgn, panelJuego );
        marco.setContentPane(panelJuego);
////
//        Caballo caballo = new Caballo("W");
//        tablero.getCasilla(2,1).setFicha(caballo);
//
//
//        // Obtener los movimientos válidos para el peón blanco
//        List<String> movimientos = caballo.movimientosValidos("g1", tablero);
//
//
//        // Mostrar los movimientos válidos
//        System.out.println("Movimientos válidos del Peón Blanco en a2:");
//        for (String movimientop : movimientos) {
//            System.out.println(movimientop);
//        }
    }
}

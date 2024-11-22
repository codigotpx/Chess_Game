package controlador;

import funcionalidades.CargarIcon;
import funcionalidades.CargarPgn;

import modelo.*;
import vista.PanelJuego;
import vista.TableroBoton;

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
        PanelJuego panelJuego = new PanelJuego(tablero, cargarIcon);
        TableroBoton tableroBoton = new TableroBoton(tablero, cargarIcon);
        ControladorJuego controladorJuego = new ControladorJuego(tablero, cargarPgn, panelJuego);
        ControladorJuegoE controladorJuegoE = new ControladorJuegoE(tableroBoton, tablero );
        marco.setContentPane(tableroBoton);
    }
}

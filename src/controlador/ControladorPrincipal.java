package controlador;

import funcionalidades.CargarFondo;
import funcionalidades.CargarIcon;
import funcionalidades.CargarPgn;

import modelo.*;
import vista.MenuPanel;
import vista.PanelJuego;
import vista.TableroBoton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorPrincipal {
    private JFrame marco;
    private PanelJuego panelJuego;
    private MenuPanel menuPanel;
    private TableroBoton tableroBoton;

    public ControladorPrincipal(JFrame marco) {
        this.marco= marco;
        iniciarComponentes();
    }

    public void iniciarComponentes() {
        Tablero tablero = new Tablero();
        CargarIcon cargarIcon = new CargarIcon();
        CargarPgn cargarPgn = new CargarPgn();
        CargarFondo cargarFondo = new CargarFondo();
        panelJuego = new PanelJuego(tablero, cargarIcon, cargarFondo);
        menuPanel = new MenuPanel(cargarFondo);
        tableroBoton = new TableroBoton(tablero, cargarIcon, cargarFondo);
        ControladorJuego controladorJuego = new ControladorJuego(tablero, cargarPgn, panelJuego);
        ControladorJuegoE controladorJuegoE = new ControladorJuegoE(tableroBoton, tablero );
        marco.setContentPane(menuPanel);

        menuPanel.addListenerBotonCargarPgn(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanelCargarJuego();
            }
        });

        menuPanel.addListenerBotonJugar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanelJugar();
            }
        });
    }


    public void mostrarPanelCargarJuego() {
        marco.setContentPane(panelJuego);
        marco.revalidate();
        marco.repaint();
    }

    public void mostrarPanelJugar() {
        marco.setContentPane(tableroBoton);
        marco.revalidate();
        marco.repaint();
    }
}

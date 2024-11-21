package funcionalidades;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CargarPgn {
    private List<String> movimientos;

    public CargarPgn() {
        this.movimientos = new ArrayList<>();
    }

    // Método para leer y procesar el archivo PGN
    public List<String> obtenerMovimientos(String nombreArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            StringBuilder contenido = new StringBuilder();

            // Leer el archivo completo, ignorando líneas de metadatos
            while ((linea = br.readLine()) != null) {
                if (!linea.startsWith("[") && !linea.endsWith("]")) {
                    contenido.append(linea).append(" ");
                }
            }

            // Eliminar numeraciones de movimientos y resultados
            String[] movimientosArray = contenido.toString()
                    .replaceAll("\\b\\d+\\.", "")     // Eliminar numeraciones como 1., 2., etc.
//                    .replaceAll("\\s*1-0\\s*|\\s*0-1\\s*|\\s*1/2-1/2\\s*", "") // Eliminar resultados
                    .trim()
                    .split("\\s+");                 // Dividir por espacios

            // Almacenar movimientos en la lista
            this.movimientos.clear();
            for (String movimiento : movimientosArray) {
                this.movimientos.add(movimiento);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.movimientos;
    }

    // Método para imprimir los movimientos (para pruebas)
    public void imprimirMovimientos() {
        for (String movimiento : movimientos) {
            System.out.print(movimiento + " ");
        }
    }
}

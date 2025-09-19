package uno;

import java.io.IOException;
import java.net.*;

public class Server {
    private static Pc pcCorrespondiente;

    public static void main(String[] args) {
        DatagramSocket ds = null;
        try {
            pcCorrespondiente = new Pc(9999);
            System.out.println("size pares: " + pcCorrespondiente.getPares().size());

            // Crear socket sin bind inicial
            ds = new DatagramSocket((SocketAddress) null);
            ds.setReuseAddress(true);
            ds.bind(new InetSocketAddress(9999));

            System.out.println("Escuchando en UDP en puerto 9999...");

            // Usar sólo un hilo para recibir, o si usas varios hilos usar el mismo socket
            Thread t = new hiloPc(ds, pcCorrespondiente);
            t.start();

            // Esperar que el hilo termine
            t.join();  // Si quieres que el servidor espere hasta que termine el hilo

        } catch (BindException be) {
            System.err.println("No se pudo iniciar el servidor porque la dirección/puerto ya están en uso: " + be.getMessage());
            be.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("El hilo fue interrumpido: " + e.getMessage());
            e.printStackTrace();
        }

        // Cerrar el socket explícitamente después de que el hilo termine
        if (ds != null && !ds.isClosed()) {
            ds.close();
            System.out.println("Socket cerrado correctamente.");
        }
    }
}

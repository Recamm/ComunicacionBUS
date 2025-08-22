import java.io.*;
import java.net.*;

public class Server {
    static Pc pcCorrespondiente;

    public Server() {
    }

    public static void main(String[] args) throws IOException {
        // Puerto el cual el servidor escucha
        ServerSocket ss = new ServerSocket(8888);

        System.out.println("Escuchando...");
        // Loop para esperar el packete llegante
        while (true) {
            Socket s = null;

            try {
                // El objeto socket recibe la peticion del cliente
                s = ss.accept();

                System.out.println("Mensaje recibido : " + s);

                // Obtiene los inpus y output streams
                // Contiene el mensaje
                DataInputStream dis = new DataInputStream(s.getInputStream());
                // Para responderle al que envio el msg
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Nuevo hilo asignado al cliente");

                // Crea un hilo nuevo
                Thread t = new hiloPc(s, dis, dos, pcCorrespondiente);

                // Usamos el metodo start()
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}
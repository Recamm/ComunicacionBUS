import java.io.*;
import java.net.*;

public class Server {
    private static Pc pcCorrespondiente;

    public static void main(String[] args) throws IOException {
        Inet4Address ipLocal = null;
        Pc pcLocal = new Pc(ipLocal.getLocalHost().getHostAddress(), 8888);
        // Las ip de las pc de los costados estan en 172.16.x.x porque esas x hay que
        // reemplazarlas por los numeros correspondientes a las ip de las pc
        // en caso de ser una pc de un extremo borrar la linea 14 y 16
        Pc pcCostado1 = new Pc("172.16.x.x", 8888);
        Pc pcCostado2 = new Pc("172.16.x.x", 8888);
        pcLocal.agregarPar(pcCostado1);
        pcLocal.agregarPar(pcCostado2);

        // Puerto el cual el servidor escucha
        ServerSocket ss = new ServerSocket(8888);

        System.out.println("Escuchando...");
        // Loop para esperar el packete llegante
        while (true) {
            Socket s = null;

            try {
                // El objeto socket recibe la peticion del cliente
                s = ss.accept();

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
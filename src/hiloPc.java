package hilos;

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

public class hiloPc extends Thread{
    final DataOutputStream dos;
    final Socket s;

    // Constructor
    public hiloPc(Socket s, DataOutputStream dos) {
        this.s = s;
        this.dos = dos;
    }

    @Override
    public void run() {
        String toreturn;
        while (true) {
            try {
                // Crear un objeto de fecha
                Date date = new Date();

                // Espera de 30 segundos
                this.sleep(5000);

                // Envio la fecha
                toreturn = fordate.format(date);
                dos.writeUTF("Fecha de hoy: " + toreturn);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

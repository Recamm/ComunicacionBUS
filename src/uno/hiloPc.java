package uno;

import java.io.*;
import java.net.*;

public class hiloPc extends Thread {
    final DatagramSocket ds;
    final Pc pcCorrespondiente;

    public hiloPc(DatagramSocket ds, Pc pcCorrespondiente) {
        this.ds = ds;
        this.pcCorrespondiente = pcCorrespondiente;
    }

    public void recibirYVerificar() throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

        String ipLocal = ObtenerIP.getIPAddress().trim();

        while (true) {
            ds.receive(dp);
            String mensaje = new String(dp.getData(), 0, dp.getLength()).trim();

            InetAddress clientAddress = dp.getAddress();

            if (mensaje.startsWith(ipLocal + ":")) {
                String contenido = mensaje.substring(mensaje.indexOf(":") + 1).trim();
                System.out.println("Mensaje recibido: " + contenido);
            } else {
                for (Pc pcPar : pcCorrespondiente.getPares()) {
                    if (!pcPar.getIp().equals(clientAddress.getHostAddress())) {
                        pcPar.enviarMensaje(pcPar.getIp(), pcPar.getPuerto(), mensaje);
                    }
                }
            }
        }
    }


    @Override
    public void run() {
        try {
            recibirYVerificar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
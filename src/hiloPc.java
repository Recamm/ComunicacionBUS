import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

public class hiloPc extends Thread{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    final Pc pcCorrespondiente;


    // Constructor
    public hiloPc(Socket s, DataInputStream dis, DataOutputStream dos, Pc pcCorrespondiente) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.pcCorrespondiente = pcCorrespondiente;
    }

    public void recibirYVerificar(String ipDestino, int puertoDestino, String ipReenvio, int puertoReenvio) throws IOException {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(puertoDestino);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                ds.receive(dp);
                String mensaje = new String(dp.getData(), 0, dp.getLength());

                System.out.println("Mensaje recibido: " + mensaje);

                if (mensaje.startsWith(ipDestino)) {
                    System.out.println("Este mensaje es para esta PC");
                    System.out.println(dis.readUTF());
                } else {
                    InetAddress ip = InetAddress.getByName(ipReenvio);
                    DatagramPacket dpReenvio = new DatagramPacket(dp.getData(), dp.getLength(), ip, puertoReenvio);
                    ds.send(dpReenvio);
                    System.out.println("Mensaje reenviado a: " + ipReenvio + ":" + puertoReenvio);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al recibir o reenviar mensaje: " + e.getMessage());
        }
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }
}
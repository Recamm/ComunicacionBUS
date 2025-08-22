import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

public class hiloPc extends Thread{
    final Socket s;
    final DataInputStream dis;
    final DataOutputStream dos;
    final Pc pcCorrespondiente;


    // Constructor
    public hiloPc(Socket s, DataInputStream dis, DataOutputStream dos, Pc pcCorrespondiente) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.pcCorrespondiente = pcCorrespondiente;
    }

    public void recibirYVerificar(String ipDestino, int puertoDestino) throws IOException {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(puertoDestino);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                ds.receive(dp);
                String mensaje = new String(dp.getData(), 0, dp.getLength());

                System.out.println("Mensaje recibido: " + mensaje);

                // Obtener la dirección IP y el puerto de la pc que envio el mensaje
                 InetAddress clientAddress = s.getInetAddress();
                 int clientPort = s.getPort();
                 // clientAddress.getHostAddress() clientPort);


                if (mensaje.startsWith(ipDestino)) {
                    System.out.println("Este mensaje es para esta PC");
                    System.out.println(dis.readUTF());
                } else {
                    for(Pc pc: pcCorrespondiente.getPares()){
                        if(!pc.getIp().equals(clientAddress.getHostAddress())  && pc.getPuerto() != clientPort){
                            pc.enviarMensaje(pc.getIp(), pc.getPuerto(), mensaje);
                        }
                    }
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
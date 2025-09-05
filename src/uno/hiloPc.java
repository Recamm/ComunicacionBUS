package uno;

import java.io.*;
import java.net.*;

public class hiloPc extends Thread{
    final Socket s;
    final DataInputStream dis;
    final DataOutputStream dos;
    final Pc pcCorrespondiente;
    private static Boolean whilehilo = true;


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
                 // int clientPort = s.getPort();
                 // clientAddress.getHostAddress() clientPort);


                if (mensaje.startsWith(ipDestino)) {
                    System.out.println("Este mensaje es para esta PC");
                    System.out.println(dis.readUTF());
                    hiloPc.whilehilo = false;
                } else {
                    for(Pc pcPar: pcCorrespondiente.getPares()){
                        if(!pcPar.getIp().equals(clientAddress.getHostAddress())){
                            pcPar.enviarMensaje(pcPar.getIp(), pcPar.getPuerto(), mensaje);
                        }
                    }
                    hiloPc.whilehilo = false;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al recibir o reenviar mensaje: " + e.getMessage());
        }
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }

    @Override
    public void run(){
        while(whilehilo){
            try{
                recibirYVerificar(Pc.getIp(), pcCorrespondiente.getPuerto());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
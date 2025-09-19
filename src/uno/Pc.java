package uno;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Scanner;

public class Pc {

    private String ip;
    private int puerto;
    private HashSet<Pc> pares;

    // Constructor
    public Pc(int puerto) {
        this.ip = ObtenerIP.getIPAddress();  // Obtenemos la IP de esta PC
        this.puerto = puerto;
        this.pares = new HashSet<>();
    }

    public Pc(String ip, int puerto) {
        this.ip = ip;
        this.puerto = puerto;
        this.pares = new HashSet<>();
    }

    public Pc(String ip, int puerto, HashSet<Pc> pares) {
        this.ip = ip;
        this.puerto = puerto;
        this.pares = pares;
    }

    public static String getIp() {
        return ObtenerIP.getIPAddress();  // Obtener IP dinámica cada vez
    }

    public int getPuerto() {
        return puerto;
    }

    public HashSet<Pc> getPares() {
        return pares;
    }

    public void setPares(HashSet<Pc> pares) {
        this.pares = pares;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public void agregarPar(Pc nuevo){
        this.pares.add(nuevo);
    }

    public void modificarPar(Pc nuevo, Pc viejo){
        this.pares.add(nuevo);
        this.pares.remove(viejo);
    }

    public void eliminarPar(Pc viejo){
        this.pares.remove(viejo);
    }

    public void enviarMensaje(String ipDestino, int puertoDestino, String msg) throws IOException {
        DatagramSocket ds = null;
        try {
            if (!ipDestino.equals(this.ip) && !ipDestino.equals(InetAddress.getLocalHost().getHostAddress())) {
                ds = new DatagramSocket();
                // Si el mensaje ya tiene el separador, no volver a agregar la IP destino
                String mensajeConIP;
                if (msg.contains(":")) {
                    mensajeConIP = msg;
                } else {
                    mensajeConIP = ipDestino + ":" + msg;
                }
                for(Pc p : this.pares){
                    InetAddress ip = InetAddress.getByName(p.ip);
                    DatagramPacket dp = new DatagramPacket(mensajeConIP.getBytes(), mensajeConIP.length(), ip, puertoDestino);
                    ds.send(dp);
                    System.out.println("Mensaje enviado a " + p.ip);
                }
            } else {
                System.out.println("No podes enviarte un mensaje a vos mismo");
            }
        } catch (IOException e) {
            System.err.println("Error al enviar el mensaje: " + e.getMessage());
        }
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }


    public static void main(String[] args) {
        // Crear la PC local y los pares de comunicación
        Pc pcLocal = new Pc(8888);
        Boolean condicionWhile = true;
        Scanner scn = new Scanner(System.in);
        String msg, ipDes;
        int puertoDes;

        Pc pcCostado1 = new Pc("172.16.4.170", 9999);
        Pc pcCostado2 = new Pc("172.16.4.200", 9999);
        pcLocal.agregarPar(pcCostado1);
        pcLocal.agregarPar(pcCostado2);

        // Bucle para enviar mensajes
        while (condicionWhile) {
            msg = scn.nextLine();
            if (msg.toLowerCase().equals("exit")) {
                condicionWhile = false;
            } else {
                // Solicitar IP y puerto del destinatario
                System.out.println("Se solicita la ip y luego el puerto de la PC a la que se le quiere enviar el mensaje.");
                System.out.println("Ejemplo de ip y puerto:");
                System.out.println(Pc.getIp());
                System.out.println("8888");
                ipDes = scn.nextLine();
                String puertoDesSTR = scn.nextLine();
                puertoDes = Integer.parseInt(puertoDesSTR);
                try {
                    pcLocal.enviarMensaje(ipDes, puertoDes, msg);
                    System.out.println();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        scn.close();
    }
}
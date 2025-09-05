package uno;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Scanner;

public class Pc {
    // Sacar static a ip y al metodo
    private static String ip = ObtenerIP.getIPAddress("eno2");
    private int puerto;
    private HashSet<Pc> pares;

    public Pc(int puerto) {
        this.puerto = puerto;
        this.pares = new HashSet<>();
    }

    public Pc(String ip, int puerto) {
        Pc.ip = ip;
        this.puerto = puerto;
        this.pares = new HashSet<>();
    }

    public Pc(String ip, int puerto, HashSet<Pc> pares) {
        Pc.ip = ip;
        this.puerto = puerto;
        this.pares = pares;
    }

    public Pc(int puerto, HashSet<Pc> pares) {
        this.puerto = puerto;
        this.pares = pares;
    }

    public static String getIp() {
        return ip;
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
            if (!ipDestino.equals(Pc.ip) && !ipDestino.equals(InetAddress.getLocalHost().getHostAddress())) {
                ds = new DatagramSocket();
                InetAddress ip = InetAddress.getByName(ipDestino);
                DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.length(), ip, puertoDestino);
                ds.send(dp);
                System.out.println("Mensaje enviado");
            }
            else{
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
        Pc pcLocal = new Pc(8888);
        Boolean condicionWhile = true;
        Scanner scn = new Scanner(System.in);
        String msg, ipDes;
        int puertoDes;

        while(condicionWhile){
            msg = scn.nextLine();
            if(msg.toLowerCase().equals("exit")){
                condicionWhile = false;
            }
            else{
                System.out.println("Se solicita la ip y luego el puerto de la pc a la que se le quiere enviar el msg.");
                System.out.println("Ejemplo de ip y puerto:");
                System.out.println(Pc.ip);
                System.out.println("8888");
                ipDes = scn.nextLine();
                String puertoDesSTR = scn.nextLine();
                puertoDes = Integer.parseInt(puertoDesSTR);
                try{
                    pcLocal.enviarMensaje(ipDes, puertoDes, msg);
                    System.out.println();
                } catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        scn.close();
    }
}

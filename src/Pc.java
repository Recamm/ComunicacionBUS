import java.io.*;
import java.net.*;
import java.util.HashSet;

public class Pc {
    private String ip;
    private int puerto;
    private HashSet<Pc> pares;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
        if(!ipDestino.equals(this.ip) || puertoDestino != this.puerto){
            DatagramSocket ds = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(ipDestino);
            DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.length(), ip, puertoDestino);
            ds.send(dp);
            ds.close();
        }
    }

}

package uno;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

public class ObtenerIP {
    public static String getIPAddress(String nombreInterfaz) {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(nombreInterfaz);
            if (networkInterface != null) {
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress address = interfaceAddress.getAddress();
                    if (address != null && !address.isLoopbackAddress() && address.getAddress().length == 4) { // IPv4
                        return address.getHostAddress();
                    }
                }
            } else {
                System.out.println("La interfaz '" + nombreInterfaz + "' no fue encontrada.");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        NetworkInterface es una clase que representa interfaces de red, algunos tipos serian
        Ethernet (cableada), wifi, visuales, etc.

        InterfaceAddres es una clase que representa una ip de la interfaz. La interfaz tiene
        muchas ip como la direccion, la mascara y la broadcast.

        networkInterface.getInterfaceAddresses() es un metodo de NetworkInterface que devuelve
        una lista con todas las ip de una interfaz y luego con un for las recorremos hasta
        encontrar la que necesitamos
     */
}

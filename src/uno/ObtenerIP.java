package uno;

import java.net.*;
import java.util.Enumeration;

public class ObtenerIP {
    public static String getIPAddress() {
        try {
            // Obtener todas las interfaces de red disponibles en la máquina
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                // Asegurarse de que la interfaz esté activa y no sea la interfaz de loopback
                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    // Recorrer todas las direcciones de la interfaz
                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                        InetAddress address = interfaceAddress.getAddress();

                        // Asegurarse de que la dirección sea IPv4 y no sea loopback
                        if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                            return address.getHostAddress();  // Retornar la primera IP válida encontrada
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;  // Si no se encuentra ninguna IP, devolver null
    }

    public static void main(String[] args) {
        String ip = getIPAddress();  // Obtener la IP

        if (ip == null) {
            System.out.println("No se encontraron direcciones IP.");
        } else {
            System.out.println("Dirección IP encontrada: " + ip);
        }
    }
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

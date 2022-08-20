package com.dimas519.chargingprotection.Service.Connection;

import java.io.IOException;
import java.net.*;

public class ClientConnection {
    private InetAddress IPAddress;
    private int port;

    private DatagramSocket clientSocket;

    public ClientConnection( String IPAddress, int port, int timeout )   {
        this.port= port;

        try {
            this.IPAddress=InetAddress.getByName(IPAddress);
            this.clientSocket=new DatagramSocket();
            this.clientSocket.setSoTimeout(timeout); // 5detik
        } catch (SocketException e) {

        } catch (UnknownHostException e) {

        }
    }


    public String sendToServer(String ekspresi) throws SocketTimeoutException,IOException{
        DatagramSocket socket=this.clientSocket;

            byte sendData[] = ekspresi.getBytes();
            int panjangData = sendData.length;
            DatagramPacket paket = new DatagramPacket(sendData, panjangData, IPAddress, port);
                //kirimkan keserver

                socket.send(paket);

                //paket yang diterima
                byte receiveData[] = new byte[1024];

                //  datagram raw-data dari jaringan
                DatagramPacket paketDatang = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(paketDatang);
                //ambil responsenya

                String responServer = new String(paketDatang.getData(), 0, paketDatang.getLength());

                return responServer;

    }

    public void sendToServerWithoutResponse(String ekspresi) throws SocketTimeoutException,IOException{
        DatagramSocket socket=this.clientSocket;

        byte sendData[] = ekspresi.getBytes();
        int panjangData = sendData.length;
        DatagramPacket paket = new DatagramPacket(sendData, panjangData, IPAddress, port);
        //kirimkan keserver

        socket.send(paket);


    }
}

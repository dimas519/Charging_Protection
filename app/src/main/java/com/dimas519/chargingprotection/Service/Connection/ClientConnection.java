package com.dimas519.chargingprotection.Service.Connection;

import java.io.IOException;
import java.net.*;


public class ClientConnection {
    private InetAddress IPAddress;
    private final int port;

    private DatagramSocket clientSocket;

    public ClientConnection( String IPAddress, int port, int timeout )   {
        this.port= port;

        try {
            this.IPAddress=InetAddress.getByName(IPAddress);
            this.clientSocket=new DatagramSocket();
            this.clientSocket.setSoTimeout(timeout); // 5detik
        } catch (SocketException e) {
            //do something socket Exception
        } catch (UnknownHostException e) {
            //do unknown Exception
        }
    }


    public String sendToServer(String ekspresi) throws IOException {
        DatagramSocket socket=this.clientSocket;

            byte[] sendData= ekspresi.getBytes();
            int panjangData = sendData.length;
            DatagramPacket paket = new DatagramPacket(sendData, panjangData, IPAddress, port);
                //kirimkan keserver

                socket.send(paket);

                //paket yang diterima
                byte[] receiveData= new byte[1024];

                //  datagram raw-data dari jaringan
                DatagramPacket paketDatang = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(paketDatang);
                //ambil responsenya dna kembalikan

                return new String(paketDatang.getData(), 0, paketDatang.getLength());

    }

    public void sendToServerWithoutResponse(String expression) throws IOException{
        DatagramSocket socket=this.clientSocket;

        byte[] sendData = expression.getBytes();
        int panjangData = sendData.length;
        DatagramPacket paket = new DatagramPacket(sendData, panjangData, IPAddress, port);
        //kirimkan keserver

        socket.send(paket);


    }
}

package tictactoe;

import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;

public class SocketsTCP {
    private ServerSocket server_socket;
    private Socket working_socket;
    private boolean isServer = false;
    private boolean isConnected = false;
    private String host;
    private String port;

    SocketsTCP() {
        getIpAndPort();
        try {
            working_socket = new Socket(host.trim(), Integer.parseInt(port.trim()));
            isConnected = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível se conectar ao IP indicado.\nPressione [OK] para gerar um novo endereço de IP e Porta automaticamente...", null, JOptionPane.PLAIN_MESSAGE , null);
            try {
                java.net.InetAddress i = java.net.InetAddress.getLocalHost();  
                host = i.getHostAddress();
                JOptionPane.showMessageDialog(null, "Aguardando cliente...\nIP: "+ host +"\nPort: " + port, null, JOptionPane.PLAIN_MESSAGE , null);
                server_socket = new ServerSocket(Integer.parseInt(port.trim()));
                isServer = true;
                working_socket = server_socket.accept();
                isConnected = true;
            } catch (Exception err) {
                JOptionPane.showMessageDialog(null, err.getMessage(), null, JOptionPane.PLAIN_MESSAGE , null);
            }
        }
    }
    
    public void sendMsg(byte[] msg) {
        try {
            OutputStream ostream = working_socket.getOutputStream();
            ostream.write(msg);
        } catch (Exception err) {
             JOptionPane.showMessageDialog(null, err.getMessage(), null, JOptionPane.PLAIN_MESSAGE , null);
        }
    }

    public byte[] recvMsg() {
        byte[] msg = new byte[100];
        try {
            InputStream istream = working_socket.getInputStream();
            istream.read(msg);
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err.getMessage(), null, JOptionPane.PLAIN_MESSAGE , null);
        }
        return msg;
    }

    public void sendStringMsg(String msg) {
    	try {
            OutputStream ostream = working_socket.getOutputStream();
            PrintWriter pw = new PrintWriter(ostream, true);
            pw.println(msg);
        } catch (Exception err) {
             JOptionPane.showMessageDialog(null, err.getMessage(), null, JOptionPane.PLAIN_MESSAGE , null);
        }
    }
    
    public String recvStringMsg() {
        try {
            InputStream istream = working_socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(istream));
            return br.readLine();
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err.getMessage(), null, JOptionPane.PLAIN_MESSAGE , null);
        }
        return "";
    }
    
    public void sendInt(int val) {
    	try {
            OutputStream ostream = working_socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(ostream);
            dos.writeInt(val);
        } catch (Exception err) {
             JOptionPane.showMessageDialog(null, err.getMessage(), null, JOptionPane.PLAIN_MESSAGE , null);
        }
    }
    
    public int recvInt() {
        try {
            InputStream istream = working_socket.getInputStream();
            DataInputStream dis = new DataInputStream(istream);           
            return dis.readInt();
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err.getMessage(), null, JOptionPane.PLAIN_MESSAGE , null);
        }
        return 0;
    }
    
   boolean isServer() {
       return isServer;
   }
   
   boolean isConnected() {
       return isConnected;
   }
   void close(){
        try {
            working_socket.close();
        } catch (Exception err) {
                JOptionPane.showMessageDialog(null, err.getMessage(), null, JOptionPane.PLAIN_MESSAGE , null);
        }
    }
   //Método que permite o jogador inserir um IP e porta
   void getIpAndPort(){
            host = JOptionPane.showInputDialog("Informe o IP:");
            port = JOptionPane.showInputDialog("Informe a Porta:");
    }
}
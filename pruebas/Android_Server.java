/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package android_server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Leidy
 */
public class Android_Server extends JFrame implements ActionListener, Runnable {

    private JPanel panelCentral;
    private JPanel panelInferior;
    private JPanel panelGeneral;
    private JPanel panelSuperior;
    private JPanel organizadorBtn;
    private JPanel organizadorTxt;
    
    private JButton btnSend;
    private JTextArea txtMessage;
    private JScrollPane scrollPanel;
    
    private static JLabel lblConexion;
    private static JTextArea txtPanel; //En este Label se mostrara el mensaje que el cliente envie.
    private static String message="Hola";
    private static String sendMessage;
    
    private Thread hiloServer;
    
    private static final int port = 7777;
    
    public Android_Server() {
        
        panelSuperior = new JPanel();
        panelCentral = new JPanel();
        panelInferior = new JPanel();
        panelGeneral = new JPanel();
        organizadorBtn = new JPanel();
        organizadorTxt = new JPanel();
        
        lblConexion = new JLabel();
        txtMessage = new JTextArea("");
        btnSend = new JButton("Enviar");
        txtPanel = new JTextArea("");
        txtPanel.setEditable(false);
        scrollPanel = new JScrollPane(txtPanel);
        
        panelGeneral = (JPanel) this.getContentPane();
        panelGeneral.setLayout(new BorderLayout());
        panelGeneral.add(panelSuperior, BorderLayout.NORTH);
        panelGeneral.add(panelCentral, BorderLayout.CENTER);
        panelGeneral.add(panelInferior, BorderLayout.SOUTH);
                
        panelSuperior.setLayout(new FlowLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Estado de conexión"));
        panelSuperior.add(lblConexion);
        
        panelCentral.setLayout(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Mensajes"));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.add(txtPanel, BorderLayout.CENTER);
        panelCentral.add(scrollPanel, BorderLayout.EAST);
        
        organizadorTxt.setLayout(new FlowLayout(FlowLayout.LEFT));
        organizadorTxt.setBackground(Color.WHITE);
        organizadorTxt.add(txtMessage);
        organizadorBtn.setLayout(new FlowLayout(FlowLayout.RIGHT));
        organizadorBtn.setBackground(Color.WHITE);
        organizadorBtn.add(btnSend);
        panelInferior.setLayout(new GridLayout(1, 2));
        panelInferior.add(txtMessage);
        panelInferior.add(organizadorBtn);

        btnSend.addActionListener(this);
        
        setSize(500, 180);
        setLocationRelativeTo(null);
        setTitle("Servidor");
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        hiloServer = new Thread(this);
        hiloServer.setName("Hilo servidor");
        hiloServer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        sendMessage = txtMessage.getText().toString();
        txtPanel.setText("Server: " + sendMessage);
        txtMessage.setText("");
    }

    public static void main(String[] args) {

        new Android_Server();
    }

    @Override
    public void run() {
        try {

            lblConexion.setText("Esperando cliente...");
            ServerSocket server = new ServerSocket(port);
            Socket client = server.accept();
            lblConexion.setText("Cliente conectado!");

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out;

            while (true) {

                out = new PrintWriter(client.getOutputStream(), true);
                                
                try {
                    message = in.readLine();
                    
                    if(message != null){
                        txtPanel.setText("Client: " + message);
                    }

                } catch (IOException e) {
                    System.out.println("La lectura del dato fallo");
                }
                
                out.println(sendMessage);
                out.flush();
                Thread.sleep(1000);
            }

        } catch (InterruptedException ex) {
            System.out.println("Problemas");
        } catch (IOException ex) {
            lblConexion.setText("Problemas con la conexión.");
        }
    }
}

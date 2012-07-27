
import com.rapplogic.xbee.XBeeConnection;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.XBeeTimeoutException;
import com.rapplogic.xbee.api.zigbee.ZNetTxRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.ws.Response;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * XbeeFrame.java
 *
 * Created on 02-feb-2012, 0:11:22
 */
/**
 *
 * @author ALDAJO
 */
public class XbeeFrame extends javax.swing.JFrame {

    /** Creates new form XbeeFrame */
    //Declaro las variables de instancia a utilizar
    XBee xbee;
    String puerto;
    XBeeAddress64 routeraddr;
    String text = "";               //datos del jtextarea
    String byite;                   //datos del jtextfield
    String status = "";             //datos de la transmisión
    String stringTextToShow;
    String stringAnswer;
    int[] stringtext;               //arreglo de valores para ser enviados
    int[] answer;                   //arreglo de valores correspondientes a la respuesta recibida.
    double angleToSend;
    double xCoordenate;
    double yCoordenate;
    double v1;
    double v2;
    double v3;
    int salidaVel[];
    
    public XbeeFrame() throws XBeeException{
        initComponents();
        // creo un objeto xbee de la clase XBee
        xbee = new XBee();
        
        try{
        //Solicito al usuario ingresar el nombre del puerto
            puerto = JOptionPane.showInputDialog("Favor ingrese el nombre del puerto\n(En mayúsculas)");
            //con el objeto, abro el puero de comunicación serial
            xbee.open(puerto, 9600);
        }catch(XBeeException ex){
        }
        routeraddr = new XBeeAddress64(0x00,0x13,0xA2,0x00,0x40,0x6F,0xF6,0xB4);    //dirección del router de prueba
        
    }//Cierre del constructor
    
    //Creo un método para enviar y recibir los mensajes
    private int[] sendMS(XBeeAddress64 addr64, int[] s, int timeout){
        //creo el objeto que me permite enviar el mensaje
        ZNetTxRequest request = new ZNetTxRequest(addr64, s);
        
        try{
            //envío el mensaje
            XBeeResponse resp = xbee.sendSynchronous(request, timeout);
            
             return resp.getRawPacketBytes();
        } catch (XBeeTimeoutException ex){
            
        } catch(XBeeException ex){
            
        }       
        return null;
    }//Cierre del método sendMS
    
    public void sendMA(XBeeAddress64 addr64, int[] s) {
        //XBeeAddress64 addr64 = new XBeeAddress64(0x00, 0x13, 0xa2, 0x00, 0x40, 0x4e, 0xcf, 0x13);

        //int[] stringToInts = stringToInts(s);
        ZNetTxRequest request = new ZNetTxRequest(addr64, s);

        try {
            //envía el mensaje
            //ZNetTxStatusResponse response = (ZNetTxStatusResponse)
            xbee.sendAsynchronous(request);

        } catch (XBeeTimeoutException ex) {
            Logger.getLogger(XbeeFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XBeeException ex) {
            Logger.getLogger(XbeeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    //método empleado para obtener los caracteres por separado de una cadena de texto
    private int[] stringToInts(String cadena) {
        char[] toCharArray = cadena.toCharArray();
        int salida[] = new int[toCharArray.length];
        for (int i = 0; i < toCharArray.length; i++) {
            salida[i] = toCharArray[i];
        }
        return salida;
    }//cierre del método stringToInts
    
    //método empleado para convertir los bytes de un arreglo en un formato hexadecimal
    private String toHexa(int[] values){
        String totalString="";                                  //almacena la cadena de bytes
        String byteregister;                                    //almacena el formato del byte en hexadecimal
        for(int i=0;i<values.length;i++){                       //se convierte a cada byte con el formato de escritura que se desea mostrar
            byteregister = Integer.toHexString(values[i]);
            totalString = totalString + "0x" + byteregister + " ";
        }
        return totalString;
    }//cierre del método toHexa
    
    private String answuerTransmitStatus(int[] values){         //tener cuidado con el método ya que debe leer un arreglo de 11 valores
        int state = values[8];
        String deliveryStatus;
        switch (state){
            case 0x00: deliveryStatus = "Success";
                break;
            case 0x01: deliveryStatus = "MAC ACK Failure";
                break;
            case 0x02: deliveryStatus = "CCA Failure";
                break;
            case 0x24: deliveryStatus = "Address Not Found";
                break;
            default: deliveryStatus = "There Are Problems";
                break;
        }
        return deliveryStatus;
    }
    //MÉTODO que adiciona un signo al byte a enviar
    private int byteSign(double a){
        int returnSign=0;
        if(a>=0){
            returnSign = 0;
        }else{
            returnSign = 1;
        }
        return returnSign;
    }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonEnviar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        controlToMove1 = new ControlToMove();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        buttonEnviar.setText("Enviar");
        buttonEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEnviarActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        controlToMove1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                controlToMove1MouseReleased(evt);
            }
        });
        controlToMove1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                controlToMove1MouseDragged(evt);
            }
        });

        javax.swing.GroupLayout controlToMove1Layout = new javax.swing.GroupLayout(controlToMove1);
        controlToMove1.setLayout(controlToMove1Layout);
        controlToMove1Layout.setHorizontalGroup(
            controlToMove1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 284, Short.MAX_VALUE)
        );
        controlToMove1Layout.setVerticalGroup(
            controlToMove1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonEnviar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                        .addComponent(controlToMove1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonEnviar))
                        .addGap(0, 227, Short.MAX_VALUE))
                    .addComponent(controlToMove1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEnviarActionPerformed
// TODO add your handling code here:
    text = jTextArea1.getText();                                 //almacena los datos del jtextarea
    byite = jTextField1.getText();                               //capturo los datos del jtextfield
    stringtext = stringToInts(byite);
    stringTextToShow = toHexa(stringtext);
//    int payload[] = new int[] {intbyte};                       //almaceno el valor ingresado en forma de byte    
    answer = sendMS(routeraddr, stringtext, 9000);         //recibo el valor emitido por el xbee
//    int byteanswerrergister = answer[0];
    stringAnswer = toHexa(answer);
    //se analiza la respuesta
    status = answuerTransmitStatus(answer);
    jTextArea1.setText(text + "Cadena: " + byite + "\n" + "Bytes Enviados: " + stringTextToShow + 
                       "\nTransmisión: " + status + "!" + "\nBytes Respuesta: " + stringAnswer + "\n");    
    jTextField1.setText("");                                    //se limpia el jtextfield
    stringtext = null;
    answer = null;
    
}//GEN-LAST:event_buttonEnviarActionPerformed

private void controles1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_controles1MouseDragged
    
}//GEN-LAST:event_controles1MouseDragged

private void controles1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_controles1MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_controles1MouseReleased

    private void controlToMove1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_controlToMove1MouseDragged
        // TODO add your handling code here:
    text = jTextArea1.getText();                                 //almacena los datos del jtextarea
    angleToSend = controlToMove1.getAngleDegree();
    xCoordenate = ((controlToMove1.getXcordenate()*90)/58);
    yCoordenate = ((controlToMove1.getYcordenate()*90)/58);
    v1 = ((-xCoordenate)/2)+((0.866)*yCoordenate);
    v2 = ((-xCoordenate)/2)-((0.866)*yCoordenate);
    v3 = (xCoordenate);
    
    salidaVel = new int[7];
    salidaVel[0] = 0xF8;
    salidaVel[1] = byteSign(v1);
    salidaVel[2] = Math.abs((int)v1);
    salidaVel[3] = byteSign(v2);
    salidaVel[4] = Math.abs((int)v2);
    salidaVel[5] = byteSign(v3);
    salidaVel[6] = Math.abs((int)v3);
/*  
    salidaVel[1] = 
*/
    sendMA(routeraddr, salidaVel);
//    answer = sendMS(routeraddr, salidaVel, 9000);
//    status = answuerTransmitStatus(answer);
    
    jTextArea1.setText(text + "velocidad X: " + xCoordenate + "  velocidad Y: " + yCoordenate + 
                       "\nTransmisión emitida... \n");
    
    System.out.println("velx: "+xCoordenate + " vely: "+ yCoordenate);
    
    xCoordenate = 0;
    yCoordenate = 0;
    stringtext = null;
    answer = null;
    salidaVel = null;
//    System.out.println("x: "+xCoordenate+" y: "+yCoordenate);
    }//GEN-LAST:event_controlToMove1MouseDragged

    private void controlToMove1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_controlToMove1MouseReleased
        // TODO add your handling code here:
    salidaVel = new int[7];
    salidaVel[0] = 0xF8;
    salidaVel[1] = 0;
    salidaVel[2] = 0;
    salidaVel[3] = 0;
    salidaVel[4] = 0;
    salidaVel[5] = 0;
    salidaVel[6] = 0;
    
    sendMA(routeraddr, salidaVel);
    salidaVel = null;
    }//GEN-LAST:event_controlToMove1MouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(XbeeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(XbeeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(XbeeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(XbeeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    new XbeeFrame().setVisible(true);
                } catch (XBeeException ex) {
                    Logger.getLogger(XbeeFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonEnviar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private ControlToMove controlToMove1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

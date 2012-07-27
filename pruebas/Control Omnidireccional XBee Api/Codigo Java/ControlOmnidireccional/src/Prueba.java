
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress16;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.wpan.TxRequestBase.Option;
import com.rapplogic.xbee.api.zigbee.ZNetTxRequest;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ALDAJO
 */
public class Prueba {
    
    
//    XBee xbee;
//    String puerto;
//    XBeeAddress64 remoteAddress;
//    XBeeResponse data;
    int[] dataToSend;
    
    public Prueba() throws XBeeException{ //constructor de la clase
    }
    public static void main(String[] args) throws XBeeException {
      int[] dataToSend = null;
      XBee xbee = new XBee();
      xbee.open("COM9", 9600);
      XBeeAddress64 remoteAddress = new XBeeAddress64(0x00, 0x13, 0xA2, 0x00, 0x40, 0x6F, 0xF6, 0xB4);
      XBeeAddress16 remoteAddress16 = new XBeeAddress16(0x65, 0xF3);
      XBeeResponse data = xbee.sendSynchronous(new ZNetTxRequest(remoteAddress, null));
      
//      data = xbee.sendSynchronous(new ZNetTxRequest(0x10, remoteAddress, remoteAddress16, 0x00, XBeeAddress64.BROADCAST, dataToSend));
      dataToSend = new int[0];
      dataToSend[0]=0x03;
      data.setRawPacketBytes(dataToSend);
    }
}

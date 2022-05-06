
package Cliente;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author domin
 */
public class CerrarConexionServer implements ActionListener {
    
    private String nombre;
    private Socket socketServicio;
    private PrintWriter outPrinter;
    private ArrayList<ChatPriv> chats;
    
    
    CerrarConexionServer(Socket socketServicio, String nombre, ArrayList<ChatPriv> chats){
        this.socketServicio = socketServicio;
        this.nombre = nombre;
        this.chats=chats;
        
        try{
            this.outPrinter = new PrintWriter(socketServicio.getOutputStream(),true);
            
        }catch(IOException e){
            System.out.print("Error al abrir salida para mensaje");
        }
        
        
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e){
        
                        /*Avisamos a todos los demás clientes que tengan ventana Privada con éste
                         de que cierren las ventanas Privadas, así no hay conflicto y el cliente si 
                         cierra su sesión sin cerrar las ventanas Privadas, se cerrarán automaticamente
                         las ventans privadas que los otros clientes tengan abiertas con él
                         */
                         
                         for(int i =0; i<this.chats.size();i++){
                             outPrinter.println("1004#"+nombre+"#"+this.chats.get(i).getNombreGuest()+"#");
                         }
        
                          /*Ahora sí, una vez finalizadas las ventanas privadas, cerramos sesión con el servidor*/
                         this.outPrinter.println("2000#"+this.nombre +"#");
        
    }
    
    
    
}


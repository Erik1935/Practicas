
package Cliente;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author domin
 */
public class ChatPriv extends JFrame{
    private Socket socketCliente;
    private String nombreHost;
    private String nombreGuest;
    private PrintWriter outPrinter;
    
    //Adicionales de ventana
    private Border border;
    private JTextArea tfLog;
    private JTextField tfMensaje;
    private JButton bEnviar;
    private JButton bCerrar;
    
    public ChatPriv(Socket socket, String nombreHost, String nombreGuest){
        super("Usuario: "+nombreHost+ " - Chat privado con "+nombreGuest);
        
        this.socketCliente=socket;
        this.nombreHost=nombreHost;
        this.nombreGuest=nombreGuest;
        
        try{
            this.outPrinter = new PrintWriter(this.socketCliente.getOutputStream(),true);
        }catch(IOException e){
            System.out.print("Error al abrir stream escritura");
        }
        
        
        //Construcción de la ventana
        //Panel de contencion de JFrame
        Container content = getContentPane();
        //Indicamos el tipo de layout de la ventana
        content.setLayout(new BorderLayout());
        //fijamos el tipo de borde
        border=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        
        JPanel pCenter = _crearPCenter();
        content.add(pCenter, BorderLayout.CENTER);
        
        //creamos el panel sur
        
        JPanel pSouth = _crearPSur();
        content.add(pSouth, BorderLayout.SOUTH);
        
        //damos tamaño y ponemos la ventana visible
        this.setSize(450,300);
        this.setLocation(420,100);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //No dejamos cerrar la ventana. Solo si le damos al botón de cerrar conversación
        
        
        
        this.getRootPane().setDefaultButton(bEnviar);
        tfMensaje.requestFocusInWindow();
        
        
        this.bEnviar.addActionListener(new ActionListener(){
        
         @Override
            public void actionPerformed(ActionEvent e) {
                
                outPrinter.println("1003#"+nombreHost+"#"+nombreGuest+"#"+ tfMensaje.getText()+"#");
                tfMensaje.setText("");
            }
        
        
        });
        
        this.bCerrar.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                //Mandamos código de finalización de sesión privada.
                //El servidor contestará a ambos clientes. 
                outPrinter.println("1004#"+nombreHost+"#"+nombreGuest+"#");
                
            }
            
        });
        
        
        
        
    }
    
    private JPanel _crearPCenter(){
        //Agregamos borde de título para la conversación.
        JPanel p =  new JPanel(new GridLayout(1,1));
        JPanel p1 = new JPanel(new BorderLayout());

        TitledBorder titleBorder = BorderFactory.createTitledBorder(border,"Log de Conexión");
        p1.setBorder(titleBorder);
     
        //Este será el registro de la conversación. La ventana "Scrolleable" que mantendrá la conversación
        tfLog = new JTextArea();
        //Hacemos que no se puede editar el textArea
        tfLog.setEditable(false);
        
        JScrollPane scroll = new JScrollPane(tfLog); 
        p1.add(scroll, BorderLayout.CENTER);
      
        p.add(p1);
        
        
        return p;
    }
    
     private JPanel _crearPSur(){
        //Agregamos borde de título para la conversación.
        JPanel p = new JPanel(new GridLayout(1,2));
        JPanel aux = new JPanel(new GridLayout(1,2));
        TitledBorder titleBorder = BorderFactory.createTitledBorder(border,"Mensaje");
        
        p.setBorder(titleBorder);
        
        tfMensaje =  new JTextField();
        p.add(tfMensaje, BorderLayout.CENTER);
        
        bEnviar =  new JButton("Enviar");
        bEnviar.setBackground(Color.GREEN);
        bEnviar.setForeground(Color.BLACK);
        aux.add(bEnviar, BorderLayout.EAST);
        
        bCerrar = new JButton("Cerrar");
        bCerrar.setBackground(Color.red);
        bCerrar.setForeground(Color.BLACK);
        aux.add(bCerrar, BorderLayout.EAST);
        
        p.add(aux);
        
        
        return p;
    }
    
    public Socket getSocketCliente() {
        return socketCliente;
    }

    public String getNombreHost() {
        return nombreHost;
    }

    public String getNombreGuest() {
        return nombreGuest;
    }
     
     
    protected void agregaComentario(String mensaje){
        this.tfLog.append(mensaje+"\n");
    }
     
    
    public void terminaSesion(){
        this.setVisible(false);//Cerramos ventana
    }
     
    public void terminaSesionConAviso(){
        try{
            this.tfLog.append("El usuario "+this.nombreGuest+" ha cerrado el chat\n");
            TimeUnit.SECONDS.sleep(1);
            this.tfLog.append("La sesión terminará en 5 segundos...\n");
            TimeUnit.SECONDS.sleep(5);
            this.setVisible(false);//Cerramos ventana
        }catch(InterruptedException e){
            System.out.print("Error en la terminación de la sesión con delay");
        }
        
    }
     
     
  

    
}
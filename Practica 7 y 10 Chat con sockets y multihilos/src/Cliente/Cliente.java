
package Cliente;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JTextArea;

public class Cliente  extends JFrame{

    //Atributos básicos de conexión
    private Socket socketServicio;
    private String nombre;
    private int port;
    private String host;
    
    
    //Atributos de chats privados
    private ArrayList<ChatPriv> chatsPrivados;
    
    //Atributos privados de la vista de Cliente
    
    private Border border;
    private JTextField tfNick;
    private JTextField tfMensaje;
    private JButton bEnviar;
    private JButton bLogout;
    private JButton bRefrescar;
    private JTextArea tfLog;
    private List lstUsuarios;
    
    //Variable booleana para saber si estamos online o solicitamos el logout
    protected boolean online = false;
    
    //Ventana de configuracion
    private VentConf vc;
 
   
    
    
    public Cliente(){
        super("Cliente");
        
        //Iniciamos contenedor de chats privados
        this.chatsPrivados = new ArrayList<>();
        
        
        //Panel de contencion de JFrame
        Container content = getContentPane();
        //Indicamos el tipo de layout de la ventana
        content.setLayout(new BorderLayout());
        //fijamos el tipo de borde
        border=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        

        
        //creamos el panel central
        
        JPanel pCenter = _crearPCenter();
        content.add(pCenter, BorderLayout.CENTER);
        
        //creamos el panel sur
        
        JPanel pSouth = _crearPSur();
        content.add(pSouth, BorderLayout.SOUTH);
        
        //damos tamaño y ponemos la ventana visible
        this.setSize(500,500);
        this.setLocation(420,100);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //No cerramos al pulsar en la X de cerrar. Obligamos a que hagan logout
        //Aunque se podría haber añadido otro actionListener al cierre que haga lo mismo que el logout....pero bueno, así espero que sea suficiente
        
         //Ventana de configuracion inicial
        vc = new VentConf(this);
        this.host = vc.getHost();
        this.port = vc.getPuerto();
        this.nombre = vc.getUsuario();
        
        this.setTitle("Usuario: "+nombre);
        
        
        
        try {
            this.socketServicio = new Socket(host, port);
        } catch (UnknownHostException ex) {
            System.out.print("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        } catch (IOException ex) {
            System.out.print("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        }
        tfLog.append("Conexion establecida con el servidor.\n");
        tfLog.append("Usuario: "+nombre+"\n");
        
        
        try{//ENVIO DE LOS DATOS DE CONEXIÓN
             PrintWriter outPrinter = new PrintWriter(this.socketServicio.getOutputStream(),true);
             
             String info_conexion = "1000#"+this.nombre+"#"+this.socketServicio.getLocalAddress()+"#"+this.socketServicio.getLocalPort()+"#";
             outPrinter.println(info_conexion);
             
            
             
         }catch(IOException e){
             System.out.print("No se pudo abrir el buffer de lectura/escritura");
         }
        
        
        
        
        
        
        
        
        //this.bRefrescar.addActionListener(new ListenerRefrescoUsuarios());
        //Acción para enviar
        bEnviar.addActionListener(new ConexionServer(socketServicio,tfMensaje, nombre));
        bLogout.addActionListener(new CerrarConexionServer(socketServicio, nombre, chatsPrivados));
        lstUsuarios.addActionListener(new ActionListener(){
        
        
            @Override
            public void actionPerformed(ActionEvent e) {
               int index = lstUsuarios.getSelectedIndex();
               String cadenaRecogida = lstUsuarios.getItem(index);
               String[]cadenas = cadenaRecogida.split(" ");
               
               /*
               COMPROBACIÓN DE QUE NO SE ABRE UN CHAT PRIVADO CONTIGO MISMO
               */
               
               if(cadenas[0].equals(nombre)){
                  
               //AÑADIR UN JDIALOG PARA QUE INFORME DEL ERROR
               //OPCIONAL. SI DA TIEMPO BIEN
                   
                   
               }else{
               
               /*
                   
                AQUÍ VIENE LA COMPROBACIÓN DE QUE ESA VENTANA DE CHAT NO EXISTE YA, PARA NO CREAR DOS VENTANAS
                CON EL MISMO USUARIO
                   
                */
                  
               //La cadenas[0] tendrá el nombre del Guest al que queremos conectarnos. 
               //Creamos ahora una nueva ventana de chat privado y se la añadimos al cliente
               ChatPriv ventana = new ChatPriv(socketServicio, nombre, cadenas[0]);
               chatsPrivados.add(ventana);
               
               
               }
               
               
               
            }
            
            //CON ESTO YA HEMOS TERMINADO LA PARTE DE ABRIR UNA NUEVA VENTANA DE CHAT PRIVADO
        
        
        });
        
        //Con getRootPane().setDefaultButton, hacemos que el botoón de Enviar sea el que se pulse por defecto al pulsar Enter
        this.getRootPane().setDefaultButton(bEnviar);
        //Ponemos el cursor en el campo del mensaje, para escribir directamente
        tfMensaje.requestFocusInWindow();
    }
    
    
    public Cliente(String nombre){
        this.nombre = nombre;
    }
    
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getLogin(){
        
        return this.tfNick.getText();
    }
  

 
    
    private JPanel _crearPCenter(){
        //Agregamos borde de título para la conversación.
        JPanel p =  new JPanel(new GridLayout(1,2));
        JPanel p1 = new JPanel(new BorderLayout());
        JPanel p2 =  new JPanel(new BorderLayout());
        
        TitledBorder titleBorder = BorderFactory.createTitledBorder(border,"Log de Conexión");
        TitledBorder titleBorderUsuarios = BorderFactory.createTitledBorder(border,"Usuarios Conectados");
        p1.setBorder(titleBorder);
        p2.setBorder(titleBorderUsuarios);
        
        
        
                
        //Este será el registro de la conversación. La ventana "Scrolleable" que mantendrá la conversación
        tfLog = new JTextArea();
        //Hacemos que no se puede editar el textArea
        tfLog.setEditable(false);
        
        JScrollPane scroll = new JScrollPane(tfLog); 
        p1.add(scroll, BorderLayout.CENTER);
        
        
        //Lista de usuarios conectados
        lstUsuarios =  new List();
        p2.add(lstUsuarios, BorderLayout.CENTER);
                
        p.add(p1);
        p.add(p2);
        
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
        
        bLogout = new JButton("Logout");
        bLogout.setBackground(Color.red);
        bLogout.setForeground(Color.BLACK);
        aux.add(bLogout, BorderLayout.EAST);
        
        p.add(aux);
        
        return p;
    }
     
     
     public void recibirMensajes(){
         
         String mensajeRecibido;
         BufferedReader inReader = null;
         this.online = true;
         
         try{
             
             inReader =  new BufferedReader(new InputStreamReader(this.socketServicio.getInputStream()));
                   
         }catch(IOException e){
             System.out.print("No se pudo abrir el buffer de lectura/escritura");
         }
         
        
         
         do{
             
             try{
                 
                 mensajeRecibido = inReader.readLine();
                 //PARSEAMOS OTRA VEZ EL TEXTO Y LO SEPARAMOS
                 String[] cadena = mensajeRecibido.split("#");
                 String codigo = cadena[0];
                 
                 String mensaje="";
                 
                 switch(codigo){
                     case "1001"://Cógido de mensaje compartido en chat
                         
                         
                         for(int i =1; i<cadena.length; i++){
                             mensaje+=cadena[i];
                         }
                         
                         this.tfLog.append(mensaje+"\n");
                     break;
                     
                     case "1002"://Código de actualización de usuarios conectados
                         lstUsuarios.removeAll();
                         String nom="";
                         String direc="";
                         String p="";
                         
                         for(int i =1; i<cadena.length; i+=3){
                             mensaje=cadena[i]+" "+cadena[i+1]+" "+cadena[i+2];
                             lstUsuarios.add(mensaje);
                         }
                         
                        
                         
                     break;
                     
                     
                     case "1003": //Codigo de mensaje Privado
                         /*
                         Contiene los nombres de los clientes emisor y receptor, así como el mensaje. 
                         */
                         boolean terminado = false;
                         int indice=-1;
                         
                         if(cadena[1].equals(this.nombre)){
                             //Yo soy el que lo ha enviado
                             for(int i=0; i< this.chatsPrivados.size()&&!terminado; i++){
                                if(this.chatsPrivados.get(i).getNombreGuest().equals(cadena[2])){
                                 indice=i;
                                 terminado = true;
                                }
                             }
                             
                             this.chatsPrivados.get(indice).agregaComentario(this.nombre +": "+ cadena[3]);
                             
 
                         }else if(cadena[2].equals(this.nombre)){
                             //Yo soy el receptor del mensaje
                            
                             for(int i=0; i< this.chatsPrivados.size()&&!terminado; i++){
                                if(this.chatsPrivados.get(i).getNombreGuest().equals(cadena[1])){
                                 indice=i;
                                 terminado = true;
                                }
                             }
                             
                             if(indice==-1){//Quiere decir que no existe dicha ventana de chat privado creada. Hemos de abrirla y crearla
                             //Además, siempre será el receptor del mensaje al que le pase esto. Dado que el emisor la ha tenido que abrir por cojones
                             ChatPriv ventana = new ChatPriv(socketServicio, nombre, cadena[1]);
                             ventana.agregaComentario(cadena[1] +": "+ cadena[3]);
                             chatsPrivados.add(ventana);

                             }else{
                                this.chatsPrivados.get(indice).agregaComentario(cadena[1] +": "+ cadena[3]);
                             }
                           
                         }

                     break;
                     
                     
                     
                     
                     
                     case "1004"://CODIGO DE CIERRE DE SESIÓN PRIVADA DE CHAT
                         //OJO QUE SE REPITE CÓDIGO. HACERLO MÁS REUTILIZABLE CREANDO OTROS MÉTODOS
                         
                         boolean terminado1 = false;
                         int indice1=-1;
                         
                         if(cadena[1].equals(this.nombre)){
                             //Yo soy el que lo ha enviado
                             for(int i=0; i< this.chatsPrivados.size()&&!terminado1; i++){
                                if(this.chatsPrivados.get(i).getNombreGuest().equals(cadena[2])){
                                 indice1=i;
                                 terminado1 = true;
                                }
                             }
                             
                             this.chatsPrivados.get(indice1).terminaSesion();
                             this.chatsPrivados.remove(indice1);
       
                         }else if(cadena[2].equals(this.nombre)){
                             //Yo soy el receptor del mensaje
                            
                             for(int i=0; i< this.chatsPrivados.size()&&!terminado1; i++){
                                if(this.chatsPrivados.get(i).getNombreGuest().equals(cadena[1])){
                                 indice1=i;
                                 terminado1 = true;
                                }
                             }
                             
                             this.chatsPrivados.get(indice1).terminaSesionConAviso();
                             this.chatsPrivados.remove(indice1);
                             
                         }
                         
                     break;
                     
                     
                     
                     
                     case "2001": //Codigo de cierre (logout) recibido desde el servidor
                         
                                                  
                         this.online = false;
                         
                         
                     break;
                 }
                 
                 
                 //this.tfLog.append(mensajeRecibido+"\n");
                 
             }catch(IOException e){
                 System.out.print("No se pudo recibir mensajes. Error al leer ");
                 online = false;
             }
             
             
         }while(online);
         
         //Cerramos conexión con el servidor
         
         try{
                     this.tfLog.append("Cerrando conexión con el servidor...\n");
                     inReader.close();
                     this.socketServicio.close();
                     this.tfLog.append("Conexión cerrada con el servidor...\n");
                     //System.exit(0);
                 }catch(IOException ex){
                     System.out.print("Error al cerrar stream de lectura");
                 }
         
         
     }
  
     
    
     
     
    
    
	public static void main(String[] args) {
		
            Cliente cliente= new Cliente();
            cliente.recibirMensajes();
            System.exit(0);
		
                      
		} 
	
}
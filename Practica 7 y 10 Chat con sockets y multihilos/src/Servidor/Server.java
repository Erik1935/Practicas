
package Servidor;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server{

       
        
        
    
	public static void main(String[] args) {
                
                //Identificación del servidor y parámetros
                int puerto = 8989;
                
                                
                ServerSocket servidor = null;
                Socket socketConexion = null;
                
                //Va a recibir mensajes de cualquier tipo. Ya sean peticiones ó los propios mensajes, luego los parseará
                MensajesRecibidos mensajes = new MensajesRecibidos();
                //Inicializamos la lista de usuarios online
                UsuariosActivos usuariosOnline = new UsuariosActivos();
                
                
                //Empezamos proceso de apertura del puerto de escucha y se queda a la espera de recibir conexiones
                
                try{
                    servidor = new ServerSocket(puerto);
                   
                    do{
                        
                        System.out.print("Servidor escuchando en el puerto "+ puerto+"\n");
                        System.out.print("Servidor a la espera de conexiones...\n");
                    
                        socketConexion = servidor.accept();
                        System.out.print("Conexión aceptada...");
                        
                        
                        
                        //Creamos una conexión del cliente, donde se van a gestionar los flujos de entrada y salida de datos
                        ProcesarConexion conexion = new ProcesarConexion(socketConexion, mensajes, usuariosOnline);
                    
                        //iniciamos el proceso de recepción de mensajes desde la conexión. Además, conexión 
                        conexion.start(); 
                        //Una hebra entrará en el método run de ProcesadorConexion, que se sobrescribe al heredar de Thread
                        //La original seguirá en el bucle recibiendo nuevas conexiones
                       }while(true);
                    
                }catch(IOException e){
                System.err.println("Error al escuchar en el puerto "+puerto);
                }
                
           
                

	}

}

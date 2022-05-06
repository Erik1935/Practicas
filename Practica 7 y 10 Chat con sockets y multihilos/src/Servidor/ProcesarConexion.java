
package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;


//
// Nota: si esta clase extendiera la clase Thread, y el procesamiento lo hiciera el método "run()",
// ¡Podríamos realizar un procesado concurrente! 
//
public class ProcesarConexion extends Thread implements Observer{
	// Referencia a un socket para enviar/recibir las peticiones/respuestas
	private Socket socketServicio;
	// stream de lectura y escritura(por aquí se recibe lo que envía el cliente)
	private PrintWriter outPrinter;
        private BufferedReader inReader;
        //Mensajes recibidos para su procesamiento
        private MensajesRecibidos mensajes;
        
        private UsuariosActivos usuariosOnline;
       

	// Constructor que tiene como parámetro una referencia al socket abierto en la conexión con el servidor, y los mensajes, aunque mensajes de momento está "vacío" hasta que los reciba
	public ProcesarConexion(Socket socketServicio, MensajesRecibidos mensajes, UsuariosActivos usuariosOnline) {
		this.socketServicio=socketServicio;
                this.mensajes = mensajes;
                
                this.usuariosOnline = usuariosOnline;
                
                // Abrimos flujos de lectura y escritura
                try{
                    
                this.outPrinter = new PrintWriter(this.socketServicio.getOutputStream(),true);
                this.inReader = new BufferedReader(new InputStreamReader(this.socketServicio.getInputStream()));
                
                }catch(IOException e){
                    System.out.print("No se pudieron abrir los flujos de entrada/salida");
                }
	}
	
	
	// Aquí es donde se realiza el procesamiento realmente:
        @Override
        public void run(){ //Esto lo hacemos porque la clase hebra tiene un método run() que es el que inicia la hebra

		String mensajeRecibido;
                boolean online = true;
                
                //Esto es importante. Apuntamos a la lista de observadores de mensajes al objeto ProcesadorConexión
                mensajes.addObserver(this);
                
                
                //Añadimos a la lista de observadores de usuarios a ésta conexión
                usuariosOnline.addObserver(this);
                
                do{
                    
                
		try {
			
                        
                            //Quedamos a la espera de leer el mensaje recibido
                           mensajeRecibido = inReader.readLine();
                           String[] cadena = mensajeRecibido.split("#");
                           String codigo = cadena[0];
                          
                           //A partir de aquí, pues bueno, se podría hacer una función adicional que se le pasara
                           //el código y resolviera, para que quede más bonito, pero bueno. Hacer si da tiempo
                           
                           //Se podría haber hecho un switch. Cambiar si eso, ya que en el cliente sí que he hecho un switch
                           if(codigo.equals("1000")){
                               //CODIGO DE INFO CONEXIÓN PARA CREAR EL CLIENTE
                               ClientConect cliente = new ClientConect(cadena[1],cadena[2],Integer.parseInt(cadena[3]), this.socketServicio);
                               //Agregamos un cliente. Además, agregaCliente llama a NotifyObservers para modificar en los clientes los usuarios conectados
                               usuariosOnline.agregaCliente(cliente);
                               
                           }else if (codigo.equals("1001")){//CODIGO DE ENVIO DE MENSAJE A TODOS LOS CLIENTES
                                                              
                               mensajes.setMensaje(mensajeRecibido);
                               
                           }else if(codigo.equals("1003") || codigo.equals("1004")){//CODIGO DE ENVIO DE MENSAJE PRIVADO || CODIGO DE CIERRE DE SESIÓN PRIVADA
                               
                               //cadena[1] contiene nombre del emisor
                               //cadena[2] contiene nombre receptor
                               int indiceEmisor =-1;
                               int indiceReceptor = -1;
                               
                               for(int i = 0; i<this.usuariosOnline.getClientes().size(); i++){
                                   if(this.usuariosOnline.getClientes().get(i).getNombre().equals(cadena[1])){
                                       indiceEmisor=i;
                                   }
                                   if(this.usuariosOnline.getClientes().get(i).getNombre().equals(cadena[2])){
                                       indiceReceptor=i;
                                   }
                                   
                               }
                               
                               if(indiceEmisor == -1 || indiceReceptor == -1){
                                   //Quiere decir que algún usuario ya no está logueado en el sistema
                                   //Pues no se envía el mensaje
                                   
                                   //MEJOR MANDAR UN ERROR AL CLIENTE EMISOR DICIENDO QUE ESE OTRO CLIENTE YA NO ESTA ON
                               }else{
                                   //Abrimos streams de escritura para ambos
                                   PrintWriter auxPrinterEmisor =  new PrintWriter(this.usuariosOnline.getClientes().get(indiceEmisor).getSocketCliente().getOutputStream(), true);
                                   PrintWriter auxPrinterReceptor =  new PrintWriter(this.usuariosOnline.getClientes().get(indiceReceptor).getSocketCliente().getOutputStream(), true);
                                   
                                   //Escribimos el mensaje en los streams
                                   
                                   auxPrinterEmisor.println(mensajeRecibido);
                                   auxPrinterReceptor.println(mensajeRecibido);
                                   /*
                                   auxPrinterEmisor.close();
                                   auxPrinterReceptor.close();*/
                                   
                               }
                               

                          }else if(codigo.equals("2000")){
                               //CODIGO DE LOGOUT
                               
                               usuariosOnline.eliminaCliente(cadena[1]);
                               
                               //Mandamos terminación al cliente para que cierre también él
                               this.outPrinter.println("2001");
                               
                               
                                 
                               
                               
                               online = false;
                           }
                                                      
                        
                                      
		}catch (IOException e) {
			System.err.println("Error al obtener los flujos de entrada/salida.");
                        online = false;//Si no se ha podido establecer conexión, cerramos streams y ponemos online a false
                        
                        try{ 
                            outPrinter.close();
                            inReader.close();
                            
                        }catch(IOException ex){
                            System.out.print("Error al cerrar flujos de entrada/salida");
                        }
		}

                }while(online);
                
                try{ //Una vez salimos, cerramos streams
                            outPrinter.close();
                            inReader.close();
                            this.socketServicio.close();
                        }catch(IOException ex){
                            System.out.print("Error al cerrar flujos de entrada/salida");
                        }
                
                /*HABRÍA QUE CERRAR EL SOCKETSERVICIO UNA VEZ TERMINAMOS??
                *
                *
                *PREGUNTAR AL PROFESOR
                *
                */
	}

        
        //Sobreescrito de la interfaz Observer
        @Override
        public void update(Observable o, Object arg){
            
            // Envia el mensaje al cliente
           outPrinter.println(arg.toString());
            
        }
        
        
        
	
}

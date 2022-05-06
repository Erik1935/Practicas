
package Servidor;


import java.util.Observable;


public class MensajesRecibidos extends Observable{
    
    private String mensaje;
    
    public MensajesRecibidos(){}
    
    public String getMensaje(){
        return this.mensaje;
    }
    
    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
        // Indica que el mensaje ha cambiado
        this.setChanged();
        // Notifica a los observadores que el mensaje ha cambiado y se lo pasa
        // (Internamente notifyObservers llama al metodo update del observador)
        this.notifyObservers(this.getMensaje());
    }
    
    
}
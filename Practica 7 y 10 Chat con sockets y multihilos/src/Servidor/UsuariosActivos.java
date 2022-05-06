
package Servidor;

import java.util.ArrayList;
import java.util.Observable;

public class UsuariosActivos extends Observable {
    
    private ArrayList<ClientConect> usuarios;
    
    
    public UsuariosActivos(){
        this.usuarios = new ArrayList<>();
    }
    
    public ArrayList<ClientConect> getClientes(){
        return this.usuarios;
    }
    
    public void agregaCliente(ClientConect cliente){
        this.usuarios.add(cliente);
        
        String users ="1002#";//CODIGO DE ACTUALIZACIÓN DE USUARIOS
        for(int i=0;i<this.usuarios.size();i++){
            users+=this.usuarios.get(i).getNombre()+"#"+this.usuarios.get(i).getDireccion()+"#"+this.usuarios.get(i).getPort()+"#";
        }
        
        this.setChanged();
        
        this.notifyObservers(users);
    }
    
    
    public void eliminaCliente(String nombre){
        
        
        
        int indice = -1;
        boolean terminado = false;
        for(int i=0; i<this.usuarios.size() && !terminado; i++){
            if(this.usuarios.get(i).getNombre().equals(nombre)){
                indice = i;
                terminado = true;
            }
        }
        
        if(indice != -1)
         this.usuarios.remove(indice);  
        
        String users ="1002#";//CODIGO DE ACTUALIZACIÓN DE USUARIOS
        for(int i=0;i<this.usuarios.size();i++){
            users+=this.usuarios.get(i).getNombre()+"#"+this.usuarios.get(i).getDireccion()+"#"+this.usuarios.get(i).getPort()+"#";
        }
        
        this.setChanged();
        
        this.notifyObservers(users);
        
    }
    
        
}

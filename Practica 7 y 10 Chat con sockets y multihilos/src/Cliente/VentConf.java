
package Cliente;


import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class VentConf extends JDialog{
    

    private JTextField tfUsuario;
    private JTextField tfHost;
    private JTextField tfPuerto;
  
    private JLabel lbUsuario;
    private JLabel lbHost; 
    private JLabel lbPuerto;
        
    private JButton btAceptar;
        
    //Creo JDialog cuyo "padre" será la ventana del cliente
    public VentConf(JFrame VentanaCliente) {
        super(VentanaCliente, "Configuracion", true);
 
        
        tfUsuario = new JTextField();
        tfHost = new JTextField("localhost");
        tfPuerto = new JTextField("8989");

        
        lbUsuario = new JLabel("Usuario:");
        lbHost = new JLabel("Host:");
        lbPuerto = new JLabel("Puerto:");
        
        //Botón de aceptar la configuración indicada
        btAceptar = new JButton("Listo!");
        btAceptar.addActionListener(new ActionListener() { //Otra forma de usar los listeners llamando a un función como argumento
        
            
                   
            
            //Cuando pulsemos en Listo! desaparece la ventana
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        //Con setDefaultButton del RootPane, decimos que el botón por defecto al pulsar ENTER sea el de Listo!
        this.getRootPane().setDefaultButton(btAceptar);
        
        Container content = this.getContentPane();
        content.setLayout(new GridBagLayout());
        
        
        JPanel pCenter = _crearPCenter();
        content.add(pCenter);
        
        this.setSize(250, 250); // Seteamos el tamaño de la ventana
        this.setLocation(450, 200); // Posicion de la ventana
        this.setResizable(false); // Evita que se pueda estirar la ventana
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Deshabilita el boton de cierre de la ventana 
        this.setVisible(true);
    }
    
    
    public JPanel _crearPCenter(){
        JPanel p =  new JPanel(new GridLayout(4,1));
        JPanel p1 = new JPanel(new GridLayout(1,2));
        JPanel p2 =  new JPanel(new GridLayout(1,2));
        JPanel p3 = new JPanel(new GridLayout(1,2));
        JPanel p4 = new JPanel(new GridLayout(1,1));
        
        
        p1.add(this.lbUsuario);
        p1.add(this.tfUsuario);
        
        p2.add(this.lbHost);
        p2.add(this.tfHost);
        
        p3.add(this.lbPuerto);
        p3.add(this.tfPuerto);
        
        p4.add(this.btAceptar);
        
        p.add(p1);
        p.add(p2);
        p.add(p3);
        p.add(p4);
        
        return p;
    }
    
    
    public String getUsuario(){
        return this.tfUsuario.getText();
    }
    
    public String getHost(){
        return this.tfHost.getText();
    }
    
    public int getPuerto(){
        return Integer.parseInt(this.tfPuerto.getText());
    }

}

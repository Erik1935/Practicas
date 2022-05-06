import controlador.User.APIResponse;
import controlador.User.LoginRequest;
import controlador.userGrpc;
import controlador.userGrpc.userBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.swing.JOptionPane;

public class GrpcClient {

	public static void main(String[] args) {
		
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090).usePlaintext().build();
		
		// stubs - generate from proto
		
		userBlockingStub userStub = userGrpc.newBlockingStub(channel);
		
		LoginRequest loginrequest = LoginRequest.newBuilder().setUsername("RA").setPassword("RAM").build();
		
		APIResponse response = userStub.login(loginrequest);
		
//		System.out.println(response.getResponsemessage());
                if(response.getResponsemessage().equals("Sucess")){
                    JOptionPane.showMessageDialog(null,"Felicidades, estas usando grpc en Java");
                }else{
                    JOptionPane.showMessageDialog(null,"La contraseña no es valida");
		
                }
		channel.shutdown();
	}

}

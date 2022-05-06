import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import user.UserService;

public class GrpcServer {

	 public static void main(String args[]) throws IOException, InterruptedException {
		 
		 System.out.println("Iniciando el servidor");
		 Server server = ServerBuilder.forPort(9090).addService(

				 new UserService()).build();
		 
		 server.start();
		 System.out.println("El servidor se inico en el puerto: "+ server.getPort());
	        server.awaitTermination();
	 }
}

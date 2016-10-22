import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DemoColaCarrefour {
	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<Cliente> q = new ArrayBlockingQueue<Cliente>(1000);

        Runnable gc = new GeneradorClientes(30, 1, 20, q);
        Thread thClientes = new Thread(gc);
        thClientes.start();
        
        int id = 1;
        Thread[] cajero = new Thread[3];
        for(int i = 0; i < cajero.length; i++) {
            // TODO: Creamos y arrancamos los cajeros en sus Threads
            cajero[i] = new Thread(new Cajero(id, 3, 2, q));
            cajero[i].start();
            id++;
        }
        thClientes.join();
        	 
        if(thClientes.isAlive()){
        	thClientes.interrupt();
        	thClientes.join();
        	System.out.println("\n*****HORARIO COMERCIAL CUMPLIDO. CERRANDO PUERTAS.*****\n");	 
        }else{
        	System.out.println("\n*****HORARIO COMERCIAL CUMPLIDO. CERRANDO PUERTAS.*****\n");
        }
        
        cajero[0].join();
        cajero[1].join();
        cajero[2].join();
        
        for(int i = 0; i < cajero.length; i++) {
        	if(cajero[i].isAlive()){
            	cajero[i].interrupt();
            	cajero[i].join(); 
            	System.out.println("\nCERRANDO CAJERO "+(i+1)+". No hay clientes\n");
            }
        }
        System.out.println("\n**********CERRANDO SUPERMERCADO**********\n");
	}	
}

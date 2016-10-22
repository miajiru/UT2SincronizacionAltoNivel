import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GeneradorClientes implements Runnable {
	 private int clientesIniciales;
	   private int cantidadTotalClientes;
	   private int clientesPorSegundo;
	   private long tiempoMaximo;
	   private BlockingQueue q;
		
	   public GeneradorClientes(int clientesIniciales, int clientesPorSegundo, long tiempoMaximo, BlockingQueue q) {
		super();
		this.clientesIniciales = clientesIniciales;
	    this.cantidadTotalClientes = clientesIniciales;
		this.clientesPorSegundo = clientesPorSegundo*1000;
		this.tiempoMaximo = tiempoMaximo;
		this.q = q;
	    }
		
	    @Override
	   public void run() {	
		System.out.println("Arranca clientes");
		long tiempoInicial = System.nanoTime();
	        
		for(int i = 1; i <= clientesIniciales; i++){	//Cargamos los primeros 30 clientes
	            q.offer(new Cliente(i));
		} 
		while (((System.nanoTime() - tiempoInicial) / 1000000000) < tiempoMaximo) {         
	            try {
	                cantidadTotalClientes++;
	                // TODO: esperar y generar cliente según "clientesPorSegundo".
	                Thread.currentThread().sleep(clientesPorSegundo);
	                q.offer(new Cliente(cantidadTotalClientes),3, TimeUnit.SECONDS);
	                System.out.println("Cliente "+cantidadTotalClientes+" a la cola");
	                    if(q.remainingCapacity() == 0){   
	                        Thread.currentThread().interrupt();//Se interrumpe porque la cola esta llena
	                        System.out.println("La cola esta llena, no caben más clientes");
	                        break;
	                    }
	            
	            } catch (InterruptedException e) {
	               e.printStackTrace();
	            }
		}
		Thread.currentThread().interrupt();//Se interrumpe tiempo maximo cumplido  
		
	}

}


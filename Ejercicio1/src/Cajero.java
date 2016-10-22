import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cajero implements Runnable {
	private int id;
    private int tiempoMaximoPorCliente;
    private int maximaEspera; // tiempo máximo que estaremos sin atender antes de cerrar caja.
    private BlockingQueue q;
    private Cliente clienteEnCaja;
    final Lock lock=new ReentrantLock();
    final Condition ocupado=lock.newCondition();
    final Condition libre =lock.newCondition();
	
    public Cajero(int id, int tiempoMaximoPorCliente, int maximaEspera, BlockingQueue q) {
	super();
	this.maximaEspera = maximaEspera;
	this.tiempoMaximoPorCliente = tiempoMaximoPorCliente;
	this.id = id;
	this.q = q;
    this.clienteEnCaja = null;
    }
   
	@Override
	public void run() {
            System.out.println("Arranca cajero "+this.id);
            Random rnd = new Random();
            boolean hayClientes = true;
            // TODO: mientras hayan clientes...
            lock.lock();
            try {
		while(hayClientes){
                    // esperamos un tiempo aleatorio entre 1segundo y tiempoMaximoPorCliente
                    long tiempoPorCliente = (long)(rnd.nextDouble() * tiempoMaximoPorCliente + 1);//En milisegundos
                    
                    ocupado.await(tiempoPorCliente,TimeUnit.SECONDS);//Atendiendo al cliente
                    // sacamos un cliente de la cola, imprimimos "CAJERO X ATENDIENDO CLIENTE Y"
                    clienteEnCaja = (Cliente)q.poll(maximaEspera,TimeUnit.SECONDS);
                    //Si hay cliente se le atiende
                    if(clienteEnCaja != null){
                    	long tiempoTotal = clienteEnCaja.getTiempoEspera() + tiempoPorCliente;//Tiempo total por cliente
                        System.out.println("CAJERO "+this.id+" atendiendo CLIENTE "+clienteEnCaja.getId()+" en "+tiempoTotal+" SEGUNDOS.");
                        libre.signalAll();//Caja libre  
                    }else{
                        hayClientes = false;//Cola vacia; salimos del bucle
                    }                
                }
			
            } catch (InterruptedException e) {
                   e.printStackTrace();
            }finally{
                lock.unlock();
            }   
           
            Thread.currentThread().interrupt(); //Interrumpimos cajero  
            System.out.println("\nCERRANDO CAJERO "+this.id+". No hay clientes\n");
	}
	
}

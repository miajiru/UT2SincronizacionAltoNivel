
public class Cliente {
	 private int id;
	    private long tiempoInicial;
	    private long tiempoFinal;

	    public Cliente(int id) {
		super();
		this.id = id;
		this.tiempoInicial = System.nanoTime()/1000000000;
	    }
		
	    private void setTiempoFinal() {
		this.tiempoFinal = (System.nanoTime()/1000000000);
	    }
		
	    public long getTiempoEspera() {
		if (this.tiempoFinal == 0L) {
	            setTiempoFinal();
		}
		return this.tiempoFinal - this.tiempoInicial;
	    }
		
	    public int getId(){
		return this.id;
	}
}

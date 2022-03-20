package distributedBank;

public class CurrencyConverter {
	CurrencyType original; 
	CurrencyType current; 
	
	public CurrencyConverter(CurrencyType original, CurrencyType current) {
		this.original = original; 
		this.current = current; 
	}
	
	public static Double getConversionRate() {
		double original_rate = original.rate(); 
		double current_rate = current.rate(); 
		
		double conversion_rate = original_rate / current_rate; 
		return Double.valueOf(conversion_rate); 
	}
}

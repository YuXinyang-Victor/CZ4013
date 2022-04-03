package distributedBank;

/**
 * Convert account currency, own designed for idempotent actions
 */
public class CurrencyConverter {
	CurrencyType original; 
	CurrencyType current; 
	
	public CurrencyConverter(CurrencyType original, CurrencyType current) {
		this.original = original; 
		this.current = current; 
	}
	
	public Double getConversionRate() {
		double original_rate = original.rate(); 
		double current_rate = current.rate(); 
		
		double conversion_rate = original_rate / current_rate; 
		return Double.valueOf(conversion_rate); 
	}
}

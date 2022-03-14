package distributedBank;

public enum CurrencyType {
	SGD(1), 
	CNY(4.66), 
	USD(0.73), 
	EUR(0.67); 

	private final double rate; 
	
	CurrencyType(double rate) {
		this.rate = rate; 
	}
	
	public double rate() {
		return rate;
	}
}

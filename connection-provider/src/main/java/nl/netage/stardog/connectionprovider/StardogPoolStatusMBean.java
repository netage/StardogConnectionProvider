package nl.netage.stardog.connectionprovider;

public interface StardogPoolStatusMBean {

	public String getstore();
	
	public String getserver();
	
	public String getuser();
	
	public int getnumActive();
	
	public int getmaxActive();
	
	public int getnumIdle();
	
	public int getminIdle();
	
}

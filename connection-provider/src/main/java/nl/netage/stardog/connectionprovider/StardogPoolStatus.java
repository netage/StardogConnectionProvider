package nl.netage.stardog.connectionprovider;

import org.apache.commons.pool.impl.GenericObjectPool;

import com.complexible.stardog.api.ConnectionConfiguration;



public class StardogPoolStatus implements StardogPoolStatusMBean {
	private GenericObjectPool<ConnectionConfiguration> internalPool=null;
	private String server,store,user;

	public StardogPoolStatus(String server, String store,String user ) {
		this.server = server;
		this.store = store;
		this.user = user;
	}
	
	public void setPool(GenericObjectPool<ConnectionConfiguration> internalPool) {
		this.internalPool = internalPool;
	}
	
	@Override
	public int getnumActive() {
		if(internalPool==null)
			return 0;
		return internalPool.getNumActive();
	}
	
	@Override
	public String getstore() {
		return store;
	}

	@Override
	public String getserver() {
		return server;
	}

	@Override
	public int getmaxActive() {
		if(internalPool==null)
			return 0;
		return internalPool.getMaxActive();
	}

	@Override
	public int getnumIdle() {
		if(internalPool==null)
			return 0;
		return internalPool.getNumIdle();
	}

	@Override
	public int getminIdle() {
		if(internalPool==null)
			return 0;
		return internalPool.getMinIdle();
	}

	@Override
	public String getuser() {
		return user;
	}

}

package nl.netage.stardog.connectionprovider;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.commons.pool.impl.GenericObjectPool;

import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.ConnectionPool;
import com.complexible.stardog.api.ConnectionPoolConfig;

public class StardogConnectionProvider {
	protected String store = null, server = null, user = null, password = null;
	protected int minSize = 10, maxSize = 1000, block = 60, expire = 3600;
	protected boolean usePool = false;

	protected ConnectionPool standardPool = null, reasonPool = null;
	private StardogPoolStatus poolInformation = null, reasonPoolInformation = null;

	public void setServer(String server) {
		this.server = server;
		setupWhenConfigured();
	}

	public void setStore(String store) {
		this.store = store;
		setupWhenConfigured();
	}

	public void setUser(String user) {
		this.user = user;
		setupWhenConfigured();
	}

	public void setPassword(String password) {
		this.password = password;
		setupWhenConfigured();
	}

	public void setMinSize(int minSize) {
		this.minSize = minSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public void setUsePool(boolean usePool) {
		this.usePool = usePool;
	}

	public String getServer() {
		return server;
	}

	public String getStore() {
		return store;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public Connection getConnection() {
		return getConnection(false);
	}

	public Connection getConnection(boolean reason) {
		try {

			if (reason) {
				if (reasonPool == null) {
					ConnectionConfiguration aConnConfig = ConnectionConfiguration.to(store).server(server)
							.credentials(user, password).reasoning(reason);

					ConnectionPoolConfig aConfig = ConnectionPoolConfig.using(aConnConfig).minPool(minSize)
							.maxPool(maxSize).expiration(expire, TimeUnit.SECONDS)
							.blockAtCapacity(block, TimeUnit.SECONDS);

					reasonPool = aConfig.create();

					// Reflect our way to the internal pool.
					Object mPool;
					Field field = reasonPool.getClass().getDeclaredField("mPool");
					field.setAccessible(true);

					mPool = field.get(reasonPool);
					Field field2 = mPool.getClass().getDeclaredField("mPool");
					field2.setAccessible(true);
					reasonPoolInformation.setPool((GenericObjectPool<ConnectionConfiguration>) field2.get(mPool));
				}
				return reasonPool.obtain();
			} else {
				if (standardPool == null) {
					ConnectionConfiguration aConnConfig = ConnectionConfiguration.to(store).server(server)
							.credentials(user, password);

					ConnectionPoolConfig aConfig = ConnectionPoolConfig.using(aConnConfig).minPool(minSize)
							.maxPool(maxSize).expiration(expire, TimeUnit.SECONDS)
							.blockAtCapacity(block, TimeUnit.SECONDS);

					standardPool = aConfig.create();

					// Reflect our way to the internal pool.
					Object mPool;
					Field field = standardPool.getClass().getDeclaredField("mPool");
					field.setAccessible(true);

					mPool = field.get(standardPool);
					Field field2 = mPool.getClass().getDeclaredField("mPool");
					field2.setAccessible(true);
					poolInformation.setPool((GenericObjectPool<ConnectionConfiguration>) field2.get(mPool));
					return standardPool.obtain();

				}
				return standardPool.obtain();
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void setupWhenConfigured() {
		if (store != null && server != null && user != null && password != null) {

			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName beanName;
			try {
				String serverName = this.server.substring(this.server.lastIndexOf('/')+1);
				beanName = new ObjectName("Stardog:type=Connections,server="+serverName+",store=" + this.store+",mode=NoReasoning");
				this.poolInformation = new StardogPoolStatus(server, store, user);
				mbs.registerMBean(this.poolInformation, beanName);
				beanName = new ObjectName("Stardog:type=Connections,server="+serverName+",store=" + this.store+",mode=Reasoning");
				this.reasonPoolInformation = new StardogPoolStatus(server, store, user);
				mbs.registerMBean(this.reasonPoolInformation, beanName);
			} catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException
					| NotCompliantMBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

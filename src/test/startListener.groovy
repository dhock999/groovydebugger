import com.boomi.connector.api.listen.Listener;
import com.boomi.connector.api.PayloadUtil;

//resources.resource1, resources.resource2 are persisted between start/stop listener
logger.info("logger startListener")
resources.resource1 = new SocketThread(listener)
Thread t = new Thread(resources.resource1);
t.start();

println "println startListener fini"

public class SocketThread implements Runnable {

	boolean _doStop=false;
	Listener _listener;
	
	SocketThread(Listener listener)
	{
		_listener = listener
	}
	
	public synchronized void doStop() {
		_doStop=true;
	}

	public void run() {
		try {
			StringBuilder packet = new StringBuilder();
			
			String line;
			while (!_doStop) {
				println "*** Waiting for message";
				_listener.submit(PayloadUtil.toPayload("Hello from a Groovy Listener"));
				Thread.sleep(60000);
				println "submit complete...looping"
			}
		} catch (Exception e) {
		}
		println "*** SocketStreamingUnmanagedListenOperation Thread Stopped ";
	}
	
}

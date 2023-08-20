import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import com.boomi.connector.api.OperationType;
import com.boomi.connector.testutil.ConnectorTester;
import com.boomi.connector.testutil.SimpleOperationResult;
import com.boomi.connector.groovyconnector.GroovyConnector;
import com.boomi.connector.groovyconnector.tester.MockOperationContext;
import com.boomi.connector.groovyconnector.tester.MockListener
import com.boomi.connector.groovyconnector.GroovyListenOperation;

println "GroovyOperationTest"

Map<String, Object> connProps = new HashMap<String,Object>();
connProps.put("url", "https://httpbin.org");
String objectTypeId = "dummy";
GroovyConnector connector = new GroovyConnector();
ConnectorTester tester = new ConnectorTester(connector);
println "hello from stdout"

Map<String, Object> opProps = new HashMap<String,Object>();
//connProps.put("executeOperation.groovy", executeUpdateScript);
opProps.put("path", "/get");
//tester.setOperationContext(OperationType.EXECUTE,connProps,opProps,"OBJECTID",null);
MockOperationContext context = new MockOperationContext(null, connector, OperationType.EXECUTE, connProps, opProps, objectTypeId, null, null);
context.setCustomOperationType("GET");
tester.setOperationContext(context);

List<InputStream> inputs = new ArrayList<InputStream>();
inputs.add(new ByteArrayInputStream("TEST INPUT to echo".toString().getBytes()));
//List <SimpleOperationResult> actual = tester.executeExecuteOperation(inputs);
//String responseString = new String(actual.get(0).getPayloads().get(0));
//println actual.get(0).getMessage() + " " + actual.get(0).getStatusCode()  + "\r\n" + responseString;

///LISTENER
def listener = new MockListener();
tester.setOperationContext(OperationType.LISTEN, connectionProperties, opProps, null, null);
def listenOperation = new GroovyListenOperation(tester.getOperationContext());
listenOperation.start(listener);

Thread.sleep(20000)
println "testsleep done"
listenOperation.stop()

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
import com.boomi.connector.groovyconnector.GroovyListenOperation;
import com.boomi.connector.groovyconnector.tester.MockOperationContext;
import com.boomi.connector.groovyconnector.tester.MockListener;
import com.boomi.connector.testutil.SimpleBrowseContext;

//Create an instance of the connector to test
//This constructor enables connector logging to be directed to the console
GroovyConnector connector = new GroovyConnector(out);

//Simulate user setting the URL in the Connection UI. Note connectionProperties is prefined
connectionProperties.put("url", "https://petstore3.swagger.io/api/v3");
ConnectorTester tester = new ConnectorTester(connector);

//A map to put Operation UI field values
Map<String, Object> opProps = new HashMap<String,Object>();

//Simulate that the user selected "Store Inventory" from the Object list after pressing the Import Button when creating the operation
String objectTypeId = "/store/inventory"

//tester.setOperationContext(OperationType.EXECUTE,connProps,opProps,"OBJECTID",null);
MockOperationContext context = new MockOperationContext(null, connector, OperationType.EXECUTE, connectionProperties, opProps, objectTypeId, null, null);

//Simulate a GET operation
context.setCustomOperationType("GET");
tester.setOperationContext(context);

//Create an Input Stream. This is ignored for a GET but for a POST, it would contain a mock input Document value
List<InputStream> inputs = new ArrayList<InputStream>();
inputs.add(new ByteArrayInputStream("A test document value".toString().getBytes()));

//Execute the operation and get the result
List <SimpleOperationResult> actual = tester.executeExecuteOperation(inputs);

//Test for a response document
assert actual.size()>0

//Get the first Document coming out of the connector
String responseString = new String(actual.get(0).getPayloads().get(0));
println actual.get(0).getMessage() + " " + actual.get(0).getStatusCode()  + "\r\n" + responseString

//Assert that the document value is not blank
assert responseString != ""

//BROWSER
SimpleBrowseContext sbc = new SimpleBrowseContext(null, connector, OperationType.EXECUTE, "GET", connectionProperties, null);
tester.setBrowseContext(sbc);

//actual = tester.browseTypes();

///LISTENER
def listener = new MockListener();
tester.setOperationContext(OperationType.LISTEN, connectionProperties, opProps, null, null);
def listenOperation = new GroovyListenOperation(tester.getOperationContext());
listenOperation.start(listener);

Thread.sleep(61000)
println "testsleep done"
listenOperation.stop()

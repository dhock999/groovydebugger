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
import com.boomi.connector.groovyconnector.GroovyOpenAPIConnector;
import com.boomi.connector.testutil.SimpleBrowseContext;

Logger logger = Logger.getLogger("GroovyOperationTest");

Map<String, Object> connProps = new HashMap<String,Object>();
connProps.put("spec", "https://raw.githubusercontent.com/OAI/OpenAPI-Specification/main/examples/v3.0/petstore.yaml");

GroovyOpenAPIConnector connector = new GroovyOpenAPIConnector();
ConnectorTester tester = new ConnectorTester(connector);

String actual="";
SimpleBrowseContext sbc = new SimpleBrowseContext(null, connector, OperationType.EXECUTE, "GET", connProps, null);
tester.setBrowseContext(sbc);

actual = tester.browseTypes();
println "types" + actual

actual = tester.browseProfiles("/pets");
println "profiles" + actual
logger.info("Test Complete")
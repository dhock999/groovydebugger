import com.boomi.connector.api.PayloadUtil;
import com.boomi.connector.api.ResponseUtil;
import com.boomi.connector.api.result.ConnectorStatus;
import com.boomi.connector.util.result.SimpleConnectorStatus;
import com.boomi.connector.api.OperationStatus;
import com.boomi.connector.api.ObjectData;
import com.boomi.connector.api.Payload;
import java.util.logging.Logger;


Logger logger = Logger.getLogger("executeUpdate")
logger.info("STDOUT LOGGER")
logger = response.getLogger();
logger.info("RESPONSE LOGGER GroovyExecuteOperation from groovy");
def url = context.getConnectionProperties().getProperty("url");
logger.info(url);
def path = context.getOperationProperties().getProperty("path");
logger.info(path);
for (ObjectData data : request) {
    // GET

    //Induced errors
// xx #$# - +
//   def xx=null
 //   xx.x

    def get = new URL(url+path).openConnection();
    def getRC = get.getResponseCode();
    logger.info(getRC+"");
    if (getRC.equals(200)) {
        ConnectorStatus status = new SimpleConnectorStatus(OperationStatus.SUCCESS, ""+get.getResponseCode(), get.getResponseMessage());
        Payload payload = PayloadUtil.toPayload(get.getInputStream().getText());

        ResponseUtil.addResult(response, data, status, payload);

    }
}

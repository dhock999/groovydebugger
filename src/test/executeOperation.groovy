import com.boomi.connector.api.PayloadUtil;
import com.boomi.connector.api.ResponseUtil;
import com.boomi.connector.api.result.ConnectorStatus;
import com.boomi.connector.util.result.SimpleConnectorStatus;
import com.boomi.connector.api.OperationStatus;
import com.boomi.connector.api.ObjectData;
import com.boomi.connector.api.Payload;
import java.util.logging.Logger;

//Log to Process Log
Logger logger = response.getLogger();
logger.info("logger executeUpdate method from groovy");
println "println executeUpdate method from groovy";


//Get url value from the Connection field in UI or Environment Extension
def url = context.getConnectionProperties().getProperty("url");
logger.info(url);

//Get the Object Type ID, selected by user from the object list when the press Import to create the operation. For OpenAPI this contains the path
def objectTypeId = context.getObjectTypeId()
def fullUrl = url+objectTypeId
logger.info(fullUrl)

//Open an http connection
def httpConnection = new URL(fullUrl).openConnection();
logger.info("logger executeOperation.groovy")

//Get the Execute Operation custom type (GET, POST etc)
def customOperationType = context.getCustomOperationType()
logger.info(customOperationType)
httpConnection.setRequestMethod(customOperationType);
if (customOperationType=="POST")
    httpConnection.doOutput(true)

//Set the response type
httpConnection.addRequestProperty("accept","application/json")

//Loop all incoming documents
for (ObjectData data : request) {
    //if POST write the each document coming into the connector
    if (customOperationType=="POST")
    {
        InputStream inputStream = data.getData();
        OutputStream outputStream = httpConnection.getOutputStream()
        byte[] buffer = new byte[8192];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }
    
    def responseCode = httpConnection.getResponseCode();
    logger.info(responseCode+"");
    def operationStatus = OperationStatus.SUCCESS
    if (responseCode != 200)
        operationStatus = OperationStatus.FAILURE
    SimpleConnectorStatus status = new SimpleConnectorStatus(operationStatus, ""+responseCode, httpConnection.getResponseMessage());
    Payload payload = PayloadUtil.toPayload(httpConnection.getInputStream().getText());

    ResponseUtil.addResult(response, data, status, payload);

}

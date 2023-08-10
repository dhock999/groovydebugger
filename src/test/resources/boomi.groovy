import java.util.Properties;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.logging.Logger;
Logger logger = Logger.getLogger("TEST");
logger.info("STDOUT LOGGER");
logger = ExecutionUtil.getBaseLogger();
logger.info("HELLO FROM request LOGGER");

println dataContext.getDataCount();
for( int i = 0; i < dataContext.getDataCount(); i++ ) {
	InputStream is = dataContext.getStream(i);
	Properties props = dataContext.getProperties(i);
	
	println ExecutionUtil.getDynamicProcessProperty("DPPName1");

	String text = is.getText();
	
	println text;
	
	text="Output "+text;
	println text;
	
	ExecutionUtil.setDynamicProcessProperty("OutputDPPName", "OutputDPPValue", false);
	is = new ByteArrayInputStream(text.getBytes());
	dataContext.storeStream(is, props);
}
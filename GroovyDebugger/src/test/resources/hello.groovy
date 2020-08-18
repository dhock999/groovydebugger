//import com.boomi.execution.ExecutionUtil; 
import java.util.logging.Logger;

def var = ExecutionUtil.getDynamicProcessProperty("propName");
println "test"; 
print var;
Logger logger = ExecutionUtil.getBaseLogger();
logger.info("LOGGER TEST");
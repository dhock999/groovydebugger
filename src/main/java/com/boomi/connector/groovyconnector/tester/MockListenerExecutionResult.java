package com.boomi.connector.groovyconnector.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import com.boomi.connector.api.ConnectorException;
import com.boomi.connector.api.Payload;
import com.boomi.connector.api.listen.ListenerExecutionResult;

public class MockListenerExecutionResult implements ListenerExecutionResult {

	String _executionId = UUID.randomUUID().toString();
	MockListenerExecutionResult(Payload payload) throws IOException
	{
		
		BufferedReader payloadIn = new BufferedReader(new InputStreamReader(payload.readFrom()));
		int c;
		StringBuffer payloadString = new StringBuffer();

		while ((c = payloadIn.read()) != -1) {

			payloadString.append((char) c);
			if (!payloadIn.ready())
				break;

		}
		File workDir = new File("work");
		if (!workDir.exists())
			workDir.mkdir();
		FileWriter myWriter;
		try {
			myWriter = new FileWriter("work/"+_executionId+".response");
			myWriter.write("Hello sync response from your listener. Here is your input echoed back: " + payloadString.toString());
			myWriter.close();
		} catch (IOException e) {
			throw new ConnectorException(e.toString());
		}
	}
	@Override
	public String getExecutionId() {
		return _executionId;
	}

	@Override
	public boolean isSuccess() {
		return true;
	}

}

package com.boomi.connector.groovyconnector.tester;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import com.boomi.connector.api.Payload;
import com.boomi.connector.api.PayloadMetadata;
import com.boomi.connector.api.listen.IndexedPayloadBatch;
import com.boomi.connector.api.listen.ListenManager;
import com.boomi.connector.api.listen.ListenOperation;
import com.boomi.connector.api.listen.Listener;
import com.boomi.connector.api.listen.ListenerExecutionResult;
import com.boomi.connector.api.listen.PayloadBatch;
import com.boomi.connector.api.listen.SubmitOptions;
import com.boomi.connector.groovyconnector.GroovyScriptHelpers;

public class MockListener implements Listener {
	Logger _logger = Logger.getLogger("MockListener");

	@Override
	public PayloadMetadata createMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void submit(Payload payload) {
		String payloadText="";
		try {
			payloadText = GroovyScriptHelpers.inputStreamToString(payload.readFrom());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_logger.info("submit Payload: " + payloadText);
	}

	@Override
	public void submit(Throwable error) {
		_logger.info("submit Throwable: " + error.getMessage());
	}

	@Override
	public Future<ListenerExecutionResult> submit(Payload payload, SubmitOptions options) {
		MockFuture future = null;
		System.out.println("submit start process");
		try {
			future = new MockFuture(payload);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Future<ListenerExecutionResult> future = new MockFuture();
		return (Future<ListenerExecutionResult>)future;
	}

	@Override
	public PayloadBatch getBatch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> IndexedPayloadBatch<T> getBatch(T index) {
		// TODO Auto-generated method stub
		return null;
	}

}

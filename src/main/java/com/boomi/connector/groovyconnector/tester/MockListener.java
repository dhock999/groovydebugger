package com.boomi.connector.groovyconnector.tester;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.boomi.connector.api.Payload;
import com.boomi.connector.api.PayloadMetadata;
import com.boomi.connector.api.listen.IndexedPayloadBatch;
import com.boomi.connector.api.listen.ListenManager;
import com.boomi.connector.api.listen.ListenOperation;
import com.boomi.connector.api.listen.Listener;
import com.boomi.connector.api.listen.ListenerExecutionResult;
import com.boomi.connector.api.listen.PayloadBatch;
import com.boomi.connector.api.listen.SubmitOptions;

public class MockListener implements Listener {

	@Override
	public PayloadMetadata createMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void submit(Payload payload) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submit(Throwable error) {
		// TODO Auto-generated method stub
		
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

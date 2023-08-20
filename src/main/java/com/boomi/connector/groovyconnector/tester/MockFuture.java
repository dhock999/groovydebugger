package com.boomi.connector.groovyconnector.tester;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.boomi.connector.api.ConnectorException;
import com.boomi.connector.api.Payload;

public class MockFuture implements Future {
	
	MockListenerExecutionResult _mockListenerExecutionResult;
	MockFuture (Payload payload) throws IOException
	{
		_mockListenerExecutionResult = new MockListenerExecutionResult(payload);
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		return _mockListenerExecutionResult;
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

}

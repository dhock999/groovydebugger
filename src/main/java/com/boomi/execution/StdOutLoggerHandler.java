package com.boomi.execution;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class StdOutLoggerHandler extends Handler {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");

	private StringWriter sw;
	private String scriptName = "Script";
	public StdOutLoggerHandler(StringWriter sw) {
		this.sw=sw;
	}

	@Override
	public void publish(LogRecord record) {
		String s = String.format("%s\t%s\t%s\t%s\n", sdf.format(new Date(record.getMillis())), record.getLevel().getName(), scriptName, record.getMessage());
		sw.append(s);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws SecurityException {
	}

	public void setScriptName(String scriptName)
	{
		this.scriptName = scriptName;
	}
}

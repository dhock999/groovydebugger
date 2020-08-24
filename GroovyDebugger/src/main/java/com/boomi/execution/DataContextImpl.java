package com.boomi.execution;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataContextImpl {

	private JSONObject _mockData;
	private JSONObject _actualData;

	public JSONObject getActualData()
	{
		return _actualData;
	}
	
	public DataContextImpl(JSONObject mock) {
		_mockData = mock;
		_actualData = new JSONObject();
		_actualData.put("documents", new JSONArray());
		JSONArray mockProcessProperties = _mockData.getJSONArray("processProperties");
		if (mockProcessProperties==null)
			mockProcessProperties = new JSONArray();
		_actualData.put("processProperties", mockProcessProperties);
		_actualData.put("visitIndex", 0);
	}
	
	private JSONArray getProcessProperties()
	{
		return _actualData.getJSONArray("processProperties");
	}
	
	private JSONObject findProcessProperty(String propName)
	{
		for (int i=0; i<getProcessProperties().length(); i++)
		{
			JSONObject processProperty = getProcessProperties().getJSONObject(i);
			if (processProperty.has("propName") && processProperty.getString("propName").contentEquals(propName))
				return processProperty;
		}
		return null;
	}
	
	public String getDynamicProcessProperty(String propName)
	{
		String propValue="";
		JSONObject processProperty = findProcessProperty(propName);
		if (processProperty!=null && processProperty.has("propValue"))
			propValue = processProperty.getString("propValue");
		
		return propValue;
	}
	
	public void setDynamicProcessProperty(String propName, String propValue)
	{
		JSONObject processProperty = findProcessProperty(propName);
		if (processProperty==null)
		{
			processProperty = new JSONObject();
			processProperty.put("propName", propName);
			getProcessProperties().put(processProperty);
		}
		processProperty.put("propValue", propValue);
	}
	
	public void setCombineAll(boolean combineAll) {
		/* void */ }

//	  public List<Properties> getMetaDataList() {
//	    return new BoomiDataContextImpl.MetaDataList();
//	  }
//
//	  public List<InputStream> getStreamList() {
//	    return new BoomiDataContextImpl.StreamList();
//	  }

	public boolean isUsed() {
		return false;
	}

	public int getDataCount() {
		return this._mockData.getJSONArray("documents").length();
	}

//	  public BaseData getData(int index) {
//	    throw new Exception('Not Implemented')
//	  }

	public InputStream getStream(int index) {
		JSONObject document = this.getMockDocument(index);
		String docContents = "";
		if (document.has("docContents"))
			docContents = document.getString("docContents");
		return new ByteArrayInputStream(docContents.getBytes());
	}

	private JSONObject getMockDocument(int index) {
		return this._mockData.getJSONArray("documents").getJSONObject(index);
	}

	public Properties getProperties(int index) {
		JSONObject document = getMockDocument(index);
		Properties properties = new Properties();
		if (document.has("documentProperties")) {
			JSONArray documentProperties = document.getJSONArray("documentProperties");
			for (int i=0; i<documentProperties.length(); i++)
			{
				JSONObject documentProperty = documentProperties.getJSONObject(i);
				String propName = "";
				if (documentProperty.has("propName"))
					propName = documentProperty.getString("propName");
				
				if (propName!=null && propName.trim().length()>0)
				{
					String propValue = "";
					if (documentProperty.has("propValue"))
						propValue = documentProperty.getString("propValue");
					properties.put(propName, propValue);
				}
			}
		}
		return properties;
	}

	public void storeStream(InputStream stream, Properties properties) throws Exception {
		JSONObject document = new JSONObject();
		JSONArray documents = _actualData.getJSONArray("documents");
		document.put("docindex", documents.length());
		documents.put(document);
		String docContents = "";
		try (Scanner scanner = new Scanner(stream, "UTF-8")) {
			docContents = scanner.useDelimiter("\\A").next();
		}
		document.put("docContents", docContents);
		JSONArray documentProperties = new JSONArray();
		document.put("documentProperties", documentProperties);
		for (Object key : properties.keySet()) {
			JSONObject documentProperty = new JSONObject();
			documentProperties.put(documentProperty);
			documentProperty.put("propName", (String) key);
			documentProperty.put("propValue", (String) properties.get(key));
		}
	}

//	  public void storePayload(Payload payload, Properties properties) throws Exception {
//	    println("Payload ${storedCount++}")
//	    properties.list(System.out)
//	    println('-- end properties --')
//
//	    def output = this.output.get(storedCount)
//	    logger.info(String.format('%s', output.toString()))
//
//	    println('-- dumping payload --')
//	    def input = payload.readFrom()
//	    int size = 0;
//	    byte[] buffer = new byte[1024];
//	    while ((size = input.read(buffer)) != -1) output.write(buffer, 0, size);
//
//	    println('\n-- end payload --')
//	  }
}

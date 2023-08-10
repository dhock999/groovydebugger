package com.boomi.execution;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.manywho.services.atomsphere.AtomsphereAPI;
import com.manywho.services.atomsphere.ServiceConfiguration;
import com.manywho.services.atomsphere.actions.groovymapscriptrunner.MapScriptIOItem;
import com.manywho.services.atomsphere.actions.groovyprocessscriptrunner.PropertyItem;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroovyDebuggerTest {
	
	ServiceConfiguration configuration;
	private void init() throws JSONException, Exception
	{
		JSONObject testCredentials=new JSONObject(TestUtil.readResource("testCredentials.json", this.getClass()));
		configuration = new ServiceConfiguration();
		configuration.setAccount(testCredentials.getString("accountId"));
		configuration.setUsername(testCredentials.getString("username"));
		configuration.setPassword(testCredentials.getString("password"));
	}
	
//	@Test
	public void testGetProcessScript() throws JSONException, Exception
	{
		String script;
		String componentId = "1153de37-d02c-4ff1-88a2-d8f4fec9671b";
		init();
		Document response = AtomsphereAPI.executeAPIXML(configuration, "Component", "GET", componentId, null, false);
		NodeList elements = response.getElementsByTagName("script");
		Node node = elements.item(0);
		script = node.getTextContent();
//		script = script.replaceAll("&#13;", "\r\n");
		System.out.println(script);
		assertTrue(script.length()>100);
	}
    
    @Test
    public void testBoomi() throws Exception
    {
		String mockDocs = TestUtil.readResource("mockdocument.json", this.getClass());
		
//		groovyCompilerConfiguration.setScriptBaseClass("Templates");
		String testScript = TestUtil.readResource("boomi.groovy", this.getClass());
		
		GroovyRunner gr = new GroovyRunner();
		gr.run(testScript, mockDocs);

		String stdout = gr.getStdout(); 
		System.out.println(stdout);
		assertTrue(stdout.length()>0);
//		System.out.println(gr.getActualDocs());
    }
    
    @Test
    public void testProcessScript() throws Exception
    {
		String testScript = TestUtil.readResource("boomi.groovy", this.getClass());
		List<PropertyItem>processProperties = new ArrayList<PropertyItem>();
		List<PropertyItem>documentProperties = new ArrayList<PropertyItem>();
		PropertyItem item = new PropertyItem();
		processProperties.add(item);
		item.setName("DPPName1");
		item.setValue("DPPValue11111");
		item = new PropertyItem();
		processProperties.add(item);
		item.setName("DPPName2");
		item.setValue("DPPValue2");

		documentProperties.add(item);
		item.setName("DDPName1");
		item.setValue("DDPValue1");
		item = new PropertyItem();
		documentProperties.add(item);
		item.setName("DDPName2");
		item.setValue("DDPValue2");

		GroovyRunner gr = new GroovyRunner();
		gr.runProcessScript(testScript, "Document 1", documentProperties, processProperties);

		String stdout = gr.getStdout(); 
		System.out.println(stdout);
		assertTrue(stdout.length()>0);
//		System.out.println(gr.getActualDocs());
    }
    
    @Test
    public void testMapScript() throws Exception
    {
//		groovyCompilerConfiguration.setScriptBaseClass("Templates");
		String testScript = TestUtil.readResource("mapscript.groovy", this.getClass());
		
		GroovyRunner gr = new GroovyRunner();
		List<MapScriptIOItem> inputFields = new ArrayList<MapScriptIOItem>();
		List<MapScriptIOItem> outputFields = new ArrayList<MapScriptIOItem>();

		MapScriptIOItem item = new MapScriptIOItem();
		item.setFieldName("input");
		item.setFieldValue("INPUTVALUE");
		item.setFieldType("character");
		inputFields.add(item);

		item = new MapScriptIOItem();
		item.setFieldName("intval");
		item.setFieldValue("0");
		item.setFieldType("integer");
		inputFields.add(item);

		item = new MapScriptIOItem();
		item.setFieldName("floatval");
		item.setFieldValue("0.1");
		item.setFieldType("float");
		inputFields.add(item);

		item = new MapScriptIOItem();
		item.setFieldName("datetimeval");
		item.setFieldValue("20201201 123344.222");
		item.setFieldType("datetime");
		inputFields.add(item);

		item = new MapScriptIOItem();
		item.setFieldName("output");
		outputFields.add(item);
		gr.runMapScript(testScript, inputFields, outputFields);

		String stdout = gr.getStdout();
		String expectedStdout = "INPUTVALUE\r\n";
		System.out.println(stdout);
		assertEquals(expectedStdout, stdout);

		String outputValue = outputFields.get(0).getFieldValue();
		System.out.println(outputValue);
		assertEquals("INPUTVALUE Output", outputValue);
    }
}
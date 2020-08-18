package com.boomi.execution;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class TestUtil
{

    static String inputStreamToString(InputStream is) throws IOException
    {
    	try (Scanner scanner = new Scanner(is, "UTF-8")) {
    		return scanner.useDelimiter("\\A").next();
    	}
    }

	static String readResource(String resourcePath, Class theClass) throws Exception
	{
		String resource = null;
		try {
			InputStream is = theClass.getClassLoader().getResourceAsStream(resourcePath);
			resource = inputStreamToString(is);
			
		} catch (Exception e)
		{
			throw new Exception("Error loading resource: "+resourcePath + " " + e.getMessage());
		}

		return resource;
	}
}
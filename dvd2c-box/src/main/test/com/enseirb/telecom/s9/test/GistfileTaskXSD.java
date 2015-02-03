package com.enseirb.telecom.s9.test;
import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.s9.Task;
import com.enseirb.telecom.s9.endpoints.ContentEndPoints;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
 
public class GistfileTaskXSD {
	private static final Logger LOGGER = LoggerFactory.getLogger(GistfileTaskXSD.class);
	@Test
	public void testAll() throws IOException, JAXBException {
 
		Task task = new Task();
		task.setTask("t");
		task.setRetries("retries");
		task.setKwargs("kwargs...");
		task.setId(UUID.randomUUID().toString());
		task.getArgs().add("arg1");
		task.getArgs().add("arg2");
 
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
			public HierarchicalStreamWriter createWriter(Writer writer) {
				return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
			}
		});
 
		//System.out.println(xstream.toXML(task));
		LOGGER.info(xstream.toXML(task));
				
//		{
//			  "id": "0af8243a-6503-464a-9ac0-2676a05371a0",
//			  "task": "t",
//			  "args":  [
//			      "arg1",
//			      "arg2"
//			    ],
//			  "kwargs": "kwargs...",
//			  "retries": "retries"
//			}
 
 
	}
 
}
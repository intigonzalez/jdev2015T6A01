package com.enseirb.telecom.s9.test;
import java.io.IOException;
import java.io.Writer;
import java.util.UUID;
 
import javax.xml.bind.JAXBException;
 
import org.junit.Test;
 
import com.enseirb.telecom.s9.Task;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
 
public class GistfileTaskXSD {
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
 
		System.out.println(xstream.toXML(task));
		
//		{
//			  "id": "0af8243a-6503-464a-9ac0-2676a05371a0",
//			  "task": "t",
//			  "args": {
//			    "arg": [
//			      "arg1",
//			      "arg2"
//			    ]
//			  },
//			  "kwargs": "kwargs...",
//			  "retries": "retries"
//			}
 
 
	}
 
}
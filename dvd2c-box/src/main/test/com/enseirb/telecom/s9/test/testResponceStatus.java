package com.enseirb.telecom.s9.test;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;

public class testResponceStatus {

	@Test
	public void testAll() {

		Status st = Status.fromStatusCode(200);

		System.out.println(st);
	}

}

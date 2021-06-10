package com.naturalmotion.servlet;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.naturalmotion.webservice.configuration.Configuration;

import csr.Checksum;

public class UserResolverTest {

	private Configuration configuration = new Configuration();

	@Test
	public void testFindUserFromId() throws Exception {
		Assertions
				.assertThat(new UserResolver(configuration).findUserFromId("e62e32fa4c0292375d2107e5256e51bc051416ca"))
				.isEqualTo("buzz");
	}

	@Test
	public void listAll() {
		String list = configuration.getString("users.list");
		String[] split = list.split(",");
		Checksum checksum = new Checksum();
		for (String user : split) {
			String hmac = checksum.computeHmac(user);
			System.out.println(user + ": " + hmac);
		}
	}

}

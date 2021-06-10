package com.naturalmotion.servlet;

import com.naturalmotion.webservice.configuration.Configuration;

import csr.Checksum;

public class UserResolver {

	private Configuration configuration;

	public UserResolver(Configuration configuration) {
		this.configuration = configuration;
	}

	public String findUserFromId(String userId) {
		String resultUser = null;
		String list = configuration.getString("users.list");
		String[] split = list.split(",");
		Checksum checksum = new Checksum();
		for (String user : split) {
			String computeHmac = checksum.computeHmac(user);
			if (computeHmac.equals(userId)) {
				resultUser = user;
			}
		}
		return resultUser;
	}
}

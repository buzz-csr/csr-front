package com.naturalmotion.token;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TokenManager {

	private static final String KEY = "2HI";

	public String create(String user) throws NoSuchAlgorithmException, InvalidKeyException {
		String dir = String.valueOf(System.currentTimeMillis());
		TokenUser tokenUser = new TokenUser(user, dir);
		return encrypt(tokenUser.toString());
	}

	public TokenUser from(String id) {
		String decrypt = decrypt(id);
		return new TokenUser(decrypt);
	}

	private String encrypt(String strToEncrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, getKey());
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes()));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	private String decrypt(String strToDecrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, getKey());
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	private SecretKeySpec getKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		byte[] key = Arrays.copyOf(sha.digest(KEY.getBytes()), 16);
		return new SecretKeySpec(key, "AES");
	}
}

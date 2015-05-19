package com.server;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class CryptoGenerator {
	private static CryptoGenerator instance = new CryptoGenerator();
	
	private SecureRandom random = new SecureRandom();

	private CryptoGenerator() {}
	
	public static CryptoGenerator getInstance() {
		return instance;
	}
	
	public String nextPhotoName() {
		return new BigInteger(130, random).toString(32);
	}
}
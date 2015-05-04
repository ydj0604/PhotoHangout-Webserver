package com.server;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class CryptoGenerator {
  private SecureRandom random = new SecureRandom();

  public String nextPhotoName() {
    return new BigInteger(130, random).toString(32);
  }
}
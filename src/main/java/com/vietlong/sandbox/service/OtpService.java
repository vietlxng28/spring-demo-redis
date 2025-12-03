package com.vietlong.sandbox.service;

import java.time.Duration;
import java.util.Random;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class OtpService {

  private static final String OTP_KEY_PREFIX = "otp:";
  private static final int OTP_MIN_VALUE = 100000;
  private static final int OTP_MAX_VALUE = 999999;
  private static final int OTP_EXPIRATION_SECONDS = 30;
  private static final String OTP_VALID_MESSAGE = "OTP is valid";
  private static final String OTP_INVALID_MESSAGE = "OTP is invalid";

  private final RedisTemplate<String, String> redisTemplate;

  public String generateOtp(String username) {
    String otp = generateRandomOtp();
    String key = buildOtpKey(username);

    redisTemplate.opsForValue().set(key, otp, Duration.ofSeconds(OTP_EXPIRATION_SECONDS));
    log.info("Generated OTP for username: {}", username);

    return otp;
  }

  public String validateOtp(String username, String inputOtp) {
    String key = buildOtpKey(username);
    String storedOtp = redisTemplate.opsForValue().get(key);

    if (storedOtp != null && storedOtp.equals(inputOtp.trim())) {
      Boolean deleted = redisTemplate.delete(key);
      if (Boolean.TRUE.equals(deleted)) {
        log.info("OTP validated successfully for username: {}", username);
      }
      return OTP_VALID_MESSAGE;
    }

    log.warn("Invalid OTP attempt for username: {}", username);
    return OTP_INVALID_MESSAGE;
  }

  private String generateRandomOtp() {
    Random random = new Random();
    int otp = random.nextInt(OTP_MAX_VALUE - OTP_MIN_VALUE + 1) + OTP_MIN_VALUE;
    return String.valueOf(otp);
  }

  private String buildOtpKey(String username) {
    return OTP_KEY_PREFIX + username;
  }
}

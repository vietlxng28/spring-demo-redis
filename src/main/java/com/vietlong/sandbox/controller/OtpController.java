package com.vietlong.sandbox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import com.vietlong.sandbox.service.OtpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/otp")
@AllArgsConstructor
@Tag(name = "OTP Management", description = "APIs for generating and validating OTP (One-Time Password)")
public class OtpController {

    private final OtpService otpService;

    @GetMapping("/{username}")
    @Operation(summary = "Generate OTP for username", description = "Generates a 6-digit OTP for the specified username. The OTP is stored in Redis with a 30-second expiration time.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTP successfully generated")
    })
    public ResponseEntity<String> generateOtp(
            @PathVariable String username) {
        String otp = otpService.generateOtp(username);
        return ResponseEntity.ok(otp);
    }

    @PostMapping("/{username}")
    @Operation(summary = "Validate OTP for username", description = "Validates the provided OTP for the specified username. The OTP is deleted from Redis upon successful validation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTP validation result")
    })
    public ResponseEntity<String> validateOtp(
            @PathVariable String username,
            @RequestBody String otp) {
        String result = otpService.validateOtp(username, otp);
        return ResponseEntity.ok(result);
    }
}

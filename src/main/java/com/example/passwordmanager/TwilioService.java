package com.example.passwordmanager;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.type.PhoneNumber;

public class TwilioService {
    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public static final String VERIFY_SERVICE_ID = System.getenv("VERIFY_SERVICE_ID");

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendVerificationCode(String phoneNumber) {
        Verification verification = Verification.creator(
                VERIFY_SERVICE_ID,
                phoneNumber,
                "sms"
        ).create();

        System.out.println("Verification code sent to " + phoneNumber);
    }
    public static boolean verifyCode(String phoneNumber, String code) {
        VerificationCheck verificationCheck = VerificationCheck.creator(
                        VERIFY_SERVICE_ID
                )
                .setTo(phoneNumber)
                .setCode(code)
                .create();

        return "approved".equals(verificationCheck.getStatus());
    }

}
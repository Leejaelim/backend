package matchuri.backend.identity.auth.service;

import matchuri.backend.identity.auth.api.CaptchaPurpose;

import matchuri.backend.identity.auth.api.CaptchaVerifier;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!prod")
public class LocalCaptchaVerifier implements CaptchaVerifier {

    @Override
    public boolean verify(String token, CaptchaPurpose purpose, String clientIp) {
        return true;
    }

}

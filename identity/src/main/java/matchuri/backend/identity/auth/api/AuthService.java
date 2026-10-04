package matchuri.backend.identity.auth.api;

import matchuri.backend.identity.auth.command.LoginCommand;
import matchuri.backend.identity.auth.command.OAuth2ExchangeCommand;
import matchuri.backend.identity.auth.result.LoginPayload;
import matchuri.backend.identity.auth.result.LoginResult;
import matchuri.backend.identity.auth.result.LogoutResult;
import matchuri.backend.identity.member.entity.SocialProviderType;

public interface AuthService {

    LoginResult login(LoginCommand command, String clientIp);

    LoginResult refresh(String refreshToken, String clientIp);

    LogoutResult logout(Long memberId, String refreshToken, String clientIp);

    SocialProviderType resolveOAuth2LoginProvider(String provider);

    LoginPayload exchangeOAuth2Code(OAuth2ExchangeCommand command, String clientIp);
}

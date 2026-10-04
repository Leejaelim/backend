package matchuri.backend.identity.auth.command;

import matchuri.backend.identity.member.entity.SocialProviderType;

public record OAuth2ExchangeCommand(
        SocialProviderType provider,
        String code
) {
}

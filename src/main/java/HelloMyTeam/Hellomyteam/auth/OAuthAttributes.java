package HelloMyTeam.Hellomyteam.auth;

import HelloMyTeam.Hellomyteam.dto.MemberDTO;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {
    GOOGLE("google", (attributes) -> {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName((String) attributes.get("name"));
        memberDTO.setEmail((String) attributes.get("email"));
        return memberDTO;
    });

    private final String registrationId;
    private final Function<Map<String, Object>, MemberDTO> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, MemberDTO> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static MemberDTO extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.auth.OAuthAttributes;
import HelloMyTeam.Hellomyteam.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                                    .getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        MemberDTO memberProfile = OAuthAttributes.extract(registrationId, attributes);

        httpSession.setAttribute("user", memberProfile);

        Map<String, Object> customAttribute = customAttribute(attributes, userNameAttributeName, memberProfile, registrationId);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttribute,
                userNameAttributeName);
    }

    private Map customAttribute(Map attributes, String userNameAttributeName, MemberDTO memberDTO, String registrationId) {
        Map<String, Object> customAttribute = new LinkedHashMap<>();
        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", memberDTO.getName());
        customAttribute.put("email", memberDTO.getEmail());
        return customAttribute;
    }
}

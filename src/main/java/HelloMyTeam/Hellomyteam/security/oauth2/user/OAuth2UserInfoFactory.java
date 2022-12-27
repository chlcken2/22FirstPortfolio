package HelloMyTeam.Hellomyteam.security.oauth2.user;


import HelloMyTeam.Hellomyteam.exception.OAuth2AuthenticationProcessingException;
import HelloMyTeam.Hellomyteam.security.oauth2.model.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}

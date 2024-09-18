package se.sowl.yesterdayauth.oauth.factory;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import se.sowl.yesterdaydomain.oauth.domain.OAuth2Profile;
import se.sowl.yesterdaydomain.oauth.domain.OAuth2UserAttribute;
import se.sowl.yesterdaydomain.oauth.domain.Role;
import se.sowl.yesterdaydomain.user.domain.CustomOAuth2User;
import se.sowl.yesterdaydomain.user.domain.User;

import java.util.Collections;

@Component
public class OAuth2UserFactory {

    public OAuth2User createOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User, OAuth2Profile userProfile, User user) {
        String userNameAttributeName = getUserNameAttributeName(userRequest);
        OAuth2UserAttribute oAuth2UserAttribute = new OAuth2UserAttribute(oAuth2User.getAttributes(), userNameAttributeName, userProfile);
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(Role.USER.getValue())),
            oAuth2UserAttribute.getAttributes(),
            userNameAttributeName
        );
    }

    public CustomOAuth2User createCustomOAuth2User(User user, OAuth2User oAuth2User) {
        return new CustomOAuth2User(oAuth2User, user);
    }

    private String getUserNameAttributeName(OAuth2UserRequest userRequest) {
        return userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
    }
}

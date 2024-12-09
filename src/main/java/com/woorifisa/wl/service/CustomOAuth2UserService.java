package com.woorifisa.wl.service;

import com.woorifisa.wl.model.entity.Role;
import com.woorifisa.wl.model.entity.User;
import com.woorifisa.wl.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth 제공자 (google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 사용자 정보
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = getEmail(attributes, registrationId);
        String name = getName(attributes, registrationId);

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && !user.getOauthType().equalsIgnoreCase(registrationId)) {
            String errorMessage = "이미 " + user.getOauthType() + "로 로그인한 계정입니다.<br>다른 로그인 방식을 사용해주세요.";
            saveErrorMessageToSession(errorMessage);
            throw new OAuth2AuthenticationException(errorMessage);
        }

        if (user == null) {
            user = createUser(email, name, registrationId);
        }
        user.setOauthType(registrationId.toUpperCase());
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        saveUserToSession(user);

        return oAuth2User;
    }

    private String getEmail(Map<String, Object> attributes, String registrationId) {
        if (registrationId.equals("google")) {
            return (String) attributes.get("email");
        } else if (registrationId.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            return (String) kakaoAccount.get("email");
        } else if (registrationId.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return (String) response.get("email");
        }
        throw new IllegalArgumentException("지원하지 않는 OAuth 제공자입니다.");
    }

    private String getName(Map<String, Object> attributes, String registrationId) {
        if (registrationId.equals("google")) {
            return (String) attributes.get("name");
        } else if (registrationId.equals("kakao")) {
            Map<String, Object> kakaoProfile = (Map<String, Object>) attributes.get("properties");
            return (String) kakaoProfile.get("nickname");
        } else if (registrationId.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return (String) response.get("name");
        }
        throw new IllegalArgumentException("지원하지 않는 OAuth 제공자입니다.");
    }

    private User createUser(String email, String name, String registrationId) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setOauthType(registrationId.toUpperCase());
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.GUEST); // 기본 역할 GUEST
        return user;
    }

    // 로그인한 유저의 세션 정보
    private void saveUserToSession(User user) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attributes.getRequest().getSession();
        session.setAttribute("user_id", user.getUserId());
        session.setAttribute("name", user.getName());
        session.setAttribute("user_role", user.getRole());
    }

    // 세션에 오류 메시지를 저장
    private void saveErrorMessageToSession(String errorMessage) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attributes.getRequest().getSession();
        session.setAttribute("error_message", errorMessage);
    }
}

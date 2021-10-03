package kakaologin.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/kakao/callback")
    public @ResponseBody String kakaoCallBack(String code) {
        String clientId = "e28f6da4a2c88321914b28137fe7d64e";
        String redirectUri = "http://localhost:8080/auth/kakao/callback";
        String reqeustUri = "https://kauth.kakao.com/oauth/token";
        String requestProfileUri = "https://kapi.kakao.com/v2/user/me";

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =authService.createKakaoTokenRequest(clientId, redirectUri, code);
        ResponseEntity<String> response = authService.postKakaoTokenRequest(reqeustUri, kakaoTokenRequest);
        OAuthToken oAuthToken = authService.mapResponseToOAuthToken(response);

        System.out.println("access token: " + oAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = authService.createKakaoTokenRequest(oAuthToken.getAccess_token());
        ResponseEntity<String> kakaoProfileResponse = authService.postKakaoTokenRequest(requestProfileUri, kakaoProfileRequest);

        return kakaoProfileResponse.getBody();
    }
}

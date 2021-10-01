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

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =authService.createKakaoTokenRequest(clientId, redirectUri, code);
        OAuthToken oAuthToken = authService.postKakaoTokenRequest(reqeustUri, kakaoTokenRequest);

        System.out.println("access token: " + oAuthToken.getAccess_token());

        return "카카오 토큰 요청 완료, 토큰 요청에 대한 응답: " + oAuthToken;
    }
}

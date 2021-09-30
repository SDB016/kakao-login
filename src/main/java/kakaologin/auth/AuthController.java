package kakaologin.auth;

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
public class AuthController {

    @GetMapping("/auth/kakao/callback")
    public @ResponseBody String kakaoCallBack(String code) {

        RestTemplate rt = new RestTemplate(); // http 요청을 쉽게 해주는 라이브러리

        //HTTPHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); // body 데이터를 담을 value
        params.add("grant_type","authorization_code");
        params.add("client_id","e28f6da4a2c88321914b28137fe7d64e");
        params.add("redirect_uri","http://localhost:8080/auth/kakao/callback");
        params.add("code", code);

        //HTTPHeader 와 HTTPBody 를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token", //response 할 주소
                HttpMethod.POST, // 요청 방식
                kakaoTokenRequest, //헤더 값과 바디 값
                String.class //반환받을 타입
        );

        return "카카오 토큰 요청 완료, 토큰 요청에 대한 응답: " + response;
    }
}

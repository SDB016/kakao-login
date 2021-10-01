package kakaologin.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    public HttpEntity<MultiValueMap<String, String>> createKakaoTokenRequest(String clientId, String redirectUri, String code) {
        //HTTPHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); // body 데이터를 담을 value
        params.add("grant_type","authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        //HTTPHeader 와 HTTPBody 를 하나의 오브젝트에 담기
        return new HttpEntity<>(params, headers);
    }

    public OAuthToken postKakaoTokenRequest(String reqeustUri, HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest) {
        RestTemplate rt = new RestTemplate(); // http 요청을 쉽게 해주는 라이브러리
        ResponseEntity<String> response = rt.exchange(
                reqeustUri, //request 할 주소
                HttpMethod.POST, // 요청 방식
                kakaoTokenRequest, //헤더 값과 바디 값
                String.class //반환받을 타입
        );
        OAuthToken oAuthToken = mapResponseToOAuthToken(response);
        return oAuthToken;
    }

    private OAuthToken mapResponseToOAuthToken(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return oAuthToken;
    }
}

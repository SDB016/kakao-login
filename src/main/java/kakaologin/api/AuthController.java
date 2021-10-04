package kakaologin.api;

import kakaologin.domain.Member;
import kakaologin.repository.MemberRepository;
import kakaologin.service.AuthService;
import kakaologin.model.OAuthToken;
import kakaologin.model.KakaoProfile;
import kakaologin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;

    @Value("${sdb.key}")
    private String sdbkey;

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallBack(String code) {
        String clientId = "e28f6da4a2c88321914b28137fe7d64e";
        String redirectUri = "http://localhost:8080/auth/kakao/callback";
        String reqeustUri = "https://kauth.kakao.com/oauth/token";
        String requestProfileUri = "https://kapi.kakao.com/v2/user/me";

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =authService.createKakaoTokenRequest(clientId, redirectUri, code);
        ResponseEntity<String> response = authService.postKakaoTokenRequest(reqeustUri, kakaoTokenRequest);
        OAuthToken oAuthToken = authService.mapResponseToOAuthToken(response, OAuthToken.class);

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = authService.createKakaoTokenRequest(oAuthToken.getAccess_token());
        ResponseEntity<String> kakaoProfileResponse = authService.postKakaoTokenRequest(requestProfileUri, kakaoProfileRequest);
        KakaoProfile kakaoProfile = authService.mapResponseToOAuthToken(kakaoProfileResponse, KakaoProfile.class);

        String userId = kakaoProfile.getKakaoAccount().getEmail() + "_" + kakaoProfile.getId();
        String username = kakaoProfile.getKakaoAccount().getProfile().getNickname();
        String email = kakaoProfile.getKakaoAccount().getEmail();
        String password = sdbkey;

        //가입자 혹은 비가입자 체크
        Member originMember = memberService.findByUserId(userId);

        if (originMember.getUserId() == null) {
            System.out.println("기존 회원이 아니기에 자동 회원가입을 진행합니다.");
            memberService.joinMember(userId, username, email, password);
        }

        System.out.println("자동 로그인을 진행합니다.");
        // 로그인 처리
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userId, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }
}

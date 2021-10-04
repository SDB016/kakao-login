package kakaologin.auth;

import kakaologin.domain.Member;
import kakaologin.repository.MemberRepository;
import kakaologin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member principal = memberRepository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.: " + username));
        return new PrincipalDetail(principal);
    }
}

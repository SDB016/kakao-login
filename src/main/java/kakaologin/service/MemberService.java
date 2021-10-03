package kakaologin.service;

import kakaologin.domain.Member;
import kakaologin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member joinMember(String userId, String username, String email, String password) {
        Member member = memberRepository.findByUserId(userId);
        if (member != null) {
            return member;
        }

        Member newMember = Member.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        return memberRepository.save(newMember);
    }
}

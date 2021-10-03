package kakaologin.service;

import kakaologin.domain.Member;
import kakaologin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member joinMember(String userId, String username, String email, String password) {
        Member member = memberRepository.findByUserId(userId);
        if (member != null) {
            return member;
        }

        Member newMember = Member.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .password(password) //TODO password 인코딩
                .build();
        return memberRepository.save(newMember);
    }
}

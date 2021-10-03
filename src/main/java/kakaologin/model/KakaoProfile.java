package kakaologin.model;

import lombok.Data;
@Data
public class KakaoProfile {
    private Integer id;
    private String connectedAt;
    private Properties properties;
    private KakaoAccount kakaoAccount;

    @Data
    public class KakaoAccount {
        private Boolean profileNicknameNeedsAgreement;
        private Profile profile;
        private Boolean hasEmail;
        private Boolean emailNeedsAgreement;
        private Boolean isEmailValid;
        private Boolean isEmailVerified;
        private String email;
        private Boolean hasGender;
        private Boolean genderNeedsAgreement;

        public String gender;

        @Data
        public class Profile {
            private String nickname;
        }
    }

    @Data
    public class Properties {
        private String nickname;
    }
}

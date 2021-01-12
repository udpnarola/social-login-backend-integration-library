package social.login.backend.integration.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialUser {

    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
}

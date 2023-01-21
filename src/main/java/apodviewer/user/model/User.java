package apodviewer.user.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {
    String userSub;
    String userName;
    String email;
    boolean emailVerified;
    String profilePictureUrl;
}

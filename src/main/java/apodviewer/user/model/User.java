package apodviewer.user.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {
    String userId;
    String userName;
    String email;
}

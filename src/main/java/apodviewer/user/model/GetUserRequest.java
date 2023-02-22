package apodviewer.user.model;

import lombok.Data;

@Data
public class GetUserRequest {
    String userSub;

    public GetUserRequest() {}
}

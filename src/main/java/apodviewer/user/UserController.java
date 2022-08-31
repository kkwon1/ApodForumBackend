package apodviewer.user;

import apodviewer.user.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping(value = "/user", params = {"user_id"})
    public User getUserInformation(@RequestParam String user_id) {
        // TODO: implement
        // return User likes
        // return User saves
        // return User setting/configuration (if any)
        return User.builder().build();
    }
}

package apodviewer.apod.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UserController {

    @GetMapping("/user")
    public String getApod(@RequestParam("userId") String userId) throws IOException {
        return "";
    }


}

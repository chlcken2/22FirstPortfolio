package HelloMyTeam.Hellomyteam.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReactTestController {
    @ApiOperation(value = "test", notes = "리액트 연결 테스트")
    @GetMapping("/api/hello")
    public String test() {
        return "Hello, 2world!";
    }
}

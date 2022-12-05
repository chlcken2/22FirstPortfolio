package HelloMyTeam.Hellomyteam.controller;

public class ReactiTestController {
    @GetMapping("/api/hello")
    public String test() {
        return "Hello, world!";
    }
}

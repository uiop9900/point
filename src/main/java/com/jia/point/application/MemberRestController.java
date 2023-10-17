package com.jia.point.application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberRestController {

    @RequestMapping("test")
    @ResponseBody
    public String test() {
        return "test";
    }


}

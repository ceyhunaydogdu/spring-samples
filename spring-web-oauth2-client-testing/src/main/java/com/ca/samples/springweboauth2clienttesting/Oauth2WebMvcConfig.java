package com.ca.samples.springweboauth2clienttesting;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/

@Controller
public class Oauth2WebMvcConfig {
    @GetMapping(value = "/")
    public String home(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client,
            @AuthenticationPrincipal OAuth2User user) {
        model.addAttribute("userName", user.getAttributes().get("name"));
        model.addAttribute("clientName", client.getClientRegistration().getClientName());
        model.addAttribute("userAttributes", user.getAttributes());
        return "home";
    }
}

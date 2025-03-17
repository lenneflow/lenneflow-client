package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.dto.UserDto;
import de.lenneflow.lenneflowclient.dto.UserDto2;
import de.lenneflow.lenneflowclient.dto.UserToken;
import de.lenneflow.lenneflowclient.endpointprovider.AccountEndpointProvider;
import de.lenneflow.lenneflowclient.enums.Role;
import de.lenneflow.lenneflowclient.model.User;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AccountController {

    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;
    private final AccountEndpointProvider accountEndpointProvider;

    public AccountController(RestUtil restUtil, ControllerUtil controllerUtil, AccountEndpointProvider accountEndpointProvider) {
        this.restUtil = restUtil;
        this.controllerUtil = controllerUtil;
        this.accountEndpointProvider = accountEndpointProvider;
    }

    @GetMapping("/user/new")
    public String newUserGet(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        UserDto2 user = new UserDto2();
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("title", "New User");
        return "/user/new-user";
    }

    @PostMapping("/user/new")
    public String newUserPost(ModelMap model, UserDto2 user) {
        Set<Role> authorities = new HashSet<>();
        authorities.add(user.getRole());
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setAuthorities(authorities);
        restUtil.postForObject(accountEndpointProvider.getRootUrl() + accountEndpointProvider.getCreateAccountPath(), user, UserDto.class);
        return "redirect:/user/list";
    }

    @GetMapping("/user/list")
    public String newUserListGet(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        List<User> users = restUtil.getForObject(accountEndpointProvider.getRootUrl() + accountEndpointProvider.getFindAllAccountsPath(), List.class);
        model.addAttribute("userList", users);
        model.addAttribute("title", "User List");
        return "/user/user-list";
    }

    @GetMapping("/user/new-token")
    public String userToken(ModelMap model, UserDto user) {
        model = controllerUtil.createModelMap(model);
        UserToken token =new UserToken();
        model.addAttribute("token", token);
        model.addAttribute("title", "New Token");
        return "/user/new-token";
    }

}

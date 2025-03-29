package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.dto.LoginDTO;
import de.lenneflow.lenneflowclient.dto.UserDto;
import de.lenneflow.lenneflowclient.dto.UserDto2;
import de.lenneflow.lenneflowclient.dto.UserToken;
import de.lenneflow.lenneflowclient.endpointprovider.AccountEndpointProvider;
import de.lenneflow.lenneflowclient.enums.Role;
import de.lenneflow.lenneflowclient.model.AccountUser;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/user/edit/{uid}")
    public String editUserGet(ModelMap model, @PathVariable String uid) {
        model = controllerUtil.createModelMap(model);
        AccountUser foundUser = restUtil.getForObject(accountEndpointProvider.getAccountRootUrl() + accountEndpointProvider.getFindAccountPath().replace("{uid}", uid), AccountUser.class);
        UserDto2 user = new UserDto2();
        user.setUsername(foundUser.getUsername());
        user.setEmail(foundUser.getEmail());
        user.setRole(foundUser.getAuthorities().iterator().next());
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("title", "Edit User " + user.getUsername());
        return "/user/new-user";
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

    @PostMapping({"/user/new", "/user/edit/{uid}"})
    public String newUserPost(ModelMap model, UserDto2 user, @PathVariable String uid) {
        Set<Role> authorities = new HashSet<>();
        authorities.add(user.getRole());
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setAuthorities(authorities);
        restUtil.postForObject(accountEndpointProvider.getAccountRootUrl() + accountEndpointProvider.getCreateAccountPath(), user, UserDto.class);
        return "redirect:/user/list";
    }

    @GetMapping("/user/list")
    public String newUserListGet(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        List<AccountUser> users = restUtil.getForObjectList(accountEndpointProvider.getAccountRootUrl() + accountEndpointProvider.getFindAllAccountsPath(), new ParameterizedTypeReference<List<AccountUser>>() {});
        model.addAttribute("userList", users);
        model.addAttribute("title", "User List");
        return "/user/user-list";
    }

    @GetMapping("/user/delete/{uid}")
    public String deleteUser(ModelMap model, @PathVariable String uid) {
        restUtil.deleteObject(accountEndpointProvider.getAccountRootUrl() + accountEndpointProvider.getDeleteAccountPath().replace("{uid}", uid));
        return "redirect:/user/list";
    }

    @GetMapping("/user/new-token")
    public String userToken(ModelMap model) {
        LoginDTO loginDTO = new LoginDTO();
        LoginDTO principal = (LoginDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loginDTO.setPassword(principal.getUsername());
        loginDTO.setUsername(principal.getPassword());
        model = controllerUtil.createModelMap(model);
        UserToken token = restUtil.postForObject(accountEndpointProvider.getAccountRootUrl() + accountEndpointProvider.getUserTokenPath(), loginDTO,  UserToken.class);
        model.addAttribute("token", token);
        model.addAttribute("title", "Generated Token");
        return "/user/token-details";
    }

}

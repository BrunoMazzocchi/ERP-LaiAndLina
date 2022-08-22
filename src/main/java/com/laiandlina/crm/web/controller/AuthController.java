package com.laiandlina.crm.web.controller;

import com.laiandlina.crm.domain.event.*;
import com.laiandlina.crm.domain.service.*;
import com.laiandlina.crm.persistance.data.*;
import com.laiandlina.crm.persistance.entity.*;
import com.laiandlina.crm.persistance.repository.*;
import com.laiandlina.crm.web.config.exception.*;
import com.laiandlina.crm.web.response.*;
import com.laiandlina.crm.web.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.annotation.*;
import org.springframework.security.core.context.*;
import org.springframework.security.crypto.password.*;
import org.springframework.ui.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;
import java.net.*;
import java.sql.Date;
import java.util.*;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDeviceService userDeviceService;


    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private  DepartmentRepository departmentRepository;

    @Autowired
    private EmailService javaMailSender;

    @PostMapping("/signin")
    //cambiar a atribute o body
    public ModelAndView authenticateUser(@ModelAttribute("login") LoginForm loginRequest, HttpServletResponse response,
                                         ModelMap model, HttpServletRequest request) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User not found."));

        if (user.getActive()) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtProvider.generateJwtToken(authentication);
            userDeviceService.findByUserId(user.getId())
                    .map(UserDevice::getRefreshToken)
                    .map(RefreshToken::getId)
                    .ifPresent(refreshTokenService::deleteById);


            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDeviceId(getRemoteAddr(request).toString());
            String browserType = request.getHeader("User-Agent");
            deviceInfo.setDeviceType(browserType);


            //Dev test, still waiting to implement user device creation
            UserDevice userDevice = userDeviceService.createUserDevice(deviceInfo);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken();
            userDevice.setUser(user);
            userDevice.setRefreshToken(refreshToken);
            refreshToken.setUserDevice(userDevice);
            refreshToken = refreshTokenService.save(refreshToken);

            Cookie jwtTokenCookie = new Cookie("Authorization", jwtToken);
            Cookie jwtRefreshToken = new Cookie("RefreshToken", refreshToken.getToken());

            jwtTokenCookie.setMaxAge(36000);
            jwtTokenCookie.setPath("/");

            jwtRefreshToken.setMaxAge(36000);
            jwtRefreshToken.setPath("/");


            response.addCookie(jwtTokenCookie);
            response.addCookie(jwtRefreshToken);


            ResponseEntity.ok(new JwtResponse(jwtToken, refreshToken.getToken(), jwtProvider.getExpiryDuration()));
            return new ModelAndView("redirect:/index", model);
        }
        return new ModelAndView("redirect:/login", model);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/signup")
    public ModelAndView registerUser(@ModelAttribute("signUpRequest") SignUpUserRequest signUpRequest, ModelMap model,
                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {

            return new ModelAndView("redirect:newUser", model);
        }

        // Creating user's account
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());

        String password = randomString();
        user.setPassword(encoder.encode(password));
        Set<String> strRoles = Collections.singleton(signUpRequest.getRole());
        Set<Role> roles = new HashSet<>();


        Set<String> strDepartments = Collections.singleton(signUpRequest.getDepartment());
        Set<Department> departments = new HashSet<>();

        user.setState(1);
        user.setUserCreator(userPrincipal.getId());
        user.setPhoneNumber(String.valueOf(signUpRequest.getPhoneNumber()));
        java.sql.Date timestamp = new Date(System.currentTimeMillis());
        user.setCreationDate(timestamp);

        user.setUrlPhoto("stock-user.jpg");

        strRoles.forEach(role -> {
            switch (role) {
                case "Admin":
                    Role adminRole = roleRepository.findByRoleName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    roles.add(adminRole);
                    break;
                case "Dev":
                    Role devRole = roleRepository.findByRoleName(RoleName.ROLE_DEV)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    roles.add(devRole);

                    break;
                case "Almacen":
                    Role grocerRole = roleRepository.findByRoleName(RoleName.ROLE_GROCER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    roles.add(grocerRole);

                    break;
                case "Usuario":
                default:
                    Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    roles.add(userRole);
            }
        });

        strDepartments.forEach(department -> {
            switch (department) {
                case "Administracion":
                    Department departmentAdmin = departmentRepository.findByDepartmentName(DepartmentName.DEPARTMENT_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User department not found."));
                    departments.add(departmentAdmin);
                    break;
                case "IT":
                    Department departmentIT = departmentRepository.findByDepartmentName(DepartmentName.DEPARTMENT_IT)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User department not found."));
                    departments.add(departmentIT);
                    break;
            }
        });


        user.setRoles(roles);
        user.setDepartments(departments);
        user.activate();
        userRepository.save(user);

        javaMailSender.sendEmail(user.getEmail(), "Usuario nuevo", "Se te ha invitado: '" + user.getEmail()
                + ", Password: " + password + "' . Puedes continuar aqui http://localhost:8080/login");
        return new ModelAndView("redirect:/index", model);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwtToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {

        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        Optional<String> token = Optional.of(refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshToken -> {
                    refreshTokenService.verifyExpiration(refreshToken);
                    userDeviceService.verifyRefreshAvailability(refreshToken);
                    refreshTokenService.increaseCount(refreshToken);
                    return refreshToken;
                })
                .map(RefreshToken::getUserDevice)
                .map(UserDevice::getUser)
                .map(u -> jwtProvider.generateTokenFromUser(u))
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Missing refresh token in database.Please login again")));
        return ResponseEntity.ok().body(new JwtResponse(token.get(), tokenRefreshRequest.getRefreshToken(), jwtProvider.getExpiryDuration()));
    }

    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @PutMapping("/logout")
    public ResponseEntity<ApiResponse> logoutUser(@CurrentUser UserPrincipal currentUser,
                                                  @RequestBody LogOutRequest logOutRequest,
                                                  HttpServletRequest req, HttpServletResponse resp) {

        String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
        UserDevice userDevice = userDeviceService.findByUserId(currentUser.getId())
                .filter(device -> device.getDeviceId().equals(deviceId))
                .orElseThrow(() -> new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "Invalid device Id supplied. No matching device found for the given user "));
        refreshTokenService.deleteById(userDevice.getRefreshToken().getId());

        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(currentUser.getUsername(), logOutRequest.getToken(), logOutRequest);
        applicationEventPublisher.publishEvent(logoutSuccessEvent);


        eraseCookie(req, resp);
        return ResponseEntity.ok(new ApiResponse(true, "User has successfully logged out from the system!"));
    }

    private void eraseCookie(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
    }

    private String randomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    private String getRemoteAddr(HttpServletRequest req) {
        if (!ObjectUtils.isEmpty(req.getHeader("X-Real-IP"))) {
            return req.getHeader("X-Real-IP");
        }
        return req.getRemoteAddr();
    }
}

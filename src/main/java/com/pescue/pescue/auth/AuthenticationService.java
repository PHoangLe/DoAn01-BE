package com.pescue.pescue.auth;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.UserRepository;
import com.pescue.pescue.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationResponse userRegister(UserRegisterRequest request){
        User user = new User(
                request.getUserEmail(),
                passwordEncoder.encode(request.getUserPassword()),
                request.getUserFirstName(),
                request.getUserLastName(),
                request.getUserAvatar(),
                true,
                List.of(Role.ROLE_USER)
        );
        try{
            userRepository.insert(user);
        }
        catch (Exception e){
            return AuthenticationResponse.builder()
                    .errorMessage("Tài khoản email đã tồn tại")
                    .build();
        }
        return AuthenticationResponse.builder()
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        try{
            Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserEmail(),
                            request.getUserPassword()
                    )
            );

            Optional<User> user = userRepository.findUserByUserEmail(request.getUserEmail());

            if (user.get().isLocked())
                return AuthenticationResponse.builder()
                        .errorMessage("Bạn vui lòng xác thực tài khoản để đăng nhập")
                        .build();

            var jwtToken = jwtService.generateJwtToken(user.get());
            return AuthenticationResponse.builder()
                    .jwtToken(jwtToken)
                    .user(user.get())
                    .build();
        }
        catch (AuthenticationException e){
            return AuthenticationResponse.builder()
                    .errorMessage("Tài khoản hoăc mật khẩu không hợp lệ")
                    .build();
        }
    }

    public GoogleUserAuthenticationResponse googleUserAuthenticate(GoogleUserAuthenticationRequest request){
        Optional<User> googleUser = userRepository.findUserByUserEmail(request.userEmail);

        if (googleUser.isEmpty()){
            User googleUser1 = new User(request.userEmail, request.userFirstName, request.userLastName, request.userAvatar, List.of(Role.ROLE_USER));
            userRepository.insert(googleUser1);
            var jwtToken = jwtService.generateJwtToken(googleUser1);
            return GoogleUserAuthenticationResponse.builder()
                    .jwtToken(jwtToken)
                    .user(googleUser1)
                    .build();
        }

        var jwtToken = jwtService.generateJwtToken(googleUser.get());
        return GoogleUserAuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .user(googleUser.get())
                .build();
    }
}

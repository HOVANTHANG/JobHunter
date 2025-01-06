package com.example.Jobhunter.controller.admin;

import com.example.Jobhunter.Util.SecurityUtil;
import com.example.Jobhunter.domain.RestResponse;
import com.example.Jobhunter.domain.dto.LoginDTO;
import com.example.Jobhunter.domain.dto.ResLoginDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                          SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        //put input include username/password into Security
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        //Authentication => Need write function loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(auth);

        //create a Token
        String access_Token = this.securityUtil.CreateToken(authentication);
        ResLoginDTO res = new ResLoginDTO();
        res.setAccess_token(access_Token);
        return  ResponseEntity.ok().body(res);
    }
}

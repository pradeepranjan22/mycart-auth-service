package com.secor.mycartauthservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class MainRestController {

    @Autowired
    CredentialRepository credentialRepository;

    @Autowired
    TokenService tokenService;


    @Autowired
    private TokenRepository tokenRepository;

    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody Credential credential)
    {
        credentialRepository.save(credential);
        return ResponseEntity.ok("User signed up successfully");
    }

    @GetMapping("login")
    public ResponseEntity<?> login(@RequestBody Credential credential)
    {
        Credential user = credentialRepository.findById(credential.getUsername()).orElse(null);

        if(user == null)
        {
            return ResponseEntity.badRequest().body("User not found");
        }
        if(user.getPassword().equals(credential.getPassword()))
        {
            //More to be done here - Token to  be sent to the user

            return ResponseEntity.ok().
                    header("Authorization", tokenService.generateToken(user.getUsername()).getTokenid().toString()).
                    body("User logged in successfully");
        }

        return ResponseEntity.badRequest().body("incorrect password");
    }

    @GetMapping("validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String token)
    {
        String[] tokenArray = token.split(" ");

        if(tokenService.validateToken(tokenArray[1]))
        {
            return ResponseEntity.ok("valid");
        }
        return ResponseEntity.ok("invalid");
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token)
    {
        String[] tokenArray = token.split(" ");

        Token token1 = tokenService.tokenRepository.findById(Integer.parseInt(tokenArray[1])).orElse(null);

        if(token1 == null)
        {
            return ResponseEntity.badRequest().body("invalid");
        }


        tokenRepository.updateStatusByTokenid("invalid", Integer.valueOf(tokenArray[1]));

        return ResponseEntity.ok("logged out successfully");
    }



}

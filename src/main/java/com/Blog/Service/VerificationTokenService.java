package com.Blog.Service;

import com.Blog.Model.User;
import com.Blog.Model.VerificationToken;
import com.Blog.Repository.UserRepository;
import com.Blog.Repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    @Autowired
    public VerificationTokenService(VerificationTokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public VerificationToken generateVerificationToken(User user){
        String token  = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpireDate(LocalDateTime.now().plusDays(1));

        return tokenRepository.save(verificationToken);
    }

    public void verifyAccount(String token){
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if(verificationToken != null && !verificationToken.getExpireDate().isBefore(LocalDateTime.now())){
            User user = verificationToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            tokenRepository.delete(verificationToken);
        }else {
            throw new RuntimeException("Token verification failed or expired");
        }
    }
}

package com.sgs.auth.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service // @Component. static class로
public class SaltUtil {

    public String encodePassword(String salt, String password){
        return BCrypt.hashpw(password, salt);
    }
    public String genSalt(){
        return BCrypt.gensalt();
    }
}

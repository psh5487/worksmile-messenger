package com.sgs.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgs.auth.dto.ApiResult;
import com.sgs.auth.model.SecurityUser;
import com.sgs.auth.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        log.info("Access Denied Handler");

        ObjectMapper objectMapper = new ObjectMapper();

        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json;charset=utf-8");

        ApiResult apiResult = new ApiResult(HttpStatus.UNAUTHORIZED.value(),"You don't have an authority to access",null);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser user = (SecurityUser)authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = user.getAuthorities();

        if(hasRole(authorities, UserRole.ROLE_NOT_PERMITTED_USER.name())){
            apiResult.setMessage("You didn't verify email");
        }

        PrintWriter out = httpServletResponse.getWriter();
        String jsonResponse = objectMapper.writeValueAsString(apiResult);
        out.print(jsonResponse);
    }

    private boolean hasRole(Collection<GrantedAuthority> authorities, String role) {
        return authorities.contains(new SimpleGrantedAuthority(role));
    }
}

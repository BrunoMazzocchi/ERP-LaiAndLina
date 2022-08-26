package com.laiandlina.erp.web.security;


import com.laiandlina.erp.domain.service.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.*;
import org.springframework.web.filter.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {


            String jwt = getJwt(request);
            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                String username = tokenProvider.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {

            }
        } catch (Exception e) {
            logger.error("Can NOT set user authentication -> Message: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    //We are checking if there is a jwt located in cookies.
    //This process will review cookies after sign in. If cookies with auth name are present, it will check.

    private String getJwt(HttpServletRequest request) {
        String authHeader = "Not found";

        try{
            if(getCookieByName(request, "Authorization") != null){
                authHeader = getCookieByName(request, "Authorization").getValue();
                return authHeader;
            }
        } catch (Exception exception){
            logger.error("Exception -> " + exception);
        }

        return null;
    }

    protected Cookie getCookieByName(HttpServletRequest request, String name) {
        try {
            if (request.getCookies() == null) {
                return null;
            }
            for (int i = 0; i < request.getCookies().length; i++) {
                if (request.getCookies()[i].getName().equals(name)) {
                    return request.getCookies()[i];
                }
            }
        } catch (Exception exception){
            logger.error("Exception -> " + exception);
        }
        return null;
    }
}
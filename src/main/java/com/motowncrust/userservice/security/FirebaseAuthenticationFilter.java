package com.motowncrust.userservice.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring(7);
            System.out.println(authHeader);
            try {
                FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(authToken);
                System.out.println(firebaseToken);
                System.out.println(firebaseToken.getUid());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        firebaseToken.getUid(), null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                logger.error("Firebase Authentication failed", e);
            }
        }
        filterChain.doFilter(request, response);
    }
}

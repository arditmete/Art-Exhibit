package com.edu.artexhibit.config;

import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.service.impl.ArtistService;
import com.edu.artexhibit.shared.ContextHelper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final ArtistService artistService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authorizationHeader = request.getHeader("Authorization");
            final JwtUtils jwtUtils = ContextHelper.getApplicationContext().getBean(JwtUtils.class);

            String username = null;
            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtils.getSubject(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                ArtistEntity artistEntity = artistService.findByUsername(username);

                if (jwtUtils.validateToken(jwt)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            artistEntity, null, artistEntity.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("User is not logged in!");

        }
    }
}

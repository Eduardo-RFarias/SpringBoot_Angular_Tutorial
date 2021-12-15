package edu.unb.reddit.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import edu.unb.reddit.exception.InvalidTokenException;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var jwtFromRequest = getJwtFromRequest(request);

		if (StringUtils.hasText(jwtFromRequest) && jwtProvider.validateToken(jwtFromRequest)) {
			var username = jwtProvider.getUsernameFromJwt(jwtFromRequest);

			var userDetails = userDetailsService.loadUserByUsername(username);

			var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		// TODO Figure out why the api is executing the filter in all requests
		return request.getRequestURI().startsWith("/api/auth");
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		var bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken)) {
			var bearerArray = bearerToken.split(" ");
			if (bearerArray[0].equals("Bearer")) {
				return bearerArray[1];
			}
		}

		throw new InvalidTokenException(String.format(
				"Could not get the Authorization Token from this request. Expected \"Bearer <Token>\", got \"%s\"",
				bearerToken));
	}
}

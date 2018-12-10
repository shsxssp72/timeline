package com.softwareTest.timeline.Config.SpringSecurity;

import com.softwareTest.timeline.Config.Constants;
import com.softwareTest.timeline.Utility.CryptoUtility;
import com.softwareTest.timeline.Utility.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.softwareTest.timeline.Config.Constants.IGNORE_PATH;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
	private final static Logger logger=LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
									FilterChain filterChain) throws ServletException, IOException
	{
//		if(!IGNORE_PATH.contains(httpServletRequest.getRequestURI()))
//		{
			logger.info("Entering JwtAuthenticationTokenFilter.");
			String authHeader=httpServletRequest.getHeader("Authorization");
			if(authHeader!=null&&authHeader.startsWith("Bearer "))
			{
				final String authBase64edToken=authHeader.substring("Bearer ".length());
				final String authToken=CryptoUtility.Base64Decoder(authBase64edToken);
				logger.info("Token: "+authToken);
				String username=JwtTokenUtil.parseToken(authToken,/*Unused*/Constants.TOKEN_SALT);
				logger.info("Username: "+username);

				if(username!=null&&SecurityContextHolder.getContext().getAuthentication()==null)
				{
					UserDetails userDetails=customUserDetailsService.loadUserByUsername(username);

					if(userDetails!=null)
					{
						UsernamePasswordAuthenticationToken authenticationToken=
								new UsernamePasswordAuthenticationToken(userDetails,null,
										userDetails.getAuthorities());
						authenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					}
				}
			}
//		}
		filterChain.doFilter(httpServletRequest,httpServletResponse);
	}
}

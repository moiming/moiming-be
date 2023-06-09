package com.peoplein.moiming.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.peoplein.moiming.NetworkSetting;
import com.peoplein.moiming.model.dto.auth.MemberLoginDto;
import com.peoplein.moiming.security.exception.BadLoginInputException;
import com.peoplein.moiming.security.token.JwtAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper om = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /*
      일반 Login 시도중임을 Filter
      결론적으로 인증객체와 Access Token, Refresh Token 을 발급해주는 경로
    */
    public JwtLoginFilter() {

        super(new AntPathRequestMatcher(NetworkSetting.API_SERVER
                + NetworkSetting.API_AUTH_VER
                + NetworkSetting.API_AUTH
                + "/login"
        ));

    }


    /*
      Jwt 요청시 처음으로 인지하고 authentication 을 수행하는 공간
      인증전 Authentication 객체를 형성하여 AuthenticationManager 에게 인증을 위임
      인증후 Authentication 객체를 세션에 저장하고 후속 진행
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        MemberLoginDto memberLoginDto = om.readValue(request.getReader(), MemberLoginDto.class);

        if (!StringUtils.hasText(memberLoginDto.getUid()) || !StringUtils.hasText(memberLoginDto.getPassword())) {
            String msg = "ID 혹은 PW 값을 전달받지 못했습니다";
            log.error(msg);
            throw new BadLoginInputException(msg);
        }

        JwtAuthenticationToken preAuthentication = new JwtAuthenticationToken(memberLoginDto.getUid(), memberLoginDto.getPassword());
        AuthenticationManager authenticationManager = getAuthenticationManager();

        return authenticationManager.authenticate(preAuthentication);

    }
}

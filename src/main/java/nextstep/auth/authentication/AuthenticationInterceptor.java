package nextstep.auth.authentication;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.application.dto.User;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailService userDetailsService;
    private final AuthenticationConverter authenticationConverter;

    protected AuthenticationInterceptor(UserDetailService userDetailsService,
                                        AuthenticationConverter authenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.authenticationConverter = authenticationConverter;
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = authenticationConverter.convert(request);
        Authentication authentication = authenticate(token);
        afterAuthentication(request, response, authentication);

        return false;
    }

    private Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        User loginMember = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(loginMember, authenticationToken);

        return new Authentication(loginMember);
    }

    private void checkAuthentication(User userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }

}

package nextstep.auth.authorization;

import nextstep.auth.context.SecurityContext;

import javax.servlet.http.HttpServletRequest;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

    @Override
    public SecurityContext getSecurityContext(HttpServletRequest request) {
        return (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }

}

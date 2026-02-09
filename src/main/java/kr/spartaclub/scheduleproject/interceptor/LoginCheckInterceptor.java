package kr.spartaclub.scheduleproject.interceptor;

import kr.spartaclub.scheduleproject.dto.user.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.spartaclub.scheduleproject.dto.user.SessionUser;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        SessionUser loginUser = (SessionUser) session.getAttribute("loginUser");

        if (loginUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // 컨트롤러로 전달
        request.setAttribute("userId", loginUser.getId());

        return true; // 다음 단계로 진행
    }
}

package com.woorifisa.wl.filter;

import com.woorifisa.wl.model.entity.User;
import com.woorifisa.wl.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AddInfoCheckFilter extends OncePerRequestFilter {

    private final UserService userService;

    // 생성자 주입
    public AddInfoCheckFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                    jakarta.servlet.http.HttpServletResponse response,
                                    FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 제외할 URL들
        if (requestURI.equals("/login-extra") ||
                requestURI.equals("/logout") ||
                requestURI.startsWith("/css/") ||
                requestURI.startsWith("/js/") ||
                requestURI.startsWith("/img/") ||  // 이미지 폴더
                requestURI.startsWith("/images/") ||  // 이미지 폴더
                requestURI.startsWith("/user/") ||
                requestURI.equals("/login") ||
                requestURI.equals("/login-success")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 세션에서 userId 확인
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user_id") != null) {
            Long userId = (Long) session.getAttribute("user_id");
            User user = userService.findById(userId);

            if (!user.isAddInfo()) {
                response.sendRedirect("/login-extra");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
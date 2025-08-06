package com.event.messageservice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Component
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {
    private static final String TRACE_ID = "traceId";

    /**
     * 요청
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {

        if(request.getAttribute(TRACE_ID) == null){ // UUID가 없으면 새로 생성
            request.setAttribute(TRACE_ID, UUID.randomUUID().toString());
        }

        MDC.put(TRACE_ID, request.getAttribute(TRACE_ID).toString());
        log.info("[Request UUID] {} - {}", request.getAttribute(TRACE_ID) , request.getRequestURI());
        return true;
    }

    /**
     * 응답
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("[Response UUID] {} - {}", request.getAttribute(TRACE_ID) , request.getRequestURI());
        MDC.clear();
    }
}

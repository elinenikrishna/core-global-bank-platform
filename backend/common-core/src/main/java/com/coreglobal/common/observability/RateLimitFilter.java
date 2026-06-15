package com.coreglobal.common.observability;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private record Window(long second, AtomicInteger count) {}
    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        long second = Instant.now().getEpochSecond();
        Window window = windows.compute(request.getRemoteAddr(), (key, current) ->
                current == null || current.second() != second ? new Window(second, new AtomicInteger(1)) : increment(current));
        if (window.count().get() > 100) {
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Rate limit exceeded");
            return;
        }
        chain.doFilter(request, response);
    }

    private Window increment(Window current) { current.count().incrementAndGet(); return current; }
}


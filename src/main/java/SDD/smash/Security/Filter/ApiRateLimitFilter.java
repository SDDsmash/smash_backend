package SDD.smash.Security.Filter;

import SDD.smash.Security.Service.ApiRateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class ApiRateLimitFilter extends OncePerRequestFilter {

    private final ApiRateLimitService apiRateLimitService;

    private final Environment env;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        // 프리플라이트의 경우 통과
        if("OPTIONS".equalsIgnoreCase(request.getMethod()))
        {
            filterChain.doFilter(request, response);
            return;
        }

        // 개발환경인 경우 레이트리밋을 적용하지 않음
        if(isDevProfileActive())
        {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = resolveIpThatConnectedToNginx(request);

        // 리버스 프록시를 통한 요청이 아니거나, ip를 확인할 수 없는 경우 요청 거절
        if(ip == null)
        {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        // 락 여부 확인
        long ipTtl = apiRateLimitService.checkIpLockAndINCAndMaybeLock(ip);
        if(ipTtl > 0)
        {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader("Retry-After", String.valueOf(ipTtl)); // 초 단위
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isDevProfileActive() {
        return Arrays.stream(env.getActiveProfiles())
                .anyMatch(p -> p.equalsIgnoreCase("dev"));
    }


    /**
     * Nginx로 직접 들어온 IP(Nginx가 XFF에 추가한 값)
     */
    private String resolveIpThatConnectedToNginx(HttpServletRequest request)
    {
        String xff = request.getHeader("X-Forwarded-For");
        if(xff == null || xff.isBlank())
        {
            return request.getRemoteAddr();
        }
        String[] parts = xff.split(",");
        String rightMost = parts[parts.length - 1].trim();
        return rightMost.isEmpty() ? null : rightMost;
    }
}

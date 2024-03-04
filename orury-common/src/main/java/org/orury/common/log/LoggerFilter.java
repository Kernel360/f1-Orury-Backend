package org.orury.common.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.config.SlackMessage;
import org.orury.common.error.code.LogTemplate;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Component
public class LoggerFilter extends OncePerRequestFilter {
    private final SlackMessage slackMessage;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var req = new ContentCachingRequestWrapper(request);
        var res = new ContentCachingResponseWrapper(response);

        //왜 이게 최초 로그인시 실행되는지 모르겠지만 일단 조건문으로 막았습니다.
        if (req.getRequestURI().equals("/highLightTitle.png")) return;

        log.info("=== INIT URI : {}", req.getRequestURI());

        filterChain.doFilter(req, res);

        String uri = req.getRequestURI();
        String method = req.getMethod();

        // request 정보
        String requesHeaderValues = extractRequestHeader(req);
        String requestBody = extractRequestBody(req);
        String requestPart = extractRequestPart(request);
        log.info("=== URI : {} , HTTP Method : {} , Header : {}, RequestBody : {}, RequestPart : {}", uri, method, requesHeaderValues, requestBody, requestPart);

        // response 정보
        String responseHeaderValues = extractResponseHeader(res);
        String responseBody = new String(res.getContentAsByteArray());

        if (!responseBody.contains("<head>"))
            log.info("=== URI : {} , HTTP Method : {} , Header : {} , ResponseBody : {}", uri, method, responseHeaderValues, responseBody);

        if (res.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            var template = LogTemplate.of(uri, requestBody, requestPart, responseBody);
            slackMessage.send(template);
        }
        res.copyBodyToResponse();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/swagger-ui", "/v3/api-docs", "/actuator/prometheus"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath)
                .anyMatch(path::startsWith);
    }

    private String extractRequestHeader(ContentCachingRequestWrapper req) {
        var headerNames = req.getHeaderNames();
        var headerValues = new StringBuilder();

        headerNames.asIterator()
                .forEachRemaining(headerKey -> {
                    var headerValue = req.getHeader(headerKey);

                    headerValues
                            .append("[")
                            .append(headerKey)
                            .append(" : ")
                            .append(headerValue)
                            .append("] ");
                });

        return (headerValues.isEmpty()) ? null : headerValues.toString();
    }

    private String extractRequestBody(ContentCachingRequestWrapper req) {
        String requestBody = new String(req.getContentAsByteArray());
        return (requestBody.isEmpty()) ? null : requestBody;
    }

    private String extractRequestPart(HttpServletRequest request) throws ServletException, IOException {
        if (request.getContentType() == null || !request.getContentType()
                .startsWith("multipart"))
            return null;

        var multiparts = request.getParts();

        var requestPartValues = new StringBuilder();

        multiparts.forEach(part -> {

            String partValue = (part.getHeader("Content-Disposition")
                    .contains("filename="))
                    ? extractFileName(part.getHeader("Content-Disposition"))
                    : request.getParameter(part.getName());

            requestPartValues
                    .append("[")
                    .append(part.getName())
                    .append(" : ")
                    .append(partValue)
                    .append("] ");
        });

        return requestPartValues.toString();
    }

    private String extractFileName(String partHeader) {
        for (String cd : partHeader.split(";")) {
            if (cd.trim()
                    .startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf("=") + 1)
                        .trim()
                        .replace("\"", "");
                int index = fileName.lastIndexOf(File.separator);
                return fileName.substring(index + 1);
            }
        }
        return null;
    }

    private String extractResponseHeader(ContentCachingResponseWrapper res) {
        var headerValues = new StringBuilder();

        res.getHeaderNames()
                .forEach(headerKey -> {
                    var headerValue = res.getHeader(headerKey);

                    headerValues
                            .append("[")
                            .append(headerKey)
                            .append(" : ")
                            .append(headerValue)
                            .append("] ");
                });

        return (headerValues.isEmpty()) ? null : headerValues.toString();
    }
}

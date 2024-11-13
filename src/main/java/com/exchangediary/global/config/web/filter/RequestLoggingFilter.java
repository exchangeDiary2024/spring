package com.exchangediary.global.config.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.web.multipart.support.MultipartResolutionDelegate.isMultipartRequest;

@Slf4j
public class RequestLoggingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);

        log.info(requestURI);
        if (isMultipartRequest(httpRequest)) {
            logMultipartRequest(httpRequest);
            chain.doFilter(request, responseWrapper);
        }
        else {
            RequestWrapper requestWrapper = new RequestWrapper(httpRequest);
            log.info("Request Body: " + requestWrapper.getRequestBody());
            chain.doFilter(requestWrapper, responseWrapper);
        }
        log.info("Response Body: " + responseWrapper.getResponseBody());
        responseWrapper.copyBodyToResponse();
    }

    private void logMultipartRequest(HttpServletRequest request) {
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
        MultipartHttpServletRequest multipart = multipartResolver.resolveMultipart(request);
        multipart.getFileMap().forEach((paramName, file) -> {
            if ("data".equals(paramName)) {
                try {
                    String jsonData = new String(file.getBytes(), StandardCharsets.UTF_8);
                    log.info("Form Field - Name: {}, Value: {}", paramName, jsonData);
                } catch (IOException e) {
                    log.error("Error reading jsonData: " + paramName, e);
                }
            } else {
                log.info("Form Field - Name: {}, Original File Name: {}, Size: {} bytes",
                        paramName,
                        file.getOriginalFilename(),
                        file.getSize());
            }
        });
    }

    @Override
    public void destroy() {
    }
}

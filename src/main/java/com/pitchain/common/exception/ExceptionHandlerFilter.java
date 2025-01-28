package com.pitchain.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.common.apiPayload.dto.ResponseDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            if (e instanceof GeneralHandler generalHandler) {
              ResponseDTO error = generalHandler.getError();
              setResponse(response, error);
            }
        }
    }

    private void setResponse(HttpServletResponse response, ResponseDTO error) throws IOException {
        CustomApiResponse<Object> body = CustomApiResponse.onFailure(error.getCode(), error.getMessage(), null);

        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(error.getHttpStatus().value());
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}

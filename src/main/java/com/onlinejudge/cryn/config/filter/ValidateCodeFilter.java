package com.onlinejudge.cryn.config.filter;

import com.onlinejudge.cryn.common.RestResponseEnum;
import com.onlinejudge.cryn.common.TokenConst;
import com.onlinejudge.cryn.common.URIConst;
import com.onlinejudge.cryn.dto.ImageCode;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Arrays;

public class ValidateCodeFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(ValidateCodeFilter.class);

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (Arrays.asList(URIConst.VALIDATE_CODE_ARRAY_URI).contains(requestURI)&&
                "post".equalsIgnoreCase(request.getMethod())) {
            try {
                validate(new ServletWebRequest(request));
            } catch (Exception e) {
                response.setContentType("application/json;charset=UTF-8");
                logger.error(e.getMessage());
                PrintWriter writer = response.getWriter();
                RestResponseVO<Object> responseVO = RestResponseVO.createByErrorStatusMessage(RestResponseEnum.VALIDATE_CODE_ERROR.getStatus(),
                        e.getMessage());
                writer.write(JsonUtil.obj2String(responseVO));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


    private void validate(ServletWebRequest request) {
        String imageCodeParam = request.getParameter("imageCode");
        ImageCode imageCode = (ImageCode) sessionStrategy.getAttribute(request, TokenConst.SessionKey.IMAGE_CODE);
        if (imageCode == null) {
            throw new RuntimeException("??????????????????");
        }
        if (StringUtils.isBlank(imageCode.getCode())) {
            throw new RuntimeException("??????????????????");
        }
        if (LocalDateTime.now().isAfter(imageCode.getExpireTime())) {
            throw new RuntimeException("??????????????????");
        }
        if (!StringUtils.equals(imageCodeParam, imageCode.getCode())) {
            throw new RuntimeException("??????????????????");
        }
    }
}

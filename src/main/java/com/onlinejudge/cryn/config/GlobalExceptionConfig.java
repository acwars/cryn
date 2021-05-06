package com.onlinejudge.cryn.config;

import com.onlinejudge.cryn.response.RestResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionConfig {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionConfig.class);

    private final String ERROR_VIEW = "500";

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object solveException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        logger.error("requestURI:{} Exception:", request.getRequestURI(), exception);
        if (isAjaxRequest(request)) {
            String message = exception.getMessage();
            if (message == null) {
                message = "exception";
            }
            return RestResponseVO.createByErrorMessage(message);
        }
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("uri", request.getRequestURI());
        modelAndView.addObject("errorMsg", exception.getMessage());
        modelAndView.setViewName(ERROR_VIEW);
        return modelAndView;
    }


    private boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return (header != null && "XMLHttpRequest".equals(header));
    }
}

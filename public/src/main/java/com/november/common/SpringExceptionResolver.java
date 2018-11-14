package com.november.common;

import com.november.exception.LibraryException;
import com.november.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author skrT
 * @create 2018/10/29 13:53
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURI();
        ModelAndView mv;
        String defaultMsg = "System error";
        if(url.endsWith(".json")){
            if(ex instanceof LibraryException || ex instanceof ParamException){
                JsonData result = JsonData.fail(ex.getMessage());
                mv = new ModelAndView("jsonView",result.toMap());
            }else{
                log.error("unknown json exception,url:"+url,ex);
                JsonData result = JsonData.fail(defaultMsg);
                mv = new ModelAndView("jsonView",result.toMap());
            }
        }else if(url.endsWith(".html")){
            log.error("unknown page exception,url:"+url,ex);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("exception",result.toMap());
        }else{
            log.error("unknown exception,url:"+url,ex);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("jsonView",result.toMap());
        }
        return mv;
    }
}

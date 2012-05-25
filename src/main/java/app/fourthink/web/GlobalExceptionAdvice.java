package app.fourthink.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GlobalExceptionAdvice implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Object handler, Exception ex) {
        String view;
        if (ex instanceof IllegalArgumentException) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            view = "errors/not-found";
        } else if (ex instanceof IllegalStateException) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            view = "errors/conflict";
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            view = "errors/internal";
        }
        ModelAndView mav = new ModelAndView(view);
        mav.addObject("message", ex.getMessage());
        return mav;
    }
}

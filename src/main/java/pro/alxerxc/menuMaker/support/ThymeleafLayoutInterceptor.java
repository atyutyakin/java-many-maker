package pro.alxerxc.menuMaker.support;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ThymeleafLayoutInterceptor implements HandlerInterceptor {
    private static final String DEFAULT_LAYOUT = "layouts/default";
    private static final String DEFAULT_VIEW_ATTRIBUTE_NAME = "view";

    public static String defaultLayout = DEFAULT_LAYOUT;
    public static String viewAttributeName = DEFAULT_VIEW_ATTRIBUTE_NAME;

    public ThymeleafLayoutInterceptor() {
        System.out.println("CREATING ThymeleafLayoutInterceptor");
    }

    public static void setDefaultLayout(String defaultLayout) {
        Assert.hasLength(defaultLayout, "defaultLayout must not be blank");
        ThymeleafLayoutInterceptor.defaultLayout = defaultLayout;
    }

    public static void setViewAttributeName(String viewAttributeName) {
        Assert.hasLength(viewAttributeName, "defaultLayout must not be blank");
        ThymeleafLayoutInterceptor.viewAttributeName = viewAttributeName;
    }

    public static String getDefaultLayout() {
        return defaultLayout;
    }

    public static String getViewAttributeName() {
        return viewAttributeName;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           @Nullable ModelAndView modelAndView) {
        if (modelAndView == null || !modelAndView.hasView()) {
            return;
        }
        String originalViewName = modelAndView.getViewName();
        if (originalViewName == null || isRedirectOrForward(originalViewName)) {
            return;
        }
        String layoutName = getLayoutName(handler);
        modelAndView.setViewName(layoutName);
        modelAndView.addObject(getViewAttributeName(), originalViewName);
        System.out.println("layout substitution: " + originalViewName + " -> " + layoutName);
    }

    private String getLayoutName(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Layout layoutMethodAnnotation = handlerMethod.getMethodAnnotation(Layout.class);
            if (layoutMethodAnnotation != null) {
                return layoutMethodAnnotation.value();
            }
            Layout layoutClassAnnotation = handlerMethod.getBeanType().getAnnotation(Layout.class);
            if (layoutClassAnnotation != null) {
                return layoutClassAnnotation.value();
            }
        }
        return getDefaultLayout();
    }

    private boolean isRedirectOrForward(String viewName) {
        return viewName.startsWith("redirect:") || viewName.startsWith("forward:");
    }
}

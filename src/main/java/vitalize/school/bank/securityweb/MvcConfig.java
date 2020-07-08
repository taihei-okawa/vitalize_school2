package vitalize.school.bank.securityweb;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import lombok.AllArgsConstructor;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * 「/login」というURLからlogin.htmlを呼び出す
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");

    }
}
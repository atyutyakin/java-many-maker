package pro.alxerxc.menuMaker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pro.alxerxc.menu-maker")
@Getter
@Setter
public class AppConfig {
    @Value("${spring.application.name}")
    private String name;

    public static String pageSize = "15";
}

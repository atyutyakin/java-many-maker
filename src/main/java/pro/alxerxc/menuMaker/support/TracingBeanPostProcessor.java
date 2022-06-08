package pro.alxerxc.menuMaker.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TracingBeanPostProcessor implements BeanPostProcessor {

    @Value("${pro.alxerxc.menu-maker.log-beans-creation : false}")
    private boolean logBeanCreation = false;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (logBeanCreation) {
            String beanAsString = "<???>";
            try {
                beanAsString = bean.toString();
            } catch (Exception e) {
                // hide this under a carpet
            }

            String prettyBeanName = (beanName != null) ? beanName : "<???>";
            String prettyBeanClass = (bean != null) ? bean.getClass().getCanonicalName() : "<???>";

            /*
            String infoMessage = String.format("Bean created (info): %s", prettyBeanClass);
            String debugMessage = String.format("Bean created (debug): name = %s, class = %s",
                    prettyBeanName, prettyBeanClass);
            String traceMessage = String.format("Bean created (trace): \n" +
                            "   name = %s, \n" +
                            "   class = %s, \n" +
                            "   bean = %s",
                    prettyBeanName,
                    prettyBeanClass,
                    beanAsString);
             */

            log.info("Bean created (info): {}", prettyBeanClass);
            log.debug("Bean created (debug): name = {}, class = {}", prettyBeanName, prettyBeanClass);
            log.trace("Bean created (trace): \n  name = {},\n  class = {},\n  bean = {}",
                    prettyBeanName, prettyBeanClass, beanAsString);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}

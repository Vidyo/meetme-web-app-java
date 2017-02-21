package io.vidyo.config;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.RequestDumpingHandler;
import io.undertow.servlet.api.DeploymentInfo;
import org.springframework.boot.context.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UndertowConfiguration {

    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
        factory.addDeploymentInfoCustomizers(new UndertowDeploymentInfoCustomizer() {
            @Override
            public void customize(DeploymentInfo deploymentInfo) {
                deploymentInfo.addInitialHandlerChainWrapper(new HandlerWrapper() {
                    @Override
                    public HttpHandler wrap(HttpHandler handler) {
                        return new RequestDumpingHandler(handler);
                    }
                });
            }
        });

        return factory;
    }

}

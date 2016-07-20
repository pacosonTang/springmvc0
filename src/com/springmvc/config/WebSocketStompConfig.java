package com.springmvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	  public void configureMessageBroker(MessageBrokerRegistry config) {
	      config.enableSimpleBroker("/topic", "/queue", "/user");
	      config.setApplicationDestinationPrefixes("/app");
	      config.setUserDestinationPrefix("/user");
	  }

	  @Override
	  public void registerStompEndpoints(StompEndpointRegistry registry) {
	      registry.addEndpoint("/stompServer").withSockJS();
	  }
}

/*
 * Copyright 2013-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.sleuth.autoconfig.instrument.redis;

import io.lettuce.core.tracing.Tracing;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.autoconfig.brave.instrument.redis.TraceRedisProperties;
import org.springframework.cloud.sleuth.instrument.redis.TraceLettuceClientResourcesBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * Auto-configuration} enables Redis span information propagation.
 *
 * @author Marcin Grzejszczak
 * @since 3.1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "spring.sleuth.redis.enabled", matchIfMissing = true)
@ConditionalOnBean(Tracer.class)
@ConditionalOnClass(Tracing.class)
@AutoConfigureBefore({ RedisAutoConfiguration.class })
@EnableConfigurationProperties(TraceRedisProperties.class)
public class TraceRedisAutoConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(value = "spring.sleuth.redis.legacy.enabled", havingValue = "false", matchIfMissing = true)
	static class LettuceConfiguration {

		@Bean
		@ConditionalOnBean(Tracing.class)
		TraceLettuceClientResourcesBuilderCustomizer traceLettuceClientResourcesBuilderCustomizer(Tracing tracing) {
			return new TraceLettuceClientResourcesBuilderCustomizer(tracing);
		}

	}

}

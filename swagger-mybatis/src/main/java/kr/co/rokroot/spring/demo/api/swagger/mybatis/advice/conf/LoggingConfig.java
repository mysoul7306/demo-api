package kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.conf;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Plugin(name = "LoggingConfig", category = ConfigurationFactory.CATEGORY)
@Order(10)
public class LoggingConfig extends ConfigurationFactory {

    protected final String[] SUFFIXES = new String[]{".json", "*"};
    protected static final String PATTERN = "[%d{HH:mm:ss.SSS}] [%p{length=2}] %cyan{%c{1.}{(%M:%L)}} - %m%n";

    public static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        builder.setStatusLevel(Level.INFO);
        builder.setMonitorInterval("30");

        builder.add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL));

        PatternLayout pattern = PatternLayout.newBuilder()
                .withPattern(PATTERN)
                .withCharset(StandardCharsets.UTF_8)
                .build();

        AppenderComponentBuilder appenderBuilder = builder.newAppender("ConsoleAppender", "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);

        appenderBuilder.add(builder.newLayout("PatternLayout")
                .addAttribute("pattern", pattern));

        appenderBuilder.add(builder.newFilter("MarkerFilter", Filter.Result.DENY, Filter.Result.NEUTRAL)
                .addAttribute("marker", "FLOW"));

        builder.add(appenderBuilder);

        builder.add(builder.newRootLogger(Level.DEBUG)
                .add(builder.newAppenderRef("ConsoleAppender")));

        return builder.build();
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return this.getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return this.SUFFIXES;
    }
}

package kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.conf;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Order(50)
@Plugin(name = "LoggingConfig", category = ConfigurationFactory.CATEGORY)
public class LoggingConfig extends ConfigurationFactory {

    private final String profile = "local";

    public static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        final String CONSOLE = "CONSOLE";
        final String PATTERN = "[%d{HH:mm:ss.SSS}] [%highlight{%-5p}] [%t] %cyan{%c{1.}} [%M:%L] - %m%n";

        PatternLayout pattern = PatternLayout.newBuilder()
                .withPattern(PATTERN)
                .withCharset(StandardCharsets.UTF_8)
                .withDisableAnsi(false)
                .build();

        builder.setConfigurationName(name);
        builder.setStatusLevel(Level.ERROR);

        FilterComponentBuilder thresholdFilter = builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL).addAttribute("level", Level.DEBUG);
        FilterComponentBuilder markerFilter = builder.newFilter("MarkerFilter", Filter.Result.DENY, Filter.Result.NEUTRAL).addAttribute("marker", "FLOW");

        LayoutComponentBuilder consoleLayout = builder.newLayout("PatternLayout").addAttribute("pattern", pattern).addAttribute("disableAnsi", false);
        AppenderComponentBuilder consoleAppender = builder.newAppender(CONSOLE, CONSOLE).addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);

        consoleAppender.add(thresholdFilter);
        consoleAppender.add(consoleLayout);

        builder.add(consoleAppender);

        LayoutComponentBuilder fileLayout = builder.newLayout("PatternLayout").addAttribute("pattern", "[%d{HH:mm:ss.SSS}] [%-5level] [%thread] %logger{30}[%method:%line] - %msg%n");
        ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
                .addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100M"));
        AppenderComponentBuilder fileAppender = builder.newAppender("File_Appender", "RollingFile")
                .addAttribute("fileName", "target/rolling.log")
                .addAttribute("filePattern", "target/archive/rolling-%d{MM-dd-yy}.log.gz")
                .add(fileLayout)
                .addComponent(triggeringPolicy);

        builder.add(builder.newRootLogger(Level.DEBUG).add(builder.newAppenderRef(CONSOLE)));

        return builder.build();
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[]{".json", "*"};
    }
}

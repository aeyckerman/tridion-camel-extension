package com.tridion.storage.extensions.configuration;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.configuration.step.ConfigurationStep;
import com.tridion.configuration.step.StepArtifacts;
import com.tridion.storage.util.SpringContextDynamicConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringConfigurationLoader implements ConfigurationStep
{
private Logger log;

public SpringConfigurationLoader()
{
    this.log = LoggerFactory.getLogger(SpringConfigurationLoader.class); }

    public StepArtifacts configure(Configuration config, StepArtifacts artifacts) throws ConfigurationException {
        this.log.info("Starting loading spring application context");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        SpringContextDynamicConfigurer.configure(applicationContext, config);
        this.log.info("Finished loading spring application context");
        return artifacts.addArtifact("applicationContext", applicationContext);
    }
}


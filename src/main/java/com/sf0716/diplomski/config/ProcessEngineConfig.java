package com.sf0716.diplomski.config;

import javax.sql.DataSource;

import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProcessEngineConfig {

	private final String RESOURCE_PATH = "classpath:prijavaDiplomskog.bpmn";
	
	@Value("${spring.datasource.url}") 
	private String url;
	@Value("${spring.datasource.username}") 
	private String username;
	@Value("${spring.datasource.password}") 
	private String password;
	
	@Autowired
	ApplicationContext applicationContext;

  @Bean
  public DataSource dataSource() {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    return new DataSourceTransactionManager(dataSource());
  }

  @Bean
  public SpringProcessEngineConfiguration processEngineConfiguration() {
    SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();

    config.setDataSource(dataSource());
    config.setTransactionManager(transactionManager());
    config.setDatabaseSchemaUpdate("true");
    config.setHistory("full");
    config.setJobExecutorActivate(true);
    config.setDefaultSerializationFormat("application/json");
    // Load the process definition from a resource file (.bpmn)
    Resource resource = applicationContext.getResource(RESOURCE_PATH);
    Resource[] resources = {resource};
    config.setDeploymentResources(resources);
    
    return config;
  }

  @Bean
  public ProcessEngineFactoryBean processEngine() {
    ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
    factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
    return factoryBean;
  }
}
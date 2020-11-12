package com.jdbc.jdbc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
@Slf4j
public class JdbcApplication implements CommandLineRunner {
	@Autowired
	private DataSource barDataSource;
	@Autowired
	private JdbcTemplate jdbcTemplateBar;

	public static void main(String[] args) {
		SpringApplication.run(JdbcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		showConnection();
		showData();
	}
	private void showConnection() throws SQLException{
		log.info(barDataSource.toString());
		Connection conn = barDataSource.getConnection();
		log.info(conn.toString());
		conn.close();
	}
	private void showData(){
		jdbcTemplateBar.queryForList("select * from BAR").forEach((item)->{
			log.info(item.toString());
		});
	}
	//bar数据源
	@Bean
	@ConfigurationProperties("bar.datasource")
	public DataSourceProperties barDataSourceProperties(){
		DataSourceProperties dataSourceProperties = new DataSourceProperties();
		return dataSourceProperties;
	}
	@Bean
	public DataSource barDataSource(){
		DataSourceProperties dataSourceProperties = barDataSourceProperties();
		log.info("bar datasource:{}",dataSourceProperties.getSchema());
		return  dataSourceProperties.initializeDataSourceBuilder().build();
	}
	@Bean
	@Resource
	public PlatformTransactionManager barTxManager(DataSource barDataSource){
		return new DataSourceTransactionManager(barDataSource);
	}
	@Bean
	public JdbcTemplate jdbcTemplateBar(@Qualifier("barDataSource")DataSource dataSource){
		return new JdbcTemplate(dataSource);
	}

	//foo数据源
	@Bean
	@ConfigurationProperties("foo.datasource")
	public DataSourceProperties fooDataSourceProperties(){
		DataSourceProperties dataSourceProperties =new DataSourceProperties();
		return dataSourceProperties;
	}
	@Bean
	public DataSource fooDataSource(){
		DataSourceProperties dataSourceProperties = fooDataSourceProperties();
		log.info("foo datasource{}",dataSourceProperties.getUrl());
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}
	@Bean
	@Resource
	public PlatformTransactionManager fooTxManager(){
		return new DataSourceTransactionManager(fooDataSource());
	}
	@Bean
	public JdbcTemplate jdbcTemplateFoo(@Qualifier("fooDataSource")DataSource dataSource){
		return new JdbcTemplate(dataSource);
	}
}

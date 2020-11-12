package com.druid.druid;

import com.druid.druid.dao.BarDao;
import com.druid.druid.dao.BatchBarDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
@Slf4j
public class DruidApplication implements CommandLineRunner {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private BarDao barDao;
	@Autowired
	private BatchBarDao batchBarDao;

	public static void main(String[] args) {
		SpringApplication.run(DruidApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		batchBarDao.batchInsert();
//		barDao.insertData();
		barDao.listData();
		//showConn();
		//showData();
	}
	private void showConn() throws SQLException {
		log.info(dataSource.toString());
		Connection conn = dataSource.getConnection();
		log.info(conn.toString());
		conn.close();
	}
	private void showData(){
		jdbcTemplate.queryForList("select * from bar").forEach(item->{
			log.info(item.toString());
		});
	}
	@Bean
	@Autowired
	public SimpleJdbcInsert simpleJdbcInsert(JdbcTemplate jdbcTemplate){
		return new SimpleJdbcInsert(jdbcTemplate).withTableName("bar").usingGeneratedKeyColumns("id");
	}
}

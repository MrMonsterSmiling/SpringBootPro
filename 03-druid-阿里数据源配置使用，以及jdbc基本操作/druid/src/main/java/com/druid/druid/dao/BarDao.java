package com.druid.druid.dao;

import com.druid.druid.entity.Bar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Repository
public class BarDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimpleJdbcInsert simpleJdbcInsert;
    public void insertData(){
        Arrays.asList("b","c").forEach(bar->{
            jdbcTemplate.update("insert into bar(bar) values(?)",bar);
        });
        HashMap<String,String> row = new HashMap<>();
        row.put("bar","d");
        Number id = simpleJdbcInsert.executeAndReturnKey(row);
        log.info("d insert(id:{})",id.longValue());
    }
    public void listData(){
        log.info("Count:{}",jdbcTemplate.queryForObject("select count(*) from bar",Long.class));
        List<String> list = jdbcTemplate.queryForList("select bar from bar",String.class);
        list.forEach(item->{
            log.info("bar of BAR:{}",item);
        });
        List<Bar> barList = jdbcTemplate.query("select * from bar", new RowMapper<Bar>() {
            @Override
            public Bar mapRow(ResultSet resultSet, int i) throws SQLException {
                return Bar.builder()
                          .id(resultSet.getLong("id"))
                          .bar(resultSet.getString("bar"))
                          .build();
            }
        });
        barList.forEach(item->{
            log.info("bar:{}",item.toString());
        });
    }
}

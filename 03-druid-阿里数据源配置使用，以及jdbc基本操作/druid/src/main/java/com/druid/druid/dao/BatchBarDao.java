package com.druid.druid.dao;

import com.druid.druid.entity.Bar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量操作（插入）
 */
@Repository
public class BatchBarDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public void batchInsert(){
        jdbcTemplate.batchUpdate("insert into bar(bar) values(?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1,"b-"+i);
                    }

                    @Override
                    public int getBatchSize() {
                        return 6;
                    }
                });
        List<Bar> barList = new ArrayList<>();
        barList.add(Bar.builder().id(100L).bar("b100").build());
        barList.add(Bar.builder().id(101L).bar("b101").build());
        namedParameterJdbcTemplate.batchUpdate("insert into bar(id,bar)values(:id,:bar)",
                SqlParameterSourceUtils.createBatch(barList));
    }
}

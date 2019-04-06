package com.jhj.dao.map;

import com.jhj.comm.Utils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LongMapper implements RowMapper<Long> {

    private String indexName;

    public LongMapper(String indexName) {
        this.indexName = indexName;
    }

    @Override
    public Long mapRow(ResultSet rs, int i) throws SQLException {
         if(rs==null)
            return null;

        try{
            return rs.getLong(indexName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

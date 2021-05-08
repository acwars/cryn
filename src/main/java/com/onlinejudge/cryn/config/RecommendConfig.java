package com.onlinejudge.cryn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;

import javax.sql.DataSource;

@Configuration
public class RecommendConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    public DataModel getMySQLDataModel() {
        JDBCDataModel dataModel=new MySQLJDBCDataModel(dataSource,"recommends_problems_data","user_id","problem_id","problem_rating", "create_time");
        return dataModel;
    }
}
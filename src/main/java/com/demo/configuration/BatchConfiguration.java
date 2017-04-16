package com.demo.configuration;

import com.demo.listener.MyListener;
import com.demo.model.Person;
import com.demo.processor.PersonProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by maharshi.bhatt on 16/4/17.
 */
@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job importUserJob(){
        return jobBuilderFactory.get("InsertInDatabase")
                                .flow(step1())
                                .end()
                                .build();

    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("FirstStep")
                                .<Person,Person>chunk(100)
                                .reader(reader())
                                .processor(new PersonProcessor())
                                .writer(writer())
                                .listener(new MyListener())
                                .allowStartIfComplete(false)
                                .build();
    }

    @Bean
    public ItemStreamReader<Person> reader(){
        JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT first_name, last_name FROM person");
        reader.setRowMapper(((resultSet, i) -> new Person(resultSet.getString("first_name"),resultSet.getString("last_name"))));

        return reader;
    }

    @Bean
    public ItemWriter<Person> writer(){
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)" +
                "ON DUPLICATE KEY UPDATE last_name=:firstName");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

}

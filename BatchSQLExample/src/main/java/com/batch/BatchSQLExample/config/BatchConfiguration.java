package com.batch.BatchSQLExample.config;

import com.batch.BatchSQLExample.StopAllJobs;
import com.batch.BatchSQLExample.model.User;
import com.batch.BatchSQLExample.processor.UserItemProcessor;
import com.batch.BatchSQLExample.writer.LogginItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DatabaseConfig databaseConfig;

    @Value("${total:10}")
    private int total;
//
//    @Bean
//    public SqlPagingQueryProviderFactoryBean queryProviderFactoryBean() throws SQLException {
//        SqlPagingQueryProviderFactoryBean providerFactoryBean = new SqlPagingQueryProviderFactoryBean();
//        providerFactoryBean.setDataSource(databaseConfig.dataSource());
//        providerFactoryBean.setSelectClause("SELECT name FROM user");
//        return providerFactoryBean;
//    }


//    @Bean
//    public JdbcCursorItemReader<User> reader() throws SQLException {
//        JdbcCursorItemReader<User> reader = new JdbcCursorItemReader<User>();
//        reader.setDataSource(databaseConfig.dataSource());
//        reader.setSql("SELECT id, name FROM user");
//        reader.setRowMapper(new UserRowMapper());
//
//        return reader;
//    }

//    @Bean
//    public ItemReader<User> reader(PagingQueryProvider queryProvider) throws SQLException {
//        return new JdbcPagingItemReaderBuilder<User>().name("pagingItemReader").pageSize(1).queryProvider(queryProvider).rowMapper(new BeanPropertyRowMapper<>(User.class)).build();
//    }
//
//
//    @Bean
//    public UserItemProcessor processor(){
//        return new UserItemProcessor();
//    }
//
//    @Bean
//    public JdbcBatchItemWriter<User> writer(DataSource dataSource) throws SQLException {
//        return new JdbcBatchItemWriterBuilder<User>()
//                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//                .sql("INSERT INTO user (name) VALUES (:name)")
//                .dataSource(databaseConfig.dataSource())
//                .build();
//    }
//
//    @Bean
//    public Step step1(@Qualifier("reader") ItemReader<User> reader) throws SQLException {
//        return stepBuilderFactory.get("step1").<User, User> chunk(10)
//                .reader(reader)
//                .processor(processor())
//                .writer(writer(databaseConfig.dataSource()))
//                .build();
//    }
//
//    @Bean
//    public Job exportUserJob(@Qualifier("exportUserJob") Step step) throws SQLException {
//        return jobBuilderFactory.get("exportUserJob")
//                .incrementer(new RunIdIncrementer())
//                .flow(step)
//                .end()
//                .build();
//    }


    @Bean
    public ItemReader<User> jdbcPaginationItemReader(DataSource dataSource, PagingQueryProvider queryProvider) {
        return new JdbcPagingItemReaderBuilder<User>()
                .name("pagingItemReader")
                .dataSource(dataSource)
                .pageSize(1)
                .queryProvider(queryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(User.class))
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean queryProvider(DataSource dataSource) {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();

        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM tbl_user");
        queryProvider.setSortKeys(sortByNameAsc());

        return queryProvider;
    }

    private Map<String, Order> sortByNameAsc() {
        Map<String, Order> sortConfiguration = new HashMap<>();
        sortConfiguration.put("name", Order.ASCENDING);
        return sortConfiguration;
    }

    @Bean
    public ItemWriter<User> jdbcPaginationItemWriter() {
        return new LogginItemWriter();
    }

    @Bean
    public UserItemProcessor processor() {
        return new UserItemProcessor();
    }

    /**
     * Creates a bean that represents the only step of our batch job.
     * @param reader
     * @param writer
     * @param stepBuilderFactory
     * @return
     */
    @Bean
    public Step jdbcPaginationStep(@Qualifier("jdbcPaginationItemReader") ItemReader<User> reader,
                                   @Qualifier("jdbcPaginationItemWriter") ItemWriter<User> writer,
                                   StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("jdbcPaginationStep")
                .<User, User>chunk(total)
                .reader(reader)
                .processor(processor())
                .writer(writer)
                .build();
    }

    /**
     * Creates a bean that represents our example batch job.
     * @param exampleJobStep
     * @param jobBuilderFactory
     * @return
     */
    @Bean
    public Job jdbcPaginationJob(@Qualifier("jdbcPaginationStep") Step exampleJobStep,
                                 JobBuilderFactory jobBuilderFactory) {
        Job databaseCursorJob = jobBuilderFactory.get("jdbcPaginationJob")
                .incrementer(new RunIdIncrementer())
                .flow(exampleJobStep)
                .end()
                .build();
        return databaseCursorJob;
    }

}

package com.ishwaraju.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.ishwaraju.entity.User;
import com.ishwaraju.entity.Voltage;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public FlatFileItemReader<Voltage> readerVoltage() {
		return new FlatFileItemReaderBuilder<Voltage>().name("voltItemReader")
				.resource(new ClassPathResource("Volts.csv")).delimited().names(new String[] { "volt", "time" })
				.lineMapper(lineMapper()).fieldSetMapper(new BeanWrapperFieldSetMapper<Voltage>() {
					{
						setTargetType(Voltage.class);
					}
				}).build();
	}

	@Bean
	public FlatFileItemReader<User> readerUser() {
		System.out.println("****************************************");
		return new FlatFileItemReaderBuilder<User>().name("userItemReader").resource(new ClassPathResource("user.csv"))
				.delimited().names(new String[] { "name","salary" }).lineMapper(lineMapperUser())
				.fieldSetMapper(new BeanWrapperFieldSetMapper<User>() {
					{
						setTargetType(User.class);
					}
				}).build();
	}

	@Bean
	public LineMapper<User> lineMapperUser() {
		final DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
		final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] { "name","salary" });
		final UserFieldSetMapper fieldSetMapper = new UserFieldSetMapper();
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);
		return defaultLineMapper;
	}

	@Bean
	public LineMapper<Voltage> lineMapper() {

		final DefaultLineMapper<Voltage> defaultLineMapper = new DefaultLineMapper<>();
		final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(";");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] { "volt", "time" });

		final VoltageFieldSetMapper fieldSetMapper = new VoltageFieldSetMapper();
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		return defaultLineMapper;
	}
	
	@Bean
	public UserProcessor userProcessor() {
		return new UserProcessor();
	}

	@Bean
	public VoltageProcessor processor() {
		return new VoltageProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<Voltage> writer(final DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Voltage>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO voltage (volt, time) VALUES (:volt, :time)").dataSource(dataSource).build();
	}
	
	@Bean
	public JdbcBatchItemWriter<User> writerUser(final DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<User>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO user (name, salary) VALUES (:name,:salary)").dataSource(dataSource).build();
	}

	@Bean
	public Job importVoltageJob(NotificationListener listener, Step step1) {
		System.out.println(step1);
		return jobBuilderFactory.get("importVoltageJob").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step1).end().build();
	}
	
	@Bean
	public Job importUserJob(NotificationListener listener, Step step2) {
		System.out.println(step2);
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step2).end().build();
	}

	@Bean
	public Step step1(JdbcBatchItemWriter<Voltage> writer) {
		return stepBuilderFactory.get("step1").<Voltage, Voltage>chunk(10).reader(readerVoltage()).processor(processor())
				.writer(writer).build();
	}
	
	@Bean
	public Step step2(JdbcBatchItemWriter<User> writer) {
		return stepBuilderFactory.get("step1").<User, User>chunk(10).reader(readerUser()).processor(userProcessor())
				.writer(writer).build();
	}
}
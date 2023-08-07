package me.sathish.cityofsprunsinjester.batch;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import me.sathish.cityofsprunsinjester.data.RunsData;
import me.sathish.cityofsprunsinjester.data.RunsDataInput;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class CityRunProcessConfig {
    public static final String[] FEILDS = new String[]{"Activity_Type", "Date", "Favorite", "Title", "Distance", "Calories", "Time", "Avg HR", "Max HR", "Aerobic TE",
            "Avg Run Cadence", "Max Run Cadence", "Avg Pace", "Best Pace", "Total Ascent", "Total Descent", "Avg Stride Length", "Avg Vertical Ratio", "Avg Vertical Oscillation", "Avg Ground Contact Time",
            "Training Stress Score", "Avg Power", "max Power", "Grit", "low", "Total Strokes", "Avg. Swolf", "Avg Stroke Rate", "Total Reps", "Live Time", "Min Temp", "Surface Interval",
            "Decompression", "Best Lap Time", "Number of Laps", "Max Temp", "Moving Time", "Elapsed Time", "Min Elevation"};
    @Autowired
    private CityRunRepos repository;
    @Autowired
    JobBuilder jobBuilder;
    @Autowired
    StepBuilder stepBuilder;

    @Bean
    public FlatFileItemReader<RunsDataInput> reader() {
        return new FlatFileItemReaderBuilder<RunsDataInput>().name("runDataInputReader").resource(new ClassPathResource("Activities.csv")).
                delimited().names(FEILDS).fieldSetMapper(new BeanWrapperFieldSetMapper<RunsDataInput>() {
                    {
                        setTargetType(RunsDataInput.class);
                    }
                }).build();
    }

    @Bean
    public CityRunsDataProcessor processor() {
        return new CityRunsDataProcessor();
    }

    @Bean
    public MongoItemWriter<RunsData> runsDataJpaItemWriter() {
        return new MongoItemWriterBuilder<RunsData>().template(new MongoOperations() {
        });
    }
    @Bean
    public Job importUserJob(JobRepository jobRepository,
                             JobCompletionNotificationListener  listener, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager, JpaItemWriter<RunsData> writer) {
        return new StepBuilder("step1", jobRepository)
                .<RunsDataInput, RunsData> chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}

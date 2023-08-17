package me.sathish.cityofsprunsinjester.batch;

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
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
@RequiredArgsConstructor
public class CityRunProcessConfig {
    public static final String[] FEILDS = new String[]{"Activity_Type", "Date", "Favorite", "Title", "Distance", "Calories", "Time", "Avg HR", "Max HR", "Aerobic TE",
            "Avg Run Cadence", "Max Run Cadence", "Avg Pace", "Best Pace", "Total Ascent", "Total Descent", "Avg Stride Length", "Avg Vertical Ratio", "Avg Vertical Oscillation", "Avg Ground Contact Time",
            "Training Stress Score", "Avg Power", "max Power", "Grit", "Flow", "Total Strokes", "Avg Swolf", "Avg Stroke Rate", "Total Reps", "Dive Time", "Min Temp", "Surface Interval",
            "Decompression", "Best Lap Time", "Number of Laps", "Max Temp", "Moving Time", "Elapsed Time", "Min Elevation","Max Elevation"};
    private final CityRunProcessService cityRunProcessService;
    private final JobCompletionNotificationListener jobCompletionNotificationListener;

    @Bean
    public DataSource batchDataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
                .addScript("/org/springframework/batch/core/schema-hsqldb.sql")
                .generateUniqueName(true).build();
    }

    @Bean
    public JdbcTransactionManager batchTransactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

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

    public ItemWriterAdapter<RunsData> itemWriterAdapter() {
        ItemWriterAdapter<RunsData> itemWriterAdapter = new ItemWriterAdapter<>();
        itemWriterAdapter.setTargetObject(cityRunProcessService);
        itemWriterAdapter.setTargetMethod("restCallToService");
        return itemWriterAdapter;
    }


    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<RunsDataInput, RunsData>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .listener(jobCompletionNotificationListener)
                .writer(itemWriterAdapter())
                .build();
    }
}

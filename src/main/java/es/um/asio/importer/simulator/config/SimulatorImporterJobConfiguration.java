package es.um.asio.importer.simulator.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.um.asio.importer.listener.JobCompletionNotificationListener;
import es.um.asio.importer.simulator.tasklet.processor.AcademicInstitutionJSONProcessor;
import es.um.asio.importer.simulator.tasklet.processor.LinesToJSONProcessor;
import es.um.asio.importer.simulator.tasklet.processor.ResearcherJSONProcessor;
import es.um.asio.importer.simulator.tasklet.processor.TitleDegreeJSONProcessor;
import es.um.asio.importer.simulator.tasklet.reader.AcademicInstitutionReader;
import es.um.asio.importer.simulator.tasklet.reader.ResearcherReader;
import es.um.asio.importer.simulator.tasklet.reader.TitleDegreeReader;
import es.um.asio.importer.simulator.tasklet.writer.KafkaWriter;

/**
 * Simulator importer configuration.
 */
@Configuration
public class SimulatorImporterJobConfiguration {
    /**
     * Job builder factory.
     */
    @Autowired
    private JobBuilderFactory jobs;

    /**
     * Step builder factory.
     */
    @Autowired
    private StepBuilderFactory steps;

    /**
     * Job execution listener.
     * 
     * @return
     */
    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionNotificationListener();
    }

    /**
     * Academic institution reader
     * 
     * @return
     */
    @Bean
    public AcademicInstitutionReader academicInstitutionReader() {
        return new AcademicInstitutionReader();
    }
    
    /**
     * Researcher reader.
     * 
     * @return
     */
    @Bean
    public ResearcherReader researcherReader() {
        return new ResearcherReader();
    }
    
    /**
     * Title - Degree reader
     * 
     * @return
     */
    @Bean
    public TitleDegreeReader titleDegreeReader() {
        return new TitleDegreeReader();
    }

    /**
     * Lines to JSON processor
     * 
     * @return
     */
    @Bean
    public LinesToJSONProcessor linesToJSONProcessor() {
        return new LinesToJSONProcessor();
    }

    /**
     * Academic institution processor
     * 
     * @return
     */
    @Bean
    public AcademicInstitutionJSONProcessor academicInstitutionJSONProcessor() {
        return new AcademicInstitutionJSONProcessor();
    }

    /**
     * Researcher processor
     * 
     * @return
     */
    @Bean
    public ResearcherJSONProcessor researcherJSONProcessor() {
        return new ResearcherJSONProcessor();
    }

    /**
     * Title - Degree processor
     * 
     * @return
     */
    @Bean
    public TitleDegreeJSONProcessor titleDegreeJSONProcessor() {
        return new TitleDegreeJSONProcessor();
    }

    /**
     * Kafka writer
     * 
     * @return
     */
    @Bean
    public KafkaWriter kafkaWriter() {
        return new KafkaWriter();
    }

    /**
     * Read academic institution lines step.
     * 
     * @return
     */
    @Bean
    protected Step readAcademicInstitution() {
        return this.steps.get("readAcademicInstitution").tasklet(this.academicInstitutionReader()).build();
    }
    
    /**
     * Read researcher lines step.
     * 
     * @return
     */
    @Bean
    protected Step readResearcher() {
        return this.steps.get("readResearcher").tasklet(this.researcherReader()).build();
    }
    
    /**
     * Read academic institution lines step.
     * 
     * @return
     */
    @Bean
    protected Step readTitleDegree() {
        return this.steps.get("readTitleDegree").tasklet(this.titleDegreeReader()).build();
    }

    /**
     * Lines to JSON step.
     * 
     * @return
     */
    @Bean
    protected Step linesToJson() {
        return this.steps.get("linesToJson").tasklet(this.linesToJSONProcessor()).build();
    }

    /**
     * Complete academic institution step.
     * 
     * @return
     */
    @Bean
    protected Step completeAcademicInstitution() {
        return this.steps.get("completeAcademicInstitution").tasklet(this.academicInstitutionJSONProcessor()).build();
    }

    /**
     * Complete researcher step.
     * 
     * @return
     */
    @Bean
    protected Step completeResearcher() {
        return this.steps.get("completeResearcher").tasklet(this.researcherJSONProcessor()).build();
    }

    /**
     * Complete title - degree step.
     * 
     * @return
     */
    @Bean
    protected Step completeTitleDegree() {
        return this.steps.get("completeTitleDegree").tasklet(this.titleDegreeJSONProcessor()).build();
    }

    /**
     * Write kafka kstep.
     * 
     * @return
     */
    @Bean
    protected Step writeKafka() {
        return this.steps.get("writeKafka").tasklet(this.kafkaWriter()).build();
    }

    /**
     * Academic institution job.
     * 
     * @param jobExecutionListener
     * @return
     */
    @Bean
    public Job academicInstitutionJob(final JobExecutionListener jobExecutionListener) {
        // @formatter:off

        return this.jobs.get("academicInstitutionJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .start(this.readAcademicInstitution())
                .next(this.linesToJson())
                .next(this.completeAcademicInstitution())
                .next(this.writeKafka())
                .build();

        // @formatter:on
    }

    /**
     * Researcher job.
     * 
     * @param jobExecutionListener
     * @return
     */
    @Bean
    public Job researcherJob(final JobExecutionListener jobExecutionListener) {
        // @formatter:off

        return this.jobs.get("researcherJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .start(this.readResearcher())
                .next(this.linesToJson())
                .next(this.completeResearcher())
                .next(this.writeKafka())
                .build();

        // @formatter:on
    }

    /**
     * Title - Degree job.
     * 
     * @param jobExecutionListener
     * @return
     */
    @Bean
    public Job titleDegreeJob(final JobExecutionListener jobExecutionListener) {
        // @formatter:off

        return this.jobs.get("titleDegreeJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .start(this.readTitleDegree())
                .next(this.linesToJson())
                .next(this.completeTitleDegree())
                .next(this.writeKafka())
                .build();

        // @formatter:on
    }
}

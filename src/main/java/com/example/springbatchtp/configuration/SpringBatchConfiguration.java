package com.example.springbatchtp.configuration;

import com.example.springbatchtp.dto.TransactionRequestDto;
import com.example.springbatchtp.dto.TransactionResponseDto;
import com.example.springbatchtp.entities.Compte;
import com.example.springbatchtp.entities.Transaction;
import com.example.springbatchtp.repositories.CompteRepository;
import com.example.springbatchtp.repositories.TransactionRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemReader<TransactionRequestDto> transactionResponseDtoItemReader;
    @Autowired
    private ItemWriter<TransactionResponseDto> transactionResponseDtoItemWriter;
    @Autowired
    private ItemProcessor<TransactionRequestDto,TransactionResponseDto> transactionResponseDtoItemProcessor;

    @Bean
    public Job bankJob(){
        Step step1 = stepBuilderFactory.get("step-load-data")
                .<TransactionRequestDto,TransactionResponseDto>chunk(100)
                .reader(transactionResponseDtoItemReader)
                .processor(transactionResponseDtoItemProcessor)
                .writer(transactionResponseDtoItemWriter)
                .build();

        //je vais retourner le Joob
        return jobBuilderFactory.get("bank-data-loader-job")
                .start(step1)
                .build();

    }
    //il faut definir l'objet ItemReader
    @Bean
    public FlatFileItemReader<TransactionRequestDto> flatFileItemReader(@Value("${inputFile}") Resource resource){
      FlatFileItemReader<TransactionRequestDto> fileItemReader = new FlatFileItemReader<>();
      fileItemReader.setName("FFIR1");
      //pour skiper ligne 1 du fichier csv
      fileItemReader.setLinesToSkip(1);
      fileItemReader.setResource(resource);
      fileItemReader.setLineMapper(lineMapper());
      return fileItemReader;

    }

    @Bean
    public LineMapper<TransactionRequestDto> lineMapper()
    {
        DefaultLineMapper<TransactionRequestDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("idTransaction","idCompte","montant","dateTransaction");
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(TransactionRequestDto.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }
    @Bean
    public ItemProcessor<TransactionRequestDto,TransactionResponseDto> itemProcessor(){
        return new ItemProcessor<TransactionRequestDto, TransactionResponseDto>() {
            private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            @Override
            public TransactionResponseDto process(TransactionRequestDto transactionRequestDto) throws Exception {
                TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
                transactionResponseDto.setDateTransaction(dateFormat.parse(transactionRequestDto.getDateTransaction()));
                transactionResponseDto.setIdTransaction(transactionRequestDto.getIdTransaction());
                transactionResponseDto.setIdCompte(transactionRequestDto.getIdCompte());
                transactionResponseDto.setMontant(transactionRequestDto.getMontant());

                return transactionResponseDto;
            }
        };
    }
    @Bean
    public ItemWriter<TransactionResponseDto> itemWriter(){
        return new ItemWriter<TransactionResponseDto>() {
            @Autowired
            private TransactionRepository transactionRepository;
            @Autowired
            private CompteRepository compteRepository;
            @Override
            public void write(List<? extends TransactionResponseDto> list) throws Exception {
                for(int i=0;i<list.size();i++)
                {  Transaction transaction = new Transaction();
                    //fiha prob
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(list.get(i).getDateTransaction());
                    //get the day
                    if(calendar.get(Calendar.DAY_OF_MONTH)<25)
                    {
                        // calendar.add(Calendar.MONTH, 1);
                         calendar.add(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DAY_OF_MONTH)+1);


                    }else
                    {    calendar.add(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DAY_OF_MONTH)+1);
                        calendar.add(Calendar.MONTH, 1);
                        //if(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)==31)
                    }
                    Date dateDebit = calendar.getTime();
                    dateDebit.setHours(8);
                    dateDebit.setMinutes(30);
                    transaction.setDateDebit(dateDebit);
                    transaction.setIdTransaction(list.get(i).getIdTransaction());
                    transaction.setDateTransaction(list.get(i).getDateTransaction());
                    transaction.setMontant(list.get(i).getMontant());

                    transactionRepository.save(transaction);
                   //je dois avoir des comptes dans ma bd pour faire cette partie
                   Compte compte = new Compte();
                    //Compte compte =compteRepository.getById(list.get(i).getIdCompte());
                    compte.setIdCompte(list.get(i).getIdCompte());
                   compte.setSolde(/*compte.getSolde()-*/list.get(i).getMontant());

                   compteRepository.save(compte);


                }
            }
        };
    }


}

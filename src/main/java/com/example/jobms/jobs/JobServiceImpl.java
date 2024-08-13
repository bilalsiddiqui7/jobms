package com.example.jobms.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.qos.logback.core.CoreConstants;
import com.example.jobms.jobs.DTOs.JobDto;
import com.example.jobms.jobs.FeignClients.CompanyClient;
import com.example.jobms.jobs.FeignClients.ReviewClient;
import com.example.jobms.jobs.external.Company;
import com.example.jobms.jobs.external.Review;
import com.example.jobms.jobs.mapper.JobCompanyToDtoMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JobServiceImpl implements JobService {
    private JobRepository jobRepository;

    int retryAttempt=0;

    @Autowired
    RestTemplate restTemplate;

    CompanyClient companyClient;

    ReviewClient reviewClient;

    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, ReviewClient reviewClient) {
        super();
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
    public Job createJob(Job job) {
        Job savedJob = (Job) jobRepository.save(job);
        return savedJob;
    }

    @Override
    @CircuitBreaker(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
//    @Retry(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
//    @RateLimiter(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDto> getAllJobs() {
        System.out.println("retry attempts "+ ++retryAttempt);
        // TODO Auto-generated method stub
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().map(this::convertJobToJobWithCompanyDto).collect(Collectors.toList());
    }

    public List<String> companyBreakerFallback(Exception e){
        List<String> list=new ArrayList<>();
        list.add("Dummy data for fallback");
        return list;
    }

    public JobDto convertJobToJobWithCompanyDto(Job job) {
//		RestTemplate restTemplate = new RestTemplate();

//        Company company = restTemplate.getForObject("http://COMPANYMS:8082/companies/" + job.getCompanyId(), Company.class);
//        ResponseEntity<List<Review>> response = restTemplate.exchange("http://REVIEWMS:8083/reviews?companyId=" + job.getCompanyId(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
//        });
//        List<Review> reviews = response.getBody();

        // INSTEAD OF RestTemplate WE CAN USE OpenFeign, GIVEN BELOW
        Company company = companyClient.getCompany(job.getCompanyId());
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
        JobDto jobDto = JobCompanyToDtoMapper.JobCompanyAndReviewToJobDtoMapper(job, company, reviews);
        return jobDto;
    }

    @Override
    public JobDto getJobById(int id) {
        Job job = jobRepository.findById(id).orElse(null);
        JobDto jobDto = null;
        if (job != null) {
            jobDto = convertJobToJobWithCompanyDto(job);
        }
        return jobDto;
    }

    @Override
    public boolean updateJob(int id, Job job) {
        Optional<Job> JobToUpdate = jobRepository.findById(id);
        if (JobToUpdate.isPresent()) {
            Job updatedJob = JobToUpdate.get();
            updatedJob.setTitle(job.getTitle());
            updatedJob.setDescription(job.getDescription());
            updatedJob.setLocation(job.getLocation());
            updatedJob.setMinSalary(job.getMinSalary());
            updatedJob.setMaxSalary(job.getMaxSalary());
            updatedJob.setCompanyId(job.getCompanyId());
            jobRepository.save(updatedJob);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteJob(int id) {
        // We are using try catch because if the id is not found it will throw null
        // pointer exception in that case we will send the response as id not found
        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

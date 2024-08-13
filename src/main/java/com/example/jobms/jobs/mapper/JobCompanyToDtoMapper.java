package com.example.jobms.jobs.mapper;

import com.example.jobms.jobs.DTOs.JobDto;
import com.example.jobms.jobs.Job;
import com.example.jobms.jobs.external.Company;
import com.example.jobms.jobs.external.Review;

import java.util.List;

public class JobCompanyToDtoMapper {
    public static JobDto JobCompanyAndReviewToJobDtoMapper(Job job, Company company, List<Review> reviews){
        JobDto jobDto =new JobDto();
        jobDto.setCompany(company);
        jobDto.setDescription(job.getDescription());
        jobDto.setId(job.getId());
        jobDto.setLocation(job.getLocation());
        jobDto.setMaxSalary(job.getMaxSalary());
        jobDto.setMinSalary(job.getMinSalary());
        jobDto.setTitle(job.getTitle());
        jobDto.setReview(reviews);
        return jobDto;
    }
}

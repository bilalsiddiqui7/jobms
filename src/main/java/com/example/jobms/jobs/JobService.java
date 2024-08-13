package com.example.jobms.jobs;

import com.example.jobms.jobs.DTOs.JobDto;

import java.util.List;

public interface JobService {

	List<JobDto> getAllJobs();

	JobDto getJobById(int id);

	Job createJob(Job job);

	boolean updateJob(int id, Job job);

	boolean deleteJob(int id);

}

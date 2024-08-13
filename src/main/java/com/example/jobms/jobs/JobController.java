package com.example.jobms.jobs;

import java.util.List;

import com.example.jobms.jobs.DTOs.JobDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {

	private JobService jobService;

	public JobController(JobService jobService) {
		super();
		this.jobService = jobService;
	}

	@GetMapping
	public ResponseEntity<List<JobDto>> getAllJobs() {
		List<JobDto> jobsList = jobService.getAllJobs();
		return new ResponseEntity<List<JobDto>>(jobsList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<JobDto> getJobById(@PathVariable int id) {
		JobDto jobDto = jobService.getJobById(id);
		if (jobDto != null)
			return new ResponseEntity<JobDto>(jobDto, HttpStatus.OK);
		else
			return new ResponseEntity(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<Job> addJob(@RequestBody Job job) {
		Job savedJob = jobService.createJob(job);
		return new ResponseEntity<Job>(savedJob, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateJob(@PathVariable int id, @RequestBody Job job) {
		boolean updated = jobService.updateJob(id, job);
		if (updated == true)
			return new ResponseEntity<String>("Job updated", HttpStatus.OK);
		return new ResponseEntity(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteJob(@PathVariable int id) {
		boolean deleted = jobService.deleteJob(id);
		if (deleted == true)
			return new ResponseEntity<String>("Job deleted", HttpStatus.OK);
		return new ResponseEntity<String>("Job id not found", HttpStatus.NOT_FOUND);
	}
}

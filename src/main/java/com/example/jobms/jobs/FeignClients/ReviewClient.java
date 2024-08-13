package com.example.jobms.jobs.FeignClients;

import com.example.jobms.jobs.external.Company;
import com.example.jobms.jobs.external.Review;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="REVIEWMS")
public interface ReviewClient {
    @GetMapping("/reviews")
    List<Review> getReviews(@RequestParam("companyId") long companyId);
}

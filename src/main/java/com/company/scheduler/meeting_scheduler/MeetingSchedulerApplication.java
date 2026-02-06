package com.company.scheduler.meeting_scheduler;

import com.company.scheduler.meeting_scheduler.application.service.GreedySchedulerService;
import com.company.scheduler.meeting_scheduler.infrastructure.repository.MeetingRequestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MeetingSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetingSchedulerApplication.class, args);
		System.out.println("🚀 APLICACIÓN LEVANTADA CORRECTAMENTE");
	}

	@Bean
	CommandLineRunner run(GreedySchedulerService scheduler,
						  MeetingRequestRepository meetingRequestRepository) {

		return args -> {
			System.out.println("👉 Ejecutando GreedySchedulerService");

			// toma una request de la BD (seed)
			meetingRequestRepository.findAll()
					.forEach(request -> {
						scheduler.schedule(request);
					});
		};
	}
}
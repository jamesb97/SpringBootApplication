package com.example.demo;

import lombok.Data;
import lombok.*;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Bean;
import java.util.stream.Stream;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
    @Bean
	ApplicationRunner init(CarRepository repository) {
		return args -> {
			Stream.of("Ferrari", "Jaguar", "Porsche", "Lamborghini", "Bugatti", "AMC Gremlin", "Triumph Stag", "Ford Pinto", "Yugo GV").forEach
					(name -> {
						repository.save(new Car(name));
					});
			repository.findAll().forEach(System.out::println);
		};
	}
}

@Data
@NoArgsConstructor
@Entity
class Car {
	public Car(String name){
		this.name = name;
		this.getName();
	}
	@Id
	@GeneratedValue
	private Long id;

	@NonNull
	private String name;

	public Object getName() {
		 return this.getName();
	}
}

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
interface CarRepository extends JpaRepository<Car, Long> {

}
@RestController
class CoolCarController {
	private CarRepository repository;
	public CoolCarController(CarRepository repository) {
		this.repository = repository;
	}
	@GetMapping("/cool-cars")
	@CrossOrigin(origins="http://localhost:4200")
	public Collection<Car> coolCars() {
		return repository.findAll().stream().filter(this::isCool).collect(Collectors.toList());
	}
	private boolean isCool(Car car) {
		return !car.getName().equals("AMC Gremlin") && !car.getName().equals("Triumph Stag") && !car.getName().equals("Ford Pinto") && !car.getName().equals("Yugo GV");
	}
}
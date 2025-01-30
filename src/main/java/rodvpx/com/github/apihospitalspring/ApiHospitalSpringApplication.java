package rodvpx.com.github.apihospitalspring;

import com.google.cloud.spring.data.firestore.repository.config.EnableReactiveFirestoreRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiHospitalSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiHospitalSpringApplication.class, args);
	}

}

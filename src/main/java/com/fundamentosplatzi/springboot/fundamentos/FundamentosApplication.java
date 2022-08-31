package com.fundamentosplatzi.springboot.fundamentos;

import com.fundamentosplatzi.springboot.fundamentos.bean.MyBean;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentosplatzi.springboot.fundamentos.component.ComponentDependency;
import com.fundamentosplatzi.springboot.fundamentos.entity.User;
import com.fundamentosplatzi.springboot.fundamentos.pojo.UserPojo;
import com.fundamentosplatzi.springboot.fundamentos.repository.UserRepository;
import com.fundamentosplatzi.springboot.fundamentos.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean myBean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	private UserService userService;


	public FundamentosApplication(@Qualifier("componentTwoImpl") ComponentDependency componentDependency,
								  MyBean myBean, MyBeanWithDependency myBeanWithDependency,
								  MyBeanWithProperties myBeanWithProperties, UserPojo userPojo,
								  UserRepository userRepository, UserService userService){
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService= userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args){

		saveUserInDB();
	}

	private void getInformalJPQLFromUser(){
		LOGGER.info("Usuario obtenido con el metodo findByUserEmail" +
				userRepository.findMyUserByEmail("john@domain.com")
						.orElseThrow(()-> new RuntimeException("no se encontro el usuario")));

		userRepository.findAndSort("Test", Sort.by("id").descending())
				.forEach(user -> LOGGER.info("Info de usuario findAndSort" + user));

		userRepository.findByName("John")
				.forEach(user -> LOGGER.info("findByName " + user));

		LOGGER.info("findByEmailAndName"+userRepository.findByEmailAndName("oscar@domain.com","Oscar")
				.orElseThrow(()->new RuntimeException("usuario no encontrado")));

		userRepository.findByNameLike("%o%").forEach(user -> LOGGER.info("findByNameLike"+ user));
		userRepository.findByNameOrEmail("John",null).forEach(user -> LOGGER.info("findByNameOrEmail"+ user));
		userRepository.findByBirthDateBetween(LocalDate.of(2019, 01, 01),
						LocalDate.of(2019, 06, 01))
				.forEach(user -> LOGGER.info("findByBirthDateBetween"+ user));
		userRepository.findByNameLikeOrderByIdDesc("%a%").forEach(user -> LOGGER.info("findByNameLikeOrderByIdDesc"+ user));
		userRepository.findByNameLikeOrderByIdAsc("%a%").forEach(user -> LOGGER.info("findByNameLikeOrderByIdDesc"+ user));
		userRepository.findByNameContainingOrderByIdAsc("a").forEach(user -> LOGGER.info("findByNameLikeOrderByIdDesc"+ user));
	}

	private void saveWithErrorTransactional(){
		User test1 = new User("TestTransactional1", "Test1Transactional1@domain.com", LocalDate.now());
		User test2 = new User("TestTransactional2", "Test2Transactional2@domain.com", LocalDate.now());
		User test3 = new User("TestTransactional3", "Test1Transactional1@domain.com", LocalDate.now());
		User test4 = new User("TestTransactional4", "Test4Transactional4@domain.com", LocalDate.now());

		List<User> users = Arrays.asList(test1,test2,test3,test4);

		try {
			userService.saveTransaccional(users);
		}catch (Exception e){

		}
		userService.getAllUsers()
				.forEach(user -> LOGGER.info("usuario metodo transaccional "+ user));
	}

	private void saveUserInDB(){
		User user1 = new User("John", "john@domain.com", LocalDate.of(2021, 03, 15));
		User user2 = new User("Julie", "julie@domain.com", LocalDate.of(2021, 03, 20));
		User user3 = new User("Daniela", "daniela@domain.com", LocalDate.of(2021, 03, 25));
		User user4 = new User("Oscar", "oscar@domain.com", LocalDate.now());
		User user5 = new User("Test1", "Test1@domain.com", LocalDate.now());
		User user6 = new User("Test2", "Test2@domain.com", LocalDate.now());
		User user7 = new User("Test3", "Test3@domain.com", LocalDate.now());
		User user8 = new User("Test4", "Test4@domain.com", LocalDate.now());
		User user9 = new User("Test5", "Test5@domain.com", LocalDate.now());
		User user10 = new User("Test6", "Test6@domain.com", LocalDate.now());
		User user11 = new User("Test7", "Test7@domain.com", LocalDate.now());
		User user12 = new User("Test8", "Test8@domain.com", LocalDate.now());
		User user13 = new User("Test9", "Test9@domain.com", LocalDate.now());
		List<User> list = Arrays.asList(user1,user2,user3,user4, user5, user6, user7, user8, user9, user10, user11, user12, user13);
		list.stream().forEach(userRepository::save);

	}

	private void delete(){
		userRepository.deleteAll();
	}

	private void ejemplosAnteriores() {
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail()+"-"+userPojo.getPassword());
		LOGGER.error("Esto es un error");
	}
}

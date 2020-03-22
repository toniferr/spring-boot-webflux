package com.toni.springboot.webflux.app;

import java.time.LocalDate;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.toni.springboot.webflux.app.models.dao.ProductoDao;
import com.toni.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner{
	
	@Autowired
	private ProductoDao dao;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("productos").subscribe();
		Flux.just(
				new Producto("Panasonic TV", 200.20),
				new Producto("Sony TV", 300.39),
				new Producto("IPhone X", 1000.66),
				new Producto("Home Cinema Philips", 100.99),
				new Producto("Mac 5k", 2000.32),
				new Producto("Sony Xperia X", 800.99)
		)
		.flatMap(producto -> {
				producto.setCreateAt(LocalDate.now());
				return dao.save(producto);
		})
		//.map(producto -> dao.save(producto))
		.subscribe(producto -> log.info("Insert: "+producto.getId()));
	}

}

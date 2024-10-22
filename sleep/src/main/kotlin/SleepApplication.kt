package com.noom.interview.fullstack.sleep

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = ["com.noom.interview.fullstack"])
@EntityScan(basePackages = ["com.noom.interview.fullstack.models"])
@EnableJpaRepositories(basePackages = ["com.noom.interview.fullstack.repositories"])
class SleepApplication {
	companion object {
		const val UNIT_TEST_PROFILE = "unittest"
	}
}

fun main(args: Array<String>) {
	runApplication<SleepApplication>(*args)
}

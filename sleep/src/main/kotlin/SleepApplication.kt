package com.noom.interview.fullstack.sleep

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.noom.interview.fullstack"])
class SleepApplication {
	companion object {
		const val UNIT_TEST_PROFILE = "unittest"
	}
}

fun main(args: Array<String>) {
	runApplication<SleepApplication>(*args)
}

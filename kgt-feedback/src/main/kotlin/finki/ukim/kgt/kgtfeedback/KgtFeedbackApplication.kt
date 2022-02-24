package finki.ukim.kgt.kgtfeedback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class KgtFeedbackApplication

fun main(args: Array<String>) {
    runApplication<KgtFeedbackApplication>(*args)
}

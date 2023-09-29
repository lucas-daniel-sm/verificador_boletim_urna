package dev.lucasmendes.boletim_urna

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import javax.sql.DataSource


@Configuration
class WebSecurityConfig {
    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .csrf { it.disable() }
            .httpBasic(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun userDetailsService(): InMemoryUserDetailsManager {
        @Suppress("DEPRECATION")
        val user: UserDetails = User.withDefaultPasswordEncoder()
            .username("user")
            .password("3ced42e0-a2d2-4d0e-ab43-86737ee69744")
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }
}

@Configuration
class DataConfig(private var env: Environment) {
    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.sqlite.JDBC")
        dataSource.url = "jdbc:sqlite:memory:myDb?cache=shared"
        dataSource.username = "sa"
        dataSource.password = "sa"
        return dataSource
    }
}

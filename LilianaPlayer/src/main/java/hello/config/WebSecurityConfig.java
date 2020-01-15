//package hello.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//@EnableWebSecurity//(debug = true)
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//	    auth.inMemoryAuthentication().passwordEncoder(passwordEncoder())
//            .withUser("admin").password("$2a$10$UPaj80EC/CZg6.m6o3HrLeHQuk3su2VTrO1p.fnX0AhDFgCNHo7sy")
//            .roles("ADMIN").and()   //pass = 1111
//            .withUser("anhtu").password("$2a$10$UPaj80EC/CZg6.m6o3HrLeHQuk3su2VTrO1p.fnX0AhDFgCNHo7sy")
//            .roles("USER");
//	}
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        return bCryptPasswordEncoder;
//    }
//
//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//	    http.authorizeRequests().antMatchers("/api/**").permitAll();
//	}
//}
package ru.spring.security.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.spring.security.services.UserService;




@EnableWebSecurity//аннотацию конфигуратион ставить не обязательно(Достанется нам по цепочке)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {//в этой части указываем доступ пользователей, роли
        http.authorizeRequests()
                .antMatchers("/authenticated/**").authenticated() //на этот эндпоинт проходят только авторизованные пользователи
                .antMatchers("/only_for_admins/**").hasRole("ADMIN")
                .antMatchers("/read_profile/**").hasAuthority("READ_PROFILE")
                // .antMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERADMIN")//этот энд поинт помимо авторизации требует еще и роли
//                .antMatchers("/profile/**").authenticated()//попадают только авторизованные пользователи
                .and()
//                .httpBasic()//перенаправляет на стандартное окно логина и пароля
                .formLogin() //стандартная форма ввода логина и пароля
//                .loginProcessingUrl("/hellologin") // указываю страницу на которой обрабатывается ввод логина и пароля (Если стандартная форма меня не интересует)
                .and()
                .logout().logoutSuccessUrl("/"); //после выхода из авторизации(logout) направляет на страницу которую мы указываем(путь)

    }

    @Bean
    public PasswordEncoder passwordEncoder() {//преобразователь паролей
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {//сверяет данные
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());//используем пассворд энкодер созданный раннее
        authenticationProvider.setUserDetailsService(userService); //предоставляет нашему провайдеру каких то юзеров
        return authenticationProvider;

    }
}

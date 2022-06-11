package ru.spring.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spring.security.entities.Role;
import ru.spring.security.entities.User;
import ru.spring.security.repositories.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {//задача данного сервиса по имени пользователя предоставить самого юзера

    private UserRepository userRepository;

    @Autowired
    public void setUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {//делаем обертку над юзер репозиторий(чтобы напрямую не обращаться)
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {//перегоняем наших юзеров в юзеров которых понимает SpringSecurity
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
            mapRolesToAuthorities(user.getRoles())    );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){//метод который из коллекции ролей(role) получает коллекцию прав доступа(Authorities)
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}

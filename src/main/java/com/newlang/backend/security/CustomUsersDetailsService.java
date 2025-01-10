package com.newlang.backend.security;

import com.newlang.backend.models.Role;
import com.newlang.backend.models.User;
import com.newlang.backend.repository.IUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomUsersDetailsService implements UserDetailsService {
    private final IUsuariosRepository usuariosRepository;

    @Autowired
    public CustomUsersDetailsService(IUsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    public Collection<GrantedAuthority> mapToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNameRole())).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = usuariosRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario no Encontrado"));
        return new CustomUserDetails(user);
    }

}

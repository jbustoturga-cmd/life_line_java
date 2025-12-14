package com.example.lifeline.security;

import com.example.lifeline.model.Usuario;
import com.example.lifeline.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUserName(username);

        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        Usuario usuario = usuarioOpt.get();

        // ============================================
        // 🔧 Agregado: garantizar que el rol nunca sea null
        // ============================================
        String rol = usuario.getRol();
        if (rol == null || rol.isBlank()) {
            rol = "USER";  // Rol por defecto
        }
        // ============================================

        return User.builder()
                .username(usuario.getUserName())
                .password(usuario.getPassword())
                .roles(rol)          // Mantengo tu lógica exacta
                .build();
    }
}


package com.example.smartcollector.controller;

import com.example.smartcollector.dto.LoginRequest;
import com.example.smartcollector.dto.LoginResponse;
import com.example.smartcollector.dto.RegisterRequest;
import com.example.smartcollector.model.Usuario;
import com.example.smartcollector.repository.UsuarioRepository;
import com.example.smartcollector.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(
                request.email(), request.senha()
        );
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registro(@Valid @RequestBody RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.email()) != null)
            return ResponseEntity.badRequest().body("Email já cadastrado");

        String senhaCriptografada = passwordEncoder.encode(request.senha());
        Usuario usuario = new Usuario(
                request.nome(),
                request.email(),
                senhaCriptografada,
                request.funcao()
        );
        usuarioRepository.save(usuario);
        return ResponseEntity.ok().build();
    }
}
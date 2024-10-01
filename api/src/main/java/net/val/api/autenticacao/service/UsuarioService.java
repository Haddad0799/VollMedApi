package net.val.api.autenticacao.service;

import net.val.api.autenticacao.entity.Usuario;
import net.val.api.autenticacao.dtos.usuarioDto.DadosCadastroUsuario;
import net.val.api.infra.exceptions.usuarioException.LoginJaUtilizadoException;
import net.val.api.autenticacao.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void cadastrarUsuario (DadosCadastroUsuario dadosCadastroUsuario) {

        if(usuarioRepository.findByLogin(dadosCadastroUsuario.login()).isPresent()) {
            throw new LoginJaUtilizadoException(dadosCadastroUsuario.login());
        }

        String senhaCriptografada = passwordEncoder.encode(dadosCadastroUsuario.senha());

        usuarioRepository.save(new Usuario(dadosCadastroUsuario.login(), senhaCriptografada));


    }
}

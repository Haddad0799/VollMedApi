package net.val.api.service;

import net.val.api.domain.Usuario;
import net.val.api.dtos.autenticacaoDto.DadosAutenticacao;
import net.val.api.dtos.tokenDto.DadosTokenJwt;
import net.val.api.infra.exceptions.autenticacaoExceptions.UsuarioNaoEncontradoException;
import net.val.api.repositorys.UsuarioRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final AuthenticationManager manager;

    private final TokenService tokenService;


    public AutenticacaoService(UsuarioRepository usuarioRepository, @Lazy AuthenticationManager manager, @Lazy TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsuarioNaoEncontradoException {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

    public DadosTokenJwt efetuarLogin(DadosAutenticacao autenticacao) {
        var token = new UsernamePasswordAuthenticationToken(autenticacao.login(), autenticacao.senha());
        var autenticado = manager.authenticate(token);

        return new DadosTokenJwt(tokenService.gerarToken((Usuario) autenticado.getPrincipal()));
    }
}


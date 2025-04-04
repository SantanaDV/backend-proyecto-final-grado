package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.UsuarioRepository;
import org.backend.backendfacilgim.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario getUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId).orElseThrow(() -> new CustomException("No se ha encontrado el ejercicio por id: " + usuarioId));
    }

    @Override
    public Usuario getUsuarioByUsername(String nombreUsuario) {
        return usuarioRepository.findByUsername(nombreUsuario).orElseThrow(() -> new CustomException("No se ha encontrado el ejercicio por el nonbre: " + nombreUsuario));
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Integer usuarioId, Usuario datosNuevos) {
        return actualizarUsuarioAux(getUsuario(usuarioId), datosNuevos);
    }

    @Override
    public Usuario actualizarUsuarioPorUsuario(String username, Usuario datosNuevos) {
        return actualizarUsuarioAux(getUsuarioByUsername(username), datosNuevos);
    }

    @Override
    public void eliminarUsuario(Integer id) {
        usuarioRepository.delete(getUsuario(id));
    }

    @Override
    public void eliminarUsuarioPorUsername(String username) {
        usuarioRepository.delete(usuarioRepository.findByUsername(username).orElseThrow(()-> new CustomException("El usuario debe existir para poder ser eliminado")));
    }

    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    private Usuario actualizarUsuarioAux(Usuario usuarioEncontrado, Usuario usuarioDatosNuevos) {
        usuarioEncontrado.setUsername(usuarioDatosNuevos.getUsername());
        usuarioEncontrado.setPassword(usuarioDatosNuevos.getPassword());
        usuarioEncontrado.setCorreo(usuarioDatosNuevos.getCorreo());
        usuarioEncontrado.setNombre(usuarioDatosNuevos.getNombre());
        usuarioEncontrado.setApellido(usuarioDatosNuevos.getApellido());
        usuarioEncontrado.setDireccion(usuarioDatosNuevos.getDireccion());
        usuarioEncontrado.setEnabled(usuarioDatosNuevos.isEnabled());
        usuarioEncontrado.setAdmin(usuarioDatosNuevos.isAdmin());
        return usuarioEncontrado;
    }
}

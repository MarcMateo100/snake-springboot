package com.snakegame.snakeapp.controller;

import com.snakegame.snakeapp.model.Usuario;
import com.snakegame.snakeapp.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping("/")
    public String mostrarFormulario() {
        return "login"; // Muestra login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Usuario usuario = usuarioRepo.findByUsernameAndPassword(username, password);
        if (usuario != null) {
            session.setAttribute("usuario", usuario); // guardamos el usuario en sesión
            model.addAttribute("usuario", usuario);
            return "inicio";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/inicio")
    public String mostrarInicio(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "inicio";
        } else {
            return "login"; // si no hay sesión, redirigir a login
        }
    }

    @GetMapping("/juego")
    public String mostrarJuego(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "juego";
        } else {
            return "login"; // si no hay sesión, redirigir a login
        }
    }

    @GetMapping("/juego2")
    public String mostrarJuego2(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "juego2";
        } else {
            return "login"; // si no hay sesión, redirigir a login
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @PostMapping("/guardarPuntos")
    @ResponseBody
    public String guardarPuntos(@RequestParam String username,
                                @RequestParam int puntuacion) {

        Usuario usuario = usuarioRepo.findByUsername(username);

        if (usuario != null) {
            int puntuacionActual = usuario.getPuntuacion();
            if (puntuacion > puntuacionActual) {
                usuario.setPuntuacion(puntuacion);
                usuarioRepo.save(usuario);
                return "✅ Puntaje actualizado";
            } else {
                return "ℹ️ El nuevo puntaje no supera el récord actual (" + puntuacionActual + ")";
            }
        }

        return "⚠️ Usuario no encontrado";
    }


    @GetMapping("/ranking")
    public String mostrarRanking(Model model) {
        List<Usuario> lista = usuarioRepo.findAll(Sort.by(Sort.Direction.DESC, "puntuacion"));
        model.addAttribute("usuarios", lista);
        return "ranking";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }
    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String username,
                                   @RequestParam String password,
                                   Model model,
                                   HttpSession session) {

        // Validar si el nombre ya existe
        Usuario existente = usuarioRepo.findByUsername(username);
        if (existente != null) {
            model.addAttribute("error", "⚠️ Ese nombre ya está en uso. Por favor, elige otro.");
            return "registro";
        }

        // Crear nuevo usuario
        Usuario nuevo = new Usuario();
        nuevo.setUsername(username);
        nuevo.setPassword(password);
        nuevo.setPuntuacion(0);
        usuarioRepo.save(nuevo);

        // Guardar en sesión y enviar al juego directamente
        session.setAttribute("usuario", nuevo);
        model.addAttribute("usuario", nuevo);
        return "juego";
    }



}

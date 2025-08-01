package com.snakegame.snakeapp.controller;

import com.snakegame.snakeapp.model.Usuario;
import com.snakegame.snakeapp.repository.UsuarioRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public String home(@CookieValue(value = "usuario", defaultValue = "") String username) {
        if (!username.isEmpty()) {
            return "redirect:/inicio";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        return "login"; // nombre de tu plantilla login.html
    }


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        HttpServletResponse response,
                        Model model) {
        Usuario usuario = usuarioRepo.findByUsernameAndPassword(username, password);
        if (usuario != null) {
            session.setAttribute("usuario", usuario); // sesión activa
            model.addAttribute("usuario", usuario);

            // 🧠 Crear cookie persistente
            Cookie cookie = new Cookie("usuario", usuario.getUsername());
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 días
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/inicio";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpSession session) {
        session.invalidate(); // cerrar sesión

        Cookie cookie = new Cookie("usuario", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/login";
    }

    @GetMapping("/inicio")
    public String inicio(@CookieValue(value = "usuario", defaultValue = "") String username,
                         HttpSession session,
                         Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null && !username.isEmpty()) {
            usuario = usuarioRepo.findByUsername(username);
            session.setAttribute("usuario", usuario);
        }

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "inicio";
        } else {
            return "redirect:/login";
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

    @GetMapping("/juego3")
    public String mostrarJuego3(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "juego3";
        } else {
            return "login"; // si no hay sesión, redirigir a login
        }
    }

    @GetMapping("/juego4")
    public String mostrarJuego4(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "juego4";
        } else {
            return "login"; // si no hay sesión, redirigir a login
        }
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

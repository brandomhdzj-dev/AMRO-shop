package com.brandom.mipaginaweb.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.brandom.mipaginaweb.model.CarouselSlide;
import com.brandom.mipaginaweb.model.Producto;
import com.brandom.mipaginaweb.model.Settings;
import com.brandom.mipaginaweb.repository.CarouselSlideRepository;
import com.brandom.mipaginaweb.repository.ProductoRepository;
import com.brandom.mipaginaweb.repository.SettingsRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CarouselSlideRepository carouselSlideRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private Cloudinary cloudinary;

    private static final String PASSWORD_ADMIN = "Angela1511";

    // =================== VISTA PUBLICA (CLIENTE) ===================

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("logo", getLogoUrl());
        return "home";
    }

    @GetMapping("/tienda")
    public String tienda(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "index";
    }

    @GetMapping("/tienda/categoria/{nombre}")
    public String tiendaPorCategoria(@PathVariable String nombre, Model model) {
        model.addAttribute("productos", productoRepository.findByCategoria(nombre));
        model.addAttribute("categoriaActual", nombre);
        return "index";
    }

    @GetMapping("/tienda/genero/{genero}")
    public String tiendaPorGenero(@PathVariable String genero, Model model) {
        model.addAttribute("productos", productoRepository.findByGenero(genero));
        model.addAttribute("generoActual", genero);
        return "index";
    }

    @GetMapping("/producto/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(null);
        model.addAttribute("producto", producto);
        return "detalle";
    }

    // =================== LOGIN ===================

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // =================== LOGIN ADMIN ===================

    @GetMapping("/admin")
    public String loginAdmin() {
        return "admin-login";
    }

    @PostMapping("/admin/verificar")
    public String verificar(@RequestParam String password, HttpSession session) {
        if (password.equals(PASSWORD_ADMIN)) {
            session.setAttribute("admin", true);
            return "redirect:/admin/dashboard";
        }
        return "redirect:/admin";
    }

    // =================== PANEL DE ADMIN ===================

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        model.addAttribute("productos", productoRepository.findAll());
        return "admin";
    }

    @GetMapping("/admin/productos/nuevo")
    public String formularioNuevo(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        model.addAttribute("producto", new Producto());
        return "formulario";
    }

    @PostMapping("/admin/productos/guardar")
    public String guardar(@ModelAttribute Producto producto,
                          @RequestParam("archivoImagen") MultipartFile archivo,
                          @RequestParam(value = "tallasSeleccionadas", required = false) String[] tallasSeleccionadas,
                          HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }

        if (tallasSeleccionadas != null) {
            String tallas = String.join(",", tallasSeleccionadas);
            producto.setTallas(tallas);
        }

        if (!archivo.isEmpty()) {
            String url = subirACloudinary(archivo, "amro-shop/productos");
            if (url != null) {
                producto.setImagen(url);
            }
        }

        productoRepository.save(producto);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/productos/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        Producto producto = productoRepository.findById(id).orElse(null);
        model.addAttribute("producto", producto);
        if (producto != null && producto.getTallas() != null) {
            List<String> tallasSeleccionadas = Arrays.asList(producto.getTallas().split(","));
            model.addAttribute("tallasSeleccionadas", tallasSeleccionadas);
        }
        return "formulario";
    }

    @GetMapping("/admin/productos/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }

        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null && producto.getImagen() != null) {
            eliminarDeCloudinary(producto.getImagen());
        }

        productoRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // =================== LOGO ===================

    @GetMapping("/admin/logo")
    public String logoAdmin(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        model.addAttribute("logo", getLogoUrl());
        return "logo";
    }

    @PostMapping("/admin/logo/subir")
    public String subirLogo(@RequestParam("archivoLogo") MultipartFile archivo, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }

        if (!archivo.isEmpty()) {
            String url = subirACloudinary(archivo, "amro-shop/logo");
            if (url != null) {
                Settings logoSetting = settingsRepository.findById("logoUrl").orElse(new Settings("logoUrl", null));
                String oldUrl = logoSetting.getValue();
                logoSetting.setValue(url);
                settingsRepository.save(logoSetting);

                if (oldUrl != null) {
                    eliminarDeCloudinary(oldUrl);
                }
            }
        }
        return "redirect:/admin/logo";
    }

    private String getLogoUrl() {
        return settingsRepository.findById("logoUrl").map(Settings::getValue).orElse(null);
    }

    // =================== CARRUSEL ADMIN ===================

    @GetMapping("/admin/carrusel")
    public String carruselAdmin(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        model.addAttribute("slides", carouselSlideRepository.findAllByOrderByOrdenAsc());
        return "admin-carrusel";
    }

    @GetMapping("/admin/carrusel/nuevo")
    public String carruselNuevo(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        model.addAttribute("slide", new CarouselSlide());
        return "admin-carrusel-form";
    }

    @PostMapping("/admin/carrusel/guardar")
    public String carruselGuardar(@ModelAttribute CarouselSlide slide,
                                  @RequestParam("archivoImagen") MultipartFile archivo,
                                  HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }

        if (!archivo.isEmpty()) {
            String url = subirACloudinary(archivo, "amro-shop/carrusel");
            if (url != null) {
                slide.setImagen(url);
            }
        }

        if (slide.getOrden() == null) {
            Integer maxOrden = carouselSlideRepository.findMaxOrden();
            slide.setOrden(maxOrden != null ? maxOrden + 1 : 1);
        }

        carouselSlideRepository.save(slide);
        return "redirect:/admin/carrusel";
    }

    @GetMapping("/admin/carrusel/editar/{id}")
    public String carruselEditar(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        CarouselSlide slide = carouselSlideRepository.findById(id).orElse(null);
        model.addAttribute("slide", slide);
        return "admin-carrusel-form";
    }

    @GetMapping("/admin/carrusel/eliminar/{id}")
    public String carruselEliminar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        CarouselSlide slide = carouselSlideRepository.findById(id).orElse(null);
        if (slide != null && slide.getImagen() != null) {
            eliminarDeCloudinary(slide.getImagen());
        }
        carouselSlideRepository.deleteById(id);
        return "redirect:/admin/carrusel";
    }

    @GetMapping("/admin/carrusel/toggle/{id}")
    public String carruselToggle(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        CarouselSlide slide = carouselSlideRepository.findById(id).orElse(null);
        if (slide != null) {
            slide.setActivo(!slide.getActivo());
            carouselSlideRepository.save(slide);
        }
        return "redirect:/admin/carrusel";
    }

    // =================== HELPERS CLOUDINARY ===================

    @SuppressWarnings("unchecked")
    private String subirACloudinary(MultipartFile archivo, String carpeta) {
        try {
            Map<String, Object> result = cloudinary.uploader().upload(
                archivo.getBytes(),
                ObjectUtils.asMap("folder", carpeta, "resource_type", "image")
            );
            return (String) result.get("secure_url");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void eliminarDeCloudinary(String url) {
        if (url == null || url.isEmpty()) return;
        try {
            String publicId = extraerPublicId(url);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extraerPublicId(String url) {
        Pattern pattern = Pattern.compile("upload/v\\d+/(.+?)\\.(jpg|jpeg|png|gif|webp|svg|avif)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        Pattern pattern2 = Pattern.compile("upload/(.+?)\\.(jpg|jpeg|png|gif|webp|svg|avif)");
        Matcher matcher2 = pattern2.matcher(url);
        if (matcher2.find()) {
            return matcher2.group(1);
        }
        return null;
    }
}

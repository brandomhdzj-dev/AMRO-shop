package com.brandom.mipaginaweb.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.brandom.mipaginaweb.model.CarritoItem;
import com.brandom.mipaginaweb.model.Pedido;
import com.brandom.mipaginaweb.model.Producto;
import com.brandom.mipaginaweb.repository.PedidoRepository;
import com.brandom.mipaginaweb.repository.ProductoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CarritoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam Long productoId,
                                   @RequestParam String talla,
                                   HttpSession session) {
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto == null) {
            return "redirect:/tienda";
        }

        for (CarritoItem item : carrito) {
            if (item.getProductoId().equals(productoId) && item.getTalla().equals(talla)) {
                item.setCantidad(item.getCantidad() + 1);
                session.setAttribute("carrito", carrito);
                return "redirect:/carrito";
            }
        }

        CarritoItem item = new CarritoItem(
            producto.getId(),
            producto.getNombre(),
            producto.getPrecio(),
            producto.getImagen(),
            talla,
            1
        );
        carrito.add(item);
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }

    @GetMapping("/carrito")
    public String verCarrito(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        model.addAttribute("carrito", carrito);

        double total = 0;
        for (CarritoItem item : carrito) {
            total += item.getSubtotal();
        }
        model.addAttribute("total", total);
        return "carrito";
    }

    @GetMapping("/carrito/eliminar/{index}")
    public String eliminarDelCarrito(@PathVariable int index, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito != null && index >= 0 && index < carrito.size()) {
            carrito.remove(index);
            session.setAttribute("carrito", carrito);
        }
        return "redirect:/carrito";
    }

    @GetMapping("/carrito/actualizar/{index}/{cantidad}")
    public String actualizarCantidad(@PathVariable int index, @PathVariable int cantidad, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito != null && index >= 0 && index < carrito.size()) {
            if (cantidad <= 0) {
                carrito.remove(index);
            } else {
                carrito.get(index).setCantidad(cantidad);
            }
            session.setAttribute("carrito", carrito);
        }
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/comprar")
    public String comprar(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/carrito";
        }

        String fecha = LocalDate.now().toString();

        for (CarritoItem item : carrito) {
            Producto producto = productoRepository.findById(item.getProductoId()).orElse(null);
            if (producto != null) {
                Pedido pedido = new Pedido(
                    item.getNombre(),
                    item.getPrecio(),
                    producto.getCosto(),
                    item.getTalla(),
                    item.getCantidad(),
                    "pendiente",
                    fecha
                );
                pedidoRepository.save(pedido);
            }
        }

        session.removeAttribute("carrito");
        return "redirect:/carrito/exito";
    }

    @GetMapping("/carrito/exito")
    public String exito() {
        return "compra-exitosa";
    }
}

package com.brandom.mipaginaweb.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.brandom.mipaginaweb.model.Pedido;
import com.brandom.mipaginaweb.model.Producto;
import com.brandom.mipaginaweb.repository.PedidoRepository;
import com.brandom.mipaginaweb.repository.ProductoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/admin/pedidos")
    public String pedidos(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        List<Pedido> pendientes = pedidoRepository.findByEstado("pendiente");
        model.addAttribute("pedidos", pendientes);
        return "admin-pedidos";
    }

    @GetMapping("/admin/pedidos/vendidos")
    public String vendidos(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        List<Pedido> vendidos = pedidoRepository.findByEstado("vendido");
        model.addAttribute("pedidos", vendidos);

        double totalGanado = 0;
        double totalInvertido = 0;
        for (Pedido p : vendidos) {
            totalGanado += p.getTotal();
            totalInvertido += p.getCosto() * p.getCantidad();
        }
        model.addAttribute("totalGanado", totalGanado);
        model.addAttribute("totalInvertido", totalInvertido);
        model.addAttribute("gananciaTotal", totalGanado - totalInvertido);

        return "admin-ventas";
    }

    @GetMapping("/admin/pedidos/vender/{id}")
    public String marcarVendido(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido != null) {
            pedido.setEstado("vendido");
            pedidoRepository.save(pedido);
        }
        return "redirect:/admin/pedidos";
    }

    @GetMapping("/admin/productos/vender/{id}")
    public String venderProducto(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null && producto.getStock() > 0) {
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Pedido pedido = new Pedido(
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCosto(),
                producto.getTallas() != null ? producto.getTallas() : "Unitalla",
                1,
                "vendido",
                fecha
            );
            pedidoRepository.save(pedido);

            producto.setStock(producto.getStock() - 1);
            productoRepository.save(producto);
        }
        return "redirect:/admin/pedidos/vendidos";
    }

    @GetMapping("/admin/pedidos/cancelar/{id}")
    public String cancelar(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido != null) {
            pedido.setEstado("cancelado");
            pedidoRepository.save(pedido);
        }
        return "redirect:/admin/pedidos";
    }
}

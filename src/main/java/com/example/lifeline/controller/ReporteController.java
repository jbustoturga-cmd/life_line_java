package com.example.lifeline.controller;

import com.example.lifeline.utils.PdfGenerator;
import com.example.lifeline.model.Cliente;
import com.example.lifeline.service.ClienteService;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

/**
 * Controlador encargado de manejar las vistas y reportes relacionados con los
 * clientes.
 * Proporciona endpoints para listar clientes filtrados, listar todos los.
 * clientes
 * y generar un reporte en PDF.
 */
@Controller
public class ReporteController {

    private final ClienteService servicio;
    private final PdfGenerator pdfGenerator;

    /**
     * Constructor del controlador.
     * .
     * @param servicio     servicio que gestiona la lógica de negocio de clientes
     * @param pdfGenerator utilidad para generar reportes en PDF
     */
    public ReporteController(ClienteService servicio, PdfGenerator pdfGenerator) {
        this.servicio = servicio;
        this.pdfGenerator = pdfGenerator;
    }

    /**
     * Endpoint para mostrar la vista de clientes filtrados..
     * Permite filtrar por nombre y rango de fechas.
     *
     * @param nombre nombre del cliente (opcional)
     * @param desde  fecha inicial del filtro (opcional)
     * @param hasta  fecha final del filtro (opcional)
     * @param model  objeto Model para pasar datos a la vista
     * @return nombre de la vista: plantilla "vista-clientes"
     */
    @GetMapping("/vista-clientes")
    public String vistaClientes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            Model model) {
        List<Cliente> clientes = servicio.filtrarClientes(nombre, desde, hasta);
        model.addAttribute("clientes", clientes);
        model.addAttribute("nombre", nombre);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        return "vista-clientes";
    }

    /**
     * Endpoint para listar todos los clientes sin filtros..
     *
     * @param model objeto Model para pasar datos a la vista
     * @return nombre de la vista: plantilla "vista-clientes"
     */
    @GetMapping("/vista-clientes-listarTodos")
    public String vistaClientes(Model model) {
        var clientes = servicio.listarTodos();
        model.addAttribute("clientes", clientes);
        return "vista-clientes";
    }

    /**
     * Endpoint para generar un reporte PDF con filtros aplicados..
     * El archivo se envía directamente en la respuesta HTTP.
     *
     * @param response objeto HttpServletResponse para escribir el PDF
     * @throws Exception en caso de error durante la generación del PDF
     */
    @GetMapping("/reporte-clientes")
    public void generarReporte(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpServletResponse response) throws Exception {

        var clientes = servicio.filtrarClientes(nombre, desde, hasta);
        pdfGenerator.generarPdf("reporte-clientes", clientes, desde, hasta, response);
    }

}
//
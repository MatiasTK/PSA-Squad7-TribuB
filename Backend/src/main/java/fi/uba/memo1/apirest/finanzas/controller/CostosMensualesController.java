package fi.uba.memo1.apirest.finanzas.controller;

import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import fi.uba.memo1.apirest.finanzas.dto.CostosProyectoResponse;
import fi.uba.memo1.apirest.finanzas.dto.CostoRequest;
import fi.uba.memo1.apirest.finanzas.exception.RolNoEncontradoException;
import fi.uba.memo1.apirest.finanzas.service.CostosMensualesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/finanzas")
public class CostosMensualesController {
    private final CostosMensualesService service;

    public CostosMensualesController(CostosMensualesService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todos los costos mensuales")
    @ApiResponse(responseCode = "200", description = "Costos mensuales encontrados")
    @GetMapping("/costos")
    public ResponseEntity<Mono<List<CostosMensualesResponse>>> getCostos() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @Operation(summary = "Obtener un costo mensual por id")
    @ApiResponse(responseCode = "200", description = "Costo mensual encontrado")
    @ApiResponse(responseCode = "404", description = "Costo mensual no encontrado, rol invalido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolNoEncontradoException.class)))
    @GetMapping("/costos/{id}")
    public ResponseEntity<Mono<CostosMensualesResponse>> getCostos(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @Operation(summary = "Cargar un costo mensual")
    @ApiResponse(responseCode = "201", description = "Costo mensual cargado")
    @ApiResponse(responseCode = "404", description = "Error en la carga del costo mensual", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolNoEncontradoException.class)))
    @PostMapping("/cargar-costo")
    public ResponseEntity<Mono<CostosMensualesResponse>> cargarCosto(@RequestBody CostosMensualesRequest costosMensualesRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(costosMensualesRequest));
    }

    @Operation(summary = "Actualizar un costo mensual")
    @ApiResponse(responseCode = "200", description = "Costo mensual actualizado")
    @ApiResponse(responseCode = "400", description = "Error en la actualización del costo mensual, el costo no puede ser negativo",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolNoEncontradoException.class)))
    @PutMapping("/costos/actualizar-costo/{id}")
    public ResponseEntity<Mono<CostosMensualesResponse>> actualizarCosto(@PathVariable Long id, @RequestBody CostoRequest costoRequest){
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, costoRequest));
    }
    
    @Operation(summary = "Dado un año, obtener los costos mes a mes del proyecto")
    @GetMapping("/costos/proyectos/{anio}")
    public ResponseEntity<Mono<List<CostosProyectoResponse>>> getProyectos(@PathVariable String anio){
        return ResponseEntity.status(HttpStatus.OK).body(service.obtenerCostosDeProyectos(anio));
    }
    
}

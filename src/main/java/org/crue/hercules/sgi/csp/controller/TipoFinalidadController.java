package org.crue.hercules.sgi.csp.controller;

import java.util.List;

import javax.validation.Valid;

import org.crue.hercules.sgi.csp.model.TipoFinalidad;
import org.crue.hercules.sgi.csp.service.TipoFinalidadService;
import org.crue.hercules.sgi.framework.data.search.QueryCriteria;
import org.crue.hercules.sgi.framework.web.bind.annotation.RequestPageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * TipoFinalidadController
 */

@RestController
@RequestMapping("/tipofinalidades")
@Slf4j
public class TipoFinalidadController {

  /** TipoFinalidad service */
  private final TipoFinalidadService service;

  public TipoFinalidadController(TipoFinalidadService tipoFinalidadService) {
    log.debug("TipoFinalidadController(TipoFinalidadService tipoFinalidadService) - start");
    this.service = tipoFinalidadService;
    log.debug("TipoFinalidadController(TipoFinalidadService tipoFinalidadService) - end");
  }

  /**
   * Crea nuevo {@link TipoFinalidad}.
   * 
   * @param tipoFinalidad {@link TipoFinalidad}. que se quiere crear.
   * @return Nuevo {@link TipoFinalidad} creado.
   */
  @PostMapping
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-TFIN-C')")
  public ResponseEntity<TipoFinalidad> create(@Valid @RequestBody TipoFinalidad tipoFinalidad) {
    log.debug("create(TipoFinalidad tipoFinalidad) - start");
    TipoFinalidad returnValue = service.create(tipoFinalidad);
    log.debug("create(TipoFinalidad tipoFinalidad) - end");
    return new ResponseEntity<>(returnValue, HttpStatus.CREATED);
  }

  /**
   * Actualiza {@link TipoFinalidad}.
   * 
   * @param tipoFinalidad {@link TipoFinalidad} a actualizar.
   * @param id            Identificador {@link TipoFinalidad} a actualizar.
   * @return TipoFinalidad {@link TipoFinalidad} actualizado
   */
  @PutMapping("/{id}")
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-TFIN-E')")
  public TipoFinalidad update(@Valid @RequestBody TipoFinalidad tipoFinalidad, @PathVariable Long id) {
    log.debug("update(TipoFinalidad tipoFinalidad, Long id) - start");
    tipoFinalidad.setId(id);
    TipoFinalidad returnValue = service.update(tipoFinalidad);
    log.debug("update(TipoFinalidad tipoFinalidad, Long id) - end");
    return returnValue;
  }

  /**
   * Desactiva {@link TipoFinalidad} con id indicado.
   * 
   * @param id Identificador de {@link TipoFinalidad}.
   */
  @DeleteMapping("/{id}")
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-TFIN-B')")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  void deleteById(@PathVariable Long id) {
    log.debug("deleteById(Long id) - start");
    service.disable(id);
    log.debug("deleteById(Long id) - end");
  }

  /**
   * Devuelve una lista paginada y filtrada {@link TipoFinalidad} activos.
   * 
   * @param query  filtro de {@link QueryCriteria}.
   * @param paging {@link Pageable}.
   * @return el listado de entidades {@link TipoFinalidad} paginadas y filtradas.
   */
  @GetMapping()
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-TFIN-V')")
  ResponseEntity<Page<TipoFinalidad>> findAll(@RequestParam(name = "q", required = false) List<QueryCriteria> query,
      @RequestPageable(sort = "s") Pageable paging) {
    log.debug("findAll(List<QueryCriteria> query, Pageable paging) - start");
    Page<TipoFinalidad> page = service.findAll(query, paging);

    if (page.isEmpty()) {
      log.debug("findAll(List<QueryCriteria> query, Pageable paging) - end");
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    log.debug("findAll(List<QueryCriteria> query, Pageable paging) - end");
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  /**
   * Devuelve una lista paginada y filtrada {@link TipoFinalidad}.
   * 
   * @param query  filtro de {@link QueryCriteria}.
   * @param paging {@link Pageable}.
   * @return el listado de entidades {@link TipoFinalidad} paginadas y filtradas.
   */
  @GetMapping("/todos")
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-TFIN-V')")
  ResponseEntity<Page<TipoFinalidad>> findAllTodos(
      @RequestParam(name = "q", required = false) List<QueryCriteria> query,
      @RequestPageable(sort = "s") Pageable paging) {
    log.debug("findAllTodos(List<QueryCriteria> query,Pageable paging) - start");
    Page<TipoFinalidad> page = service.findAllTodos(query, paging);

    if (page.isEmpty()) {
      log.debug("findAllTodos(List<QueryCriteria> query,Pageable paging) - end");
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    log.debug("findAllTodos(List<QueryCriteria> query,Pageable paging) - end");
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  /**
   * Devuelve el {@link TipoFinalidad} con el id indicado.
   * 
   * @param id Identificador de {@link TipoFinalidad}.
   * @return TipoFinalidad {@link TipoFinalidad} correspondiente al id
   */
  @GetMapping("/{id}")
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-TFIN-V')")
  TipoFinalidad findById(@PathVariable Long id) {
    log.debug("TipoFinalidad findById(Long id) - start");
    TipoFinalidad returnValue = service.findById(id);
    log.debug("TipoFinalidad findById(Long id) - end");
    return returnValue;
  }
}
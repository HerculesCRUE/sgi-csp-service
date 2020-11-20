package org.crue.hercules.sgi.csp.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.groups.Default;

import org.crue.hercules.sgi.csp.model.AreaTematica;
import org.crue.hercules.sgi.csp.model.BaseEntity.Update;
import org.crue.hercules.sgi.csp.service.AreaTematicaService;
import org.crue.hercules.sgi.framework.data.search.QueryCriteria;
import org.crue.hercules.sgi.framework.web.bind.annotation.RequestPageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * AreaTematicaController
 */
@RestController
@RequestMapping("/areatematicas")
@Slf4j
public class AreaTematicaController {

  /** AreaTematica service */
  private final AreaTematicaService service;

  /**
   * Instancia un nuevo AreaTematicaController.
   * 
   * @param service {@link AreaTematicaService}
   */
  public AreaTematicaController(AreaTematicaService service) {
    this.service = service;
  }

  /**
   * Devuelve el {@link AreaTematica} con el id indicado.
   * 
   * @param id Identificador de {@link AreaTematica}.
   * @return {@link AreaTematica} correspondiente al id.
   */
  @GetMapping("/{id}")
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-ARTM-V')")
  AreaTematica findById(@PathVariable Long id) {
    log.debug("findById(Long id) - start");
    AreaTematica returnValue = service.findById(id);
    log.debug("findById(Long id) - end");
    return returnValue;
  }

  /**
   * Crea un nuevo {@link AreaTematica}.
   * 
   * @param areaTematica {@link AreaTematica} que se quiere crear.
   * @return Nuevo {@link AreaTematica} creado.
   */
  @PostMapping
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-ARTM-C')")
  ResponseEntity<AreaTematica> create(@Valid @RequestBody AreaTematica areaTematica) {
    log.debug("create(AreaTematica areaTematica) - start");
    AreaTematica returnValue = service.create(areaTematica);
    log.debug("create(AreaTematica areaTematica) - end");
    return new ResponseEntity<>(returnValue, HttpStatus.CREATED);
  }

  /**
   * Actualiza el {@link AreaTematica} con el id indicado.
   * 
   * @param areaTematica {@link AreaTematica} a actualizar.
   * @param id           id {@link AreaTematica} a actualizar.
   * @return {@link AreaTematica} actualizado.
   */
  @PutMapping("/{id}")
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-ARTM-E')")
  AreaTematica update(@Validated({ Update.class, Default.class }) @RequestBody AreaTematica areaTematica,
      @PathVariable Long id) {
    log.debug("update(AreaTematica areaTematica, Long id) - start");
    areaTematica.setId(id);
    AreaTematica returnValue = service.update(areaTematica);
    log.debug("update(AreaTematica areaTematica, Long id) - end");
    return returnValue;
  }

  /**
   * Reactiva el {@link AreaTematica} con id indicado.
   * 
   * @param id Identificador de {@link AreaTematica}.
   * @return {@link AreaTematica} actualizado.
   */
  @PatchMapping("/{id}/reactivar")
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-ARTM-R')")
  AreaTematica reactivar(@PathVariable Long id) {
    log.debug("reactivar(Long id) - start");
    AreaTematica returnValue = service.enable(id);
    log.debug("reactivar(Long id) - end");
    return returnValue;
  }

  /**
   * Desactiva el {@link AreaTematica} con id indicado.
   * 
   * @param id Identificador de {@link AreaTematica}.
   * @return {@link AreaTematica} actualizado.
   */
  @PatchMapping("/{id}/desactivar")
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-ARTM-E')")
  AreaTematica desactivar(@PathVariable Long id) {
    log.debug("desactivar(Long id) - start");
    AreaTematica returnValue = service.disable(id);
    log.debug("desactivar(Long id) - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades {@link AreaTematica} activos paginadas
   *
   * @param query    la información del filtro.
   * @param pageable la información de la paginación.
   * @return la lista de entidades {@link AreaTematica} paginadas
   */
  @GetMapping()
  // @PreAuthorize("hasAuthorityForAnyUO ('CSP-ARTM-V')")
  ResponseEntity<Page<AreaTematica>> findAll(@RequestParam(name = "q", required = false) List<QueryCriteria> query,
      @RequestPageable(sort = "s") Pageable paging) {
    log.debug("findAll(List<QueryCriteria> query, Pageable paging) - start");
    Page<AreaTematica> page = service.findAll(query, paging);

    if (page.isEmpty()) {
      log.debug("findAll(List<QueryCriteria> query, Pageable paging) - end");
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    log.debug("findAll(List<QueryCriteria> query, Pageable paging) - end");
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  /**
   * Devuelve todas las entidades {@link AreaTematica} activos con padre null (los
   * grupo) paginadas
   *
   * @param query    la información del filtro.
   * @param pageable la información de la paginación.
   * @return la lista de entidades {@link AreaTematica} paginadas
   */
  @GetMapping("/grupo")
  // @PreAuthorize("hasAuthorityForAnyUO ('CSP-ARTM-V')")
  ResponseEntity<Page<AreaTematica>> findAllGrupo(@RequestParam(name = "q", required = false) List<QueryCriteria> query,
      @RequestPageable(sort = "s") Pageable paging) {
    log.debug("findAllGrupo(List<QueryCriteria> query, Pageable paging) - start");
    Page<AreaTematica> page = service.findAllGrupo(query, paging);

    if (page.isEmpty()) {
      log.debug("findAllGrupo(List<QueryCriteria> query, Pageable paging) - end");
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    log.debug("findAllGrupo(List<QueryCriteria> query, Pageable paging) - end");
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  /**
   * Devuelve todas las entidades {@link AreaTematica} con padre null (los grupos)
   * paginadas
   *
   * @param query    la información del filtro.
   * @param pageable la información de la paginación.
   * @return la lista de entidades {@link AreaTematica} paginadas
   */
  @GetMapping("/grupo/todos")
  // @PreAuthorize("hasAuthorityForAnyUO ('CSP-ARTM-V')")
  ResponseEntity<Page<AreaTematica>> findAllTodosGrupo(
      @RequestParam(name = "q", required = false) List<QueryCriteria> query,
      @RequestPageable(sort = "s") Pageable paging) {
    log.debug("findAllTodosGrupo(List<QueryCriteria> query, Pageable paging) - start");
    Page<AreaTematica> page = service.findAllTodosGrupo(query, paging);

    if (page.isEmpty()) {
      log.debug("findAllTodosGrupo(List<QueryCriteria> query, Pageable paging) - end");
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    log.debug("findAllTodosGrupo(List<QueryCriteria> query, Pageable paging) - end");
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  /**
   * Devuelve todas las entidades {@link AreaTematica} hijos directos del
   * {@link AreaTematica} con el id indicado paginadas
   *
   * @param query    la información del filtro.
   * @param pageable la información de la paginación.
   * @return la lista de entidades {@link AreaTematica} paginadas
   */
  @GetMapping("/{id}/hijos")
  // @PreAuthorize("hasAuthorityForAnyUO ('CSP-ARTM-V')")
  ResponseEntity<Page<AreaTematica>> findAllHijosAreaTematica(@PathVariable Long id,
      @RequestParam(name = "q", required = false) List<QueryCriteria> query,
      @RequestPageable(sort = "s") Pageable paging) {
    log.debug("findAllHijosAreaTematica(List<QueryCriteria> query, Pageable paging) - start");
    Page<AreaTematica> page = service.findAllHijosAreaTematica(id, query, paging);

    if (page.isEmpty()) {
      log.debug("findAllHijosAreaTematica(List<QueryCriteria> query, Pageable paging) - end");
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    log.debug("findAllHijosAreaTematica(List<QueryCriteria> query, Pageable paging) - end");
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

}
package org.crue.hercules.sgi.csp.controller;

import javax.validation.Valid;
import javax.validation.groups.Default;

import org.crue.hercules.sgi.csp.model.BaseEntity.Update;
import org.crue.hercules.sgi.csp.model.ConvocatoriaConceptoGasto;
import org.crue.hercules.sgi.csp.service.ConvocatoriaConceptoGastoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * ConvocatoriaConceptoGastoController
 */
@RestController
@RequestMapping("/convocatoriaconceptogastos")
@Slf4j
public class ConvocatoriaConceptoGastoController {

  /** ConvocatoriaConceptoGasto service */
  private final ConvocatoriaConceptoGastoService service;

  /**
   * Instancia un nuevo ConvocatoriaConceptoGastoController.
   * 
   * @param service {@link ConvocatoriaConceptoGastoService}
   */
  public ConvocatoriaConceptoGastoController(ConvocatoriaConceptoGastoService service) {
    this.service = service;
  }

  /**
   * Devuelve el {@link ConvocatoriaConceptoGasto} con el id indicado.
   * 
   * @param id Identificador de {@link ConvocatoriaConceptoGasto}.
   * @return {@link ConvocatoriaConceptoGasto} correspondiente al id.
   */
  @GetMapping("/{id}")
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-CFAS-V')")
  ConvocatoriaConceptoGasto findById(@PathVariable Long id) {
    log.debug("findById(Long id) - start");
    ConvocatoriaConceptoGasto returnValue = service.findById(id);
    log.debug("findById(Long id) - end");
    return returnValue;
  }

  /**
   * Crea un nuevo {@link ConvocatoriaConceptoGasto}.
   * 
   * @param convocatoriaConceptoGasto {@link ConvocatoriaConceptoGasto} que se
   *                                  quiere crear.
   * @return Nuevo {@link ConvocatoriaConceptoGasto} creado.
   */
  @PostMapping
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-CFAS-C')")
  ResponseEntity<ConvocatoriaConceptoGasto> create(
      @Valid @RequestBody ConvocatoriaConceptoGasto convocatoriaConceptoGasto) {
    log.debug("create(ConvocatoriaConceptoGasto convocatoriaConceptoGasto) - start");
    ConvocatoriaConceptoGasto returnValue = service.create(convocatoriaConceptoGasto);
    log.debug("create(ConvocatoriaConceptoGasto convocatoriaConceptoGasto) - end");
    return new ResponseEntity<>(returnValue, HttpStatus.CREATED);
  }

  /**
   * Actualiza el {@link ConvocatoriaConceptoGasto} con el id indicado.
   * 
   * @param convocatoriaConceptoGasto {@link ConvocatoriaConceptoGasto} a
   *                                  actualizar.
   * @param id                        id {@link ConvocatoriaConceptoGasto} a
   *                                  actualizar.
   * @return {@link ConvocatoriaConceptoGasto} actualizado.
   */
  @PutMapping("/{id}")
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-CFAS-E')")
  ConvocatoriaConceptoGasto update(
      @Validated({ Update.class, Default.class }) @RequestBody ConvocatoriaConceptoGasto convocatoriaConceptoGasto,
      @PathVariable Long id) {
    log.debug("update(ConvocatoriaConceptoGasto convocatoriaConceptoGasto, Long id) - start");
    convocatoriaConceptoGasto.setId(id);
    ConvocatoriaConceptoGasto returnValue = service.update(convocatoriaConceptoGasto);
    log.debug("update(ConvocatoriaConceptoGasto convocatoriaConceptoGasto, Long id) - end");
    return returnValue;
  }

  /**
   * Desactiva el {@link ConvocatoriaConceptoGasto} con id indicado.
   * 
   * @param id Identificador de {@link ConvocatoriaConceptoGasto}.
   */
  @DeleteMapping("/{id}")
  // @PreAuthorize("hasAuthorityForAnyUO('CSP-CFAS-B')")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  void deleteById(@PathVariable Long id) {
    log.debug("deleteById(Long id) - start");
    service.delete(id);
    log.debug("deleteById(Long id) - end");
  }

}
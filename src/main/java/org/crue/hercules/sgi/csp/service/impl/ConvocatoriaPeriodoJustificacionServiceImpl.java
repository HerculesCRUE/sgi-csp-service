package org.crue.hercules.sgi.csp.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.crue.hercules.sgi.csp.enums.TipoJustificacionEnum;
import org.crue.hercules.sgi.csp.exceptions.ConvocatoriaNotFoundException;
import org.crue.hercules.sgi.csp.exceptions.ConvocatoriaPeriodoJustificacionNotFoundException;
import org.crue.hercules.sgi.csp.model.Convocatoria;
import org.crue.hercules.sgi.csp.model.ConvocatoriaPeriodoJustificacion;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaPeriodoJustificacionRepository;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaRepository;
import org.crue.hercules.sgi.csp.repository.specification.ConvocatoriaPeriodoJustificacionSpecifications;
import org.crue.hercules.sgi.csp.service.ConvocatoriaPeriodoJustificacionService;
import org.crue.hercules.sgi.framework.data.jpa.domain.QuerySpecification;
import org.crue.hercules.sgi.framework.data.search.QueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation para la gestión de
 * {@link ConvocatoriaPeriodoJustificacion}.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class ConvocatoriaPeriodoJustificacionServiceImpl implements ConvocatoriaPeriodoJustificacionService {

  private final ConvocatoriaPeriodoJustificacionRepository repository;
  private final ConvocatoriaRepository convocatoriaRepository;

  public ConvocatoriaPeriodoJustificacionServiceImpl(
      ConvocatoriaPeriodoJustificacionRepository convocatoriaPeriodoJustificacionRepository,
      ConvocatoriaRepository convocatoriaRepository) {
    this.repository = convocatoriaPeriodoJustificacionRepository;
    this.convocatoriaRepository = convocatoriaRepository;
  }

  /**
   * Actualiza el listado de {@link ConvocatoriaPeriodoJustificacion} de la
   * {@link Convocatoria} con el listado convocatoriaPeriodoJustificaciones
   * añadiendo, editando o eliminando los elementos segun proceda.
   *
   * @param convocatoriaId                     Id de la {@link Convocatoria}.
   * @param convocatoriaPeriodoJustificaciones lista con los nuevos
   *                                           {@link ConvocatoriaPeriodoJustificacion}
   *                                           a guardar.
   * @return la entidad {@link ConvocatoriaPeriodoJustificacion} persistida.
   */
  @Override
  @Transactional
  public List<ConvocatoriaPeriodoJustificacion> updateConvocatoriaPeriodoJustificacionesConvocatoria(
      Long convocatoriaId, List<ConvocatoriaPeriodoJustificacion> convocatoriaPeriodoJustificaciones) {
    log.debug(
        "updateConvocatoriaPeriodoJustificacionesConvocatoria(Long convocatoriaId, List<ConvocatoriaPeriodoJustificacion> convocatoriaPeriodoJustificaciones) - start");

    if (convocatoriaPeriodoJustificaciones.isEmpty()) {
      return new ArrayList<>();
    }

    Convocatoria convocatoria = convocatoriaRepository.findById(convocatoriaId)
        .orElseThrow(() -> new ConvocatoriaNotFoundException(convocatoriaId));

    List<ConvocatoriaPeriodoJustificacion> convocatoriaPeriodoJustificacionesBD = repository
        .findAllByConvocatoriaId(convocatoriaId);

    // Periodos eliminados
    List<ConvocatoriaPeriodoJustificacion> periodoJustificacionesEliminar = convocatoriaPeriodoJustificacionesBD
        .stream().filter(periodo -> !convocatoriaPeriodoJustificaciones.stream()
            .map(ConvocatoriaPeriodoJustificacion::getId).anyMatch(id -> id == periodo.getId()))
        .collect(Collectors.toList());

    if (!periodoJustificacionesEliminar.isEmpty()) {
      repository.deleteAll(periodoJustificacionesEliminar);
    }

    // Ordena los periodos por mesInicial
    convocatoriaPeriodoJustificaciones.sort(Comparator.comparing(ConvocatoriaPeriodoJustificacion::getMesInicial));

    AtomicInteger numPeriodo = new AtomicInteger(0);

    ConvocatoriaPeriodoJustificacion periodoJustificacionAnterior = null;
    for (ConvocatoriaPeriodoJustificacion periodoJustificacion : convocatoriaPeriodoJustificaciones) {
      // Actualiza el numero de periodo
      periodoJustificacion.setNumPeriodo(numPeriodo.incrementAndGet());

      // Si tiene id se valida que exista y que tenga la convocatoria de la que se
      // estan actualizando los periodos
      if (periodoJustificacion.getId() != null) {
        ConvocatoriaPeriodoJustificacion periodoJustificacionBD = convocatoriaPeriodoJustificacionesBD.stream()
            .filter(periodo -> periodo.getId() == periodoJustificacion.getId()).findFirst()
            .orElseThrow(() -> new ConvocatoriaPeriodoJustificacionNotFoundException(periodoJustificacion.getId()));

        Assert.isTrue(
            periodoJustificacionBD.getConvocatoria().getId() == periodoJustificacion.getConvocatoria().getId(),
            "No se puede modificar la convocatoria del ConvocatoriaPeriodoJustificacion");
      }

      // Setea la convocatoria recuperada del convocatoriaId
      periodoJustificacion.setConvocatoria(convocatoria);

      // Validaciones
      Assert.isTrue(periodoJustificacion.getMesInicial() < periodoJustificacion.getMesFinal(),
          "El mes final tiene que ser posterior al mes inicial");

      if (periodoJustificacion.getFechaInicioPresentacion() != null
          && periodoJustificacion.getFechaFinPresentacion() != null) {
        Assert.isTrue(
            periodoJustificacion.getFechaInicioPresentacion().isBefore(periodoJustificacion.getFechaFinPresentacion()),
            "La fecha de fin tiene que ser posterior a la fecha de inicio");
      }

      Assert.isTrue(
          periodoJustificacion.getConvocatoria().getDuracion() == null
              || periodoJustificacion.getMesFinal() <= periodoJustificacion.getConvocatoria().getDuracion(),
          "El mes final no puede ser superior a la duración en meses indicada en la Convocatoria");

      Assert.isTrue(
          periodoJustificacionAnterior == null || (periodoJustificacionAnterior != null
              && periodoJustificacionAnterior.getMesFinal() < periodoJustificacion.getMesInicial()),
          "El periodo se solapa con otro existente");

      Assert.isTrue(
          periodoJustificacionAnterior == null || (periodoJustificacionAnterior != null
              && periodoJustificacionAnterior.getTipoJustificacion().equals(TipoJustificacionEnum.PERIODICA)),
          "El ConvocatoriaPeriodoJustificacion de tipo final tiene que ser el último");

      periodoJustificacionAnterior = periodoJustificacion;
    }

    List<ConvocatoriaPeriodoJustificacion> returnValue = repository.saveAll(convocatoriaPeriodoJustificaciones);
    log.debug(
        "updateConvocatoriaPeriodoJustificacionesConvocatoria(Long convocatoriaId, List<ConvocatoriaPeriodoJustificacion> convocatoriaPeriodoJustificaciones) - end");

    return returnValue;
  }

  /**
   * Obtiene {@link ConvocatoriaPeriodoJustificacion} por su id.
   *
   * @param id el id de la entidad {@link ConvocatoriaPeriodoJustificacion}.
   * @return la entidad {@link ConvocatoriaPeriodoJustificacion}.
   */
  @Override
  public ConvocatoriaPeriodoJustificacion findById(Long id) {
    log.debug("findById(Long id)  - start");
    final ConvocatoriaPeriodoJustificacion returnValue = repository.findById(id)
        .orElseThrow(() -> new ConvocatoriaPeriodoJustificacionNotFoundException(id));
    log.debug("findById(Long id)  - end");
    return returnValue;
  }

  /**
   * Obtiene las {@link ConvocatoriaPeriodoJustificacion} para una
   * {@link Convocatoria}.
   *
   * @param idConvocatoria el id de la {@link Convocatoria}.
   * @param query          la información del filtro.
   * @param pageable       la información de la paginación.
   * @return la lista de entidades {@link ConvocatoriaPeriodoJustificacion} de la
   *         {@link Convocatoria} paginadas.
   */
  public Page<ConvocatoriaPeriodoJustificacion> findAllByConvocatoria(Long idConvocatoria, List<QueryCriteria> query,
      Pageable pageable) {
    log.debug("findAllByConvocatoria(Long idConvocatoria, List<QueryCriteria> query, Pageable pageable) - start");
    Specification<ConvocatoriaPeriodoJustificacion> specByQuery = new QuerySpecification<ConvocatoriaPeriodoJustificacion>(
        query);
    Specification<ConvocatoriaPeriodoJustificacion> specByConvocatoria = ConvocatoriaPeriodoJustificacionSpecifications
        .byConvocatoriaId(idConvocatoria);

    Specification<ConvocatoriaPeriodoJustificacion> specs = Specification.where(specByConvocatoria).and(specByQuery);

    Page<ConvocatoriaPeriodoJustificacion> returnValue = repository.findAll(specs, pageable);
    log.debug("findAllByConvocatoria(Long idConvocatoria, List<QueryCriteria> query, Pageable pageable) - end");
    return returnValue;
  }

}

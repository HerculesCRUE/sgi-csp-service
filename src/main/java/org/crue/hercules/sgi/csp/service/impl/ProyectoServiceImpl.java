package org.crue.hercules.sgi.csp.service.impl;

import java.time.Instant;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.crue.hercules.sgi.csp.enums.FormularioSolicitud;
import org.crue.hercules.sgi.csp.exceptions.ConvocatoriaNotFoundException;
import org.crue.hercules.sgi.csp.exceptions.ProyectoNotFoundException;
import org.crue.hercules.sgi.csp.exceptions.SolicitudNotFoundException;
import org.crue.hercules.sgi.csp.model.ContextoProyecto;
import org.crue.hercules.sgi.csp.model.Convocatoria;
import org.crue.hercules.sgi.csp.model.ConvocatoriaAreaTematica;
import org.crue.hercules.sgi.csp.model.ConvocatoriaConceptoGasto;
import org.crue.hercules.sgi.csp.model.ConvocatoriaEntidadConvocante;
import org.crue.hercules.sgi.csp.model.ConvocatoriaEntidadFinanciadora;
import org.crue.hercules.sgi.csp.model.ConvocatoriaEntidadGestora;
import org.crue.hercules.sgi.csp.model.ConvocatoriaPeriodoSeguimientoCientifico;
import org.crue.hercules.sgi.csp.model.EstadoProyecto;
import org.crue.hercules.sgi.csp.model.EstadoSolicitud;
import org.crue.hercules.sgi.csp.model.ModeloEjecucion;
import org.crue.hercules.sgi.csp.model.ModeloUnidad;
import org.crue.hercules.sgi.csp.model.Proyecto;
import org.crue.hercules.sgi.csp.model.ProyectoEntidadConvocante;
import org.crue.hercules.sgi.csp.model.ProyectoEntidadFinanciadora;
import org.crue.hercules.sgi.csp.model.ProyectoEntidadGestora;
import org.crue.hercules.sgi.csp.model.ProyectoEquipo;
import org.crue.hercules.sgi.csp.model.ProyectoPeriodoSeguimiento;
import org.crue.hercules.sgi.csp.model.ProyectoSocio;
import org.crue.hercules.sgi.csp.model.ProyectoSocioEquipo;
import org.crue.hercules.sgi.csp.model.ProyectoSocioPeriodoJustificacion;
import org.crue.hercules.sgi.csp.model.ProyectoSocioPeriodoPago;
import org.crue.hercules.sgi.csp.model.Solicitud;
import org.crue.hercules.sgi.csp.model.SolicitudModalidad;
import org.crue.hercules.sgi.csp.model.SolicitudProyecto;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoEntidadFinanciadoraAjena;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoEquipo;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoSocioEquipo;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoSocioPeriodoJustificacion;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoSocioPeriodoPago;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoSocio;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaAreaTematicaRepository;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaConceptoGastoRepository;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaEntidadConvocanteRepository;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaEntidadFinanciadoraRepository;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaEntidadGestoraRepository;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaPeriodoSeguimientoCientificoRepository;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaRepository;
import org.crue.hercules.sgi.csp.repository.EstadoProyectoRepository;
import org.crue.hercules.sgi.csp.repository.ModeloUnidadRepository;
import org.crue.hercules.sgi.csp.repository.ProgramaRepository;
import org.crue.hercules.sgi.csp.repository.ProyectoRepository;
import org.crue.hercules.sgi.csp.repository.SolicitudModalidadRepository;
import org.crue.hercules.sgi.csp.repository.SolicitudProyectoEntidadFinanciadoraAjenaRepository;
import org.crue.hercules.sgi.csp.repository.SolicitudProyectoEquipoRepository;
import org.crue.hercules.sgi.csp.repository.SolicitudProyectoSocioEquipoRepository;
import org.crue.hercules.sgi.csp.repository.SolicitudProyectoSocioPeriodoJustificacionRepository;
import org.crue.hercules.sgi.csp.repository.SolicitudProyectoSocioPeriodoPagoRepository;
import org.crue.hercules.sgi.csp.repository.SolicitudProyectoRepository;
import org.crue.hercules.sgi.csp.repository.SolicitudProyectoSocioRepository;
import org.crue.hercules.sgi.csp.repository.SolicitudRepository;
import org.crue.hercules.sgi.csp.repository.predicate.ProyectoPredicateResolver;
import org.crue.hercules.sgi.csp.repository.specification.ConvocatoriaEntidadConvocanteSpecifications;
import org.crue.hercules.sgi.csp.repository.specification.ProyectoSpecifications;
import org.crue.hercules.sgi.csp.service.ContextoProyectoService;
import org.crue.hercules.sgi.csp.service.ProyectoEntidadConvocanteService;
import org.crue.hercules.sgi.csp.service.ProyectoEntidadFinanciadoraService;
import org.crue.hercules.sgi.csp.service.ProyectoEntidadGestoraService;
import org.crue.hercules.sgi.csp.service.ProyectoEquipoService;
import org.crue.hercules.sgi.csp.service.ProyectoPeriodoSeguimientoService;
import org.crue.hercules.sgi.csp.service.ProyectoService;
import org.crue.hercules.sgi.csp.service.ProyectoSocioEquipoService;
import org.crue.hercules.sgi.csp.service.ProyectoSocioPeriodoJustificacionService;
import org.crue.hercules.sgi.csp.service.ProyectoSocioPeriodoPagoService;
import org.crue.hercules.sgi.csp.service.ProyectoSocioService;
import org.crue.hercules.sgi.csp.util.ProyectoHelper;
import org.crue.hercules.sgi.framework.rsql.SgiRSQLJPASupport;
import org.crue.hercules.sgi.framework.security.core.context.SgiSecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio implementación para la gestión de {@link Proyecto}.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class ProyectoServiceImpl implements ProyectoService {

  /**
   * Valor por defecto del atributo ajena en la copia de entidades financiadoras
   */
  private static final Boolean DEFAULT_COPY_ENTIDAD_FINANCIADORA_AJENA_VALUE = Boolean.FALSE;

  private final ProyectoRepository repository;
  private final EstadoProyectoRepository estadoProyectoRepository;
  private final ModeloUnidadRepository modeloUnidadRepository;
  private final ConvocatoriaRepository convocatoriaRepository;
  private final ConvocatoriaEntidadFinanciadoraRepository convocatoriaEntidadFinanciadoraRepository;
  private final ProyectoEntidadFinanciadoraService proyectoEntidadFinanciadoraService;
  private final ConvocatoriaEntidadConvocanteRepository convocatoriaEntidadConvocanteRepository;
  private final ProyectoEntidadConvocanteService proyectoEntidadConvocanteService;
  private final ConvocatoriaEntidadGestoraRepository convocatoriaEntidadGestoraRepository;
  private final ProyectoEntidadGestoraService proyectoEntidadGestoraService;
  private final ConvocatoriaAreaTematicaRepository convocatoriaAreaTematicaRepository;
  private final ContextoProyectoService contextoProyectoService;
  private final ConvocatoriaPeriodoSeguimientoCientificoRepository convocatoriaPeriodoSeguimientoCientificoRepository;
  private final ProyectoPeriodoSeguimientoService proyectoPeriodoSeguimientoService;
  private final SolicitudRepository solicitudRepository;
  private final SolicitudProyectoRepository solicitudProyectoRepository;
  private final SolicitudModalidadRepository solicitudModalidadRepository;
  private final SolicitudProyectoEquipoRepository solicitudEquipoRepository;
  private final ProyectoEquipoService proyectoEquipoService;
  private final SolicitudProyectoSocioRepository solicitudSocioRepository;
  private final ProyectoSocioService proyectoSocioService;
  private final SolicitudProyectoSocioEquipoRepository solicitudEquipoSocioRepository;
  private final ProyectoSocioEquipoService proyectoEquipoSocioService;
  private final SolicitudProyectoSocioPeriodoPagoRepository solicitudPeriodoPagoRepository;
  private final ProyectoSocioPeriodoPagoService proyectoSocioPeriodoPagoService;
  private final SolicitudProyectoSocioPeriodoJustificacionRepository solicitudPeriodoJustificacionRepository;
  private final ProyectoSocioPeriodoJustificacionService proyectoSocioPeriodoJustificacionService;
  private final ConvocatoriaConceptoGastoRepository convocatoriaConceptoGastoRepository;
  private final SolicitudProyectoEntidadFinanciadoraAjenaRepository solicitudProyectoEntidadFinanciadoraAjenaRepository;
  private final ProgramaRepository programaRepository;

  public ProyectoServiceImpl(ProyectoRepository repository, EstadoProyectoRepository estadoProyectoRepository,
      ModeloUnidadRepository modeloUnidadRepository, ConvocatoriaRepository convocatoriaRepository,
      ConvocatoriaEntidadFinanciadoraRepository convocatoriaEntidadFinanciadoraRepository,
      ProyectoEntidadFinanciadoraService proyectoEntidadFinanciadoraService,
      ConvocatoriaEntidadConvocanteRepository convocatoriaEntidadConvocanteRepository,
      ProyectoEntidadConvocanteService proyectoEntidadConvocanteService,
      ConvocatoriaEntidadGestoraRepository convocatoriaEntidadGestoraRepository,
      ProyectoEntidadGestoraService proyectoEntidadGestoraService,
      ConvocatoriaAreaTematicaRepository convocatoriaAreaTematicaRepository,
      ContextoProyectoService contextoProyectoService,
      ConvocatoriaPeriodoSeguimientoCientificoRepository convocatoriaPeriodoSeguimientoCientificoRepository,
      ProyectoPeriodoSeguimientoService proyectoPeriodoSeguimientoService, SolicitudRepository solicitudRepository,
      SolicitudProyectoRepository solicitudProyectoRepository,
      SolicitudModalidadRepository solicitudModalidadRepository,
      SolicitudProyectoEquipoRepository solicitudEquipoRepository, ProyectoEquipoService proyectoEquipoService,
      SolicitudProyectoSocioRepository solicitudSocioRepository, ProyectoSocioService proyectoSocioService,
      SolicitudProyectoSocioEquipoRepository solicitudEquipoSocioRepository,
      ProyectoSocioEquipoService proyectoEquipoSocioService,
      SolicitudProyectoSocioPeriodoPagoRepository solicitudPeriodoPagoRepository,
      ProyectoSocioPeriodoPagoService proyectoSocioPeriodoPagoService,
      SolicitudProyectoSocioPeriodoJustificacionRepository solicitudPeriodoJustificacionRepository,
      ProyectoSocioPeriodoJustificacionService proyectoSocioPeriodoJustificacionService,
      ConvocatoriaConceptoGastoRepository convocatoriaConceptoGastoRepository,
      SolicitudProyectoEntidadFinanciadoraAjenaRepository solicitudProyectoEntidadFinanciadoraAjenaRepository,
      ProgramaRepository programaRepository) {
    this.repository = repository;
    this.estadoProyectoRepository = estadoProyectoRepository;
    this.modeloUnidadRepository = modeloUnidadRepository;
    this.convocatoriaRepository = convocatoriaRepository;
    this.convocatoriaEntidadFinanciadoraRepository = convocatoriaEntidadFinanciadoraRepository;
    this.proyectoEntidadFinanciadoraService = proyectoEntidadFinanciadoraService;
    this.convocatoriaEntidadConvocanteRepository = convocatoriaEntidadConvocanteRepository;
    this.proyectoEntidadConvocanteService = proyectoEntidadConvocanteService;
    this.convocatoriaAreaTematicaRepository = convocatoriaAreaTematicaRepository;
    this.contextoProyectoService = contextoProyectoService;
    this.convocatoriaPeriodoSeguimientoCientificoRepository = convocatoriaPeriodoSeguimientoCientificoRepository;
    this.proyectoPeriodoSeguimientoService = proyectoPeriodoSeguimientoService;
    this.convocatoriaEntidadGestoraRepository = convocatoriaEntidadGestoraRepository;
    this.proyectoEntidadGestoraService = proyectoEntidadGestoraService;
    this.solicitudRepository = solicitudRepository;
    this.solicitudProyectoRepository = solicitudProyectoRepository;
    this.solicitudModalidadRepository = solicitudModalidadRepository;
    this.solicitudEquipoRepository = solicitudEquipoRepository;
    this.proyectoEquipoService = proyectoEquipoService;
    this.solicitudSocioRepository = solicitudSocioRepository;
    this.proyectoSocioService = proyectoSocioService;
    this.solicitudEquipoSocioRepository = solicitudEquipoSocioRepository;
    this.proyectoEquipoSocioService = proyectoEquipoSocioService;
    this.solicitudPeriodoPagoRepository = solicitudPeriodoPagoRepository;
    this.proyectoSocioPeriodoPagoService = proyectoSocioPeriodoPagoService;
    this.solicitudPeriodoJustificacionRepository = solicitudPeriodoJustificacionRepository;
    this.proyectoSocioPeriodoJustificacionService = proyectoSocioPeriodoJustificacionService;
    this.convocatoriaConceptoGastoRepository = convocatoriaConceptoGastoRepository;
    this.solicitudProyectoEntidadFinanciadoraAjenaRepository = solicitudProyectoEntidadFinanciadoraAjenaRepository;
    this.programaRepository = programaRepository;
  }

  /**
   * Guarda la entidad {@link Proyecto}.
   * 
   * @param proyecto la entidad {@link Proyecto} a guardar.
   * @return proyecto la entidad {@link Proyecto} persistida.
   */
  @Override
  @Transactional
  public Proyecto create(Proyecto proyecto) {
    log.debug("create(Proyecto proyecto) - start");
    Assert.isNull(proyecto.getId(), "Proyecto id tiene que ser null para crear un Proyecto");
    // TODO: Add right authority
    Assert.isTrue(SgiSecurityContextHolder.hasAuthorityForUO("CSP-PRO-C", proyecto.getUnidadGestionRef()),
        "La Unidad de Gestión no es gestionable por el usuario");

    this.validarDatos(proyecto);

    proyecto.setActivo(Boolean.TRUE);

    // Crea el proyecto
    repository.save(proyecto);

    // Crea el estado inicial del proyecto
    EstadoProyecto estadoProyecto = addEstadoProyecto(proyecto, EstadoProyecto.Estado.BORRADOR, null);

    proyecto.setEstado(estadoProyecto);
    // Actualiza el estado actual del proyecto con el nuevo estado
    Proyecto returnValue = repository.save(proyecto);

    // Si hay asignada una convocatoria se deben de rellenar las entidades
    // correspondientes con los datos de la convocatoria
    if (proyecto.getConvocatoriaId() != null) {
      this.copyDatosConvocatoriaToProyecto(proyecto);
    }

    log.debug("create(Proyecto proyecto) - end");
    return returnValue;
  }

  /**
   * Actualiza los datos del {@link Proyecto}.
   * 
   * @param proyectoActualizar proyectoActualizar {@link Proyecto} con los datos
   *                           actualizados.
   * @return {@link Proyecto} actualizado.
   */
  @Override
  @Transactional
  public Proyecto update(Proyecto proyectoActualizar) {
    log.debug("update(Proyecto proyecto) - start");

    this.validarDatos(proyectoActualizar);

    return repository.findById(proyectoActualizar.getId()).map((data) -> {
      ProyectoHelper.checkCanUpdate(data);
      Assert.isTrue(
          proyectoActualizar.getEstado().getId() == data.getEstado().getId()
              && ((proyectoActualizar.getConvocatoriaId() == null && data.getConvocatoriaId() == null)
                  || (proyectoActualizar.getConvocatoriaId() != null && data.getConvocatoriaId() != null
                      && proyectoActualizar.getConvocatoriaId().equals(data.getConvocatoriaId())))
              && ((proyectoActualizar.getSolicitudId() == null && data.getSolicitudId() == null)
                  || (proyectoActualizar.getSolicitudId() != null && data.getSolicitudId() != null
                      && proyectoActualizar.getSolicitudId().equals(data.getSolicitudId()))),
          "Existen campos del proyecto modificados que no se pueden modificar");

      data.setAcronimo(proyectoActualizar.getAcronimo());
      data.setAmbitoGeografico(proyectoActualizar.getAmbitoGeografico());
      data.setAnualidades(proyectoActualizar.getAnualidades());
      data.setClasificacionCVN(proyectoActualizar.getClasificacionCVN());
      data.setCodigoExterno(proyectoActualizar.getCodigoExterno());
      data.setColaborativo(proyectoActualizar.getColaborativo());
      data.setConfidencial(proyectoActualizar.getConfidencial());
      data.setContratos(proyectoActualizar.getContratos());
      data.setConvocatoriaExterna(proyectoActualizar.getConvocatoriaExterna());
      data.setCoordinadorExterno(proyectoActualizar.getCoordinadorExterno());
      data.setCosteHora(proyectoActualizar.getCosteHora());
      data.setFacturacion(proyectoActualizar.getFacturacion());
      data.setFechaFin(proyectoActualizar.getFechaFin());
      data.setFechaInicio(proyectoActualizar.getFechaInicio());
      data.setFinalidad(proyectoActualizar.getFinalidad());
      data.setFinalista(proyectoActualizar.getFinalista());
      data.setIva(proyectoActualizar.getIva());
      data.setLimitativo(proyectoActualizar.getLimitativo());
      data.setModeloEjecucion(proyectoActualizar.getModeloEjecucion());
      data.setObservaciones(proyectoActualizar.getObservaciones());
      data.setPermitePaquetesTrabajo(proyectoActualizar.getPermitePaquetesTrabajo());
      data.setTimesheet(proyectoActualizar.getTimesheet());
      data.setTipoHorasAnuales(proyectoActualizar.getTipoHorasAnuales());
      data.setTitulo(proyectoActualizar.getTitulo());
      data.setUnidadGestionRef(proyectoActualizar.getUnidadGestionRef());

      Proyecto returnValue = repository.save(data);
      log.debug("update(Proyecto proyecto) - end");
      return returnValue;
    }).orElseThrow(() -> new ProyectoNotFoundException(proyectoActualizar.getId()));
  }

  /**
   * Reactiva el {@link Proyecto}.
   *
   * @param id Id del {@link Proyecto}.
   * @return la entidad {@link Proyecto} persistida.
   */
  @Override
  @Transactional
  public Proyecto enable(Long id) {
    log.debug("enable(Long id) - start");

    Assert.notNull(id, "Proyecto id no puede ser null para reactivar un Proyecto");

    return repository.findById(id).map(proyecto -> {
      // TODO: Add right authority
      Assert.isTrue(SgiSecurityContextHolder.hasAuthorityForUO("CSP-PRO-C", proyecto.getUnidadGestionRef()),
          "El proyecto pertenece a una Unidad de Gestión no gestionable por el usuario");

      if (proyecto.getActivo()) {
        // Si esta activo no se hace nada
        return proyecto;
      }

      proyecto.setActivo(true);

      Proyecto returnValue = repository.save(proyecto);
      log.debug("enable(Long id) - end");
      return returnValue;
    }).orElseThrow(() -> new ProyectoNotFoundException(id));
  }

  /**
   * Desactiva el {@link Proyecto}.
   *
   * @param id Id del {@link Proyecto}.
   * @return la entidad {@link Proyecto} persistida.
   */
  @Override
  @Transactional
  public Proyecto disable(Long id) {
    log.debug("disable(Long id) - start");

    Assert.notNull(id, "Proyecto id no puede ser null para desactivar un Proyecto");

    return repository.findById(id).map(proyecto -> {
      // TODO: Add right authority
      Assert.isTrue(SgiSecurityContextHolder.hasAuthorityForUO("CSP-PRO-C", proyecto.getUnidadGestionRef()),
          "El proyecto pertenece a una Unidad de Gestión no gestionable por el usuario");

      if (!proyecto.getActivo()) {
        // Si no esta activo no se hace nada
        return proyecto;
      }

      proyecto.setActivo(false);

      Proyecto returnValue = repository.save(proyecto);
      log.debug("disable(Long id) - end");
      return returnValue;
    }).orElseThrow(() -> new ProyectoNotFoundException(id));
  }

  /**
   * Comprueba la existencia del {@link Proyecto} por id.
   *
   * @param id el id de la entidad {@link Proyecto}.
   * @return true si existe y false en caso contrario.
   */
  @Override
  public boolean existsById(final Long id) {
    log.debug("existsById(final Long id)  - start", id);
    final boolean existe = repository.existsById(id);
    log.debug("existsById(final Long id)  - end", id);
    return existe;
  }

  /**
   * Obtiene el {@link ModeloEjecucion} asignada al {@link Proyecto}.
   * 
   * @param id Id del {@link Proyecto}.
   * @return {@link ModeloEjecucion} asignado
   */
  @Override
  public ModeloEjecucion getModeloEjecucion(Long id) {
    log.debug("getModeloEjecucion(Long id) - start");

    Optional<ModeloEjecucion> returnValue = null;
    if (repository.existsById(id)) {
      returnValue = repository.getModeloEjecucion(id);
    } else {
      throw (new ProyectoNotFoundException(id));
    }
    log.debug("getModeloEjecucion(Long id) - end");
    return returnValue.isPresent() ? returnValue.get() : null;
  }

  /**
   * Obtiene una entidad {@link Proyecto} por id.
   * 
   * @param id Identificador de la entidad {@link Proyecto}.
   * @return Proyecto la entidad {@link Proyecto}.
   */
  @Override
  public Proyecto findById(Long id) {
    log.debug("findById(Long id) - start");
    final Proyecto returnValue = repository.findById(id).orElseThrow(() -> new ProyectoNotFoundException(id));
    ProyectoHelper.checkCanRead(returnValue);
    log.debug("findById(Long id) - end");
    return returnValue;
  }

  /**
   * Obtiene todas las entidades {@link Proyecto} activas paginadas y filtradas.
   *
   * @param query  información del filtro.
   * @param paging información de paginación.
   * @return el listado de entidades {@link Proyecto} activas paginadas y
   *         filtradas.
   */
  @Override
  public Page<Proyecto> findAllRestringidos(String query, Pageable paging) {
    log.debug("findAll(String query, Pageable paging) - start");

    Specification<Proyecto> specs = ProyectoSpecifications.activos()
        .and(SgiRSQLJPASupport.toSpecification(query, ProyectoPredicateResolver.getInstance(programaRepository)));

    // TODO: Add right authority
    // No tiene acceso a todos los UO
    if (!SgiSecurityContextHolder.hasAuthority("CSP-PRO-C")) {
      Specification<Proyecto> specByUnidadGestionRefIn = ProyectoSpecifications
          .unidadGestionRefIn(SgiSecurityContextHolder.getUOsForAuthority("CSP-PRO-C"));
      specs = specs.and(specByUnidadGestionRefIn);
    }

    Page<Proyecto> returnValue = repository.findAll(specs, paging);
    log.debug("findAll(String query, Pageable paging) - end");
    return returnValue;
  }

  /**
   * Obtiene todas las entidades {@link Proyecto} paginadas y filtradas.
   *
   * @param query  información del filtro.
   * @param paging información de paginación.
   * @return el listado de entidades {@link Proyecto} paginadas y filtradas.
   */
  @Override
  public Page<Proyecto> findAllTodosRestringidos(String query, Pageable paging) {
    log.debug("findAll(String query, Pageable paging) - start");

    Specification<Proyecto> specs = SgiRSQLJPASupport.toSpecification(query);

    // TODO: Add right authority
    // No tiene acceso a todos los UO
    if (!SgiSecurityContextHolder.hasAuthority("CSP-PRO-C")) {
      Specification<Proyecto> specByUnidadGestionRefIn = ProyectoSpecifications
          .unidadGestionRefIn(SgiSecurityContextHolder.getUOsForAuthority("CSP-PRO-C"));
      specs = specs.and(specByUnidadGestionRefIn);
    }

    // TODO implementar buscador avanzado

    Page<Proyecto> returnValue = repository.findAll(specs, paging);
    log.debug("findAll(String query, Pageable paging) - end");
    return returnValue;
  }

  /**
   * Añade el nuevo {@link EstadoProyecto} y actualiza la {@link Proyecto} con
   * dicho estado.
   * 
   * @param proyecto           la {@link Proyecto} para la que se añade el nuevo
   *                           estado.
   * @param tipoEstadoProyecto El nuevo {@link EstadoProyecto.Estado} de la
   *                           {@link Proyecto}.
   * @return la {@link Proyecto} con el estado actualizado.
   */
  private EstadoProyecto addEstadoProyecto(Proyecto proyecto, EstadoProyecto.Estado tipoEstadoProyecto,
      String comentario) {
    log.debug(
        "addEstadoProyecto(Proyecto proyecto, TipoEstadoProyectoEnum tipoEstadoProyecto, String comentario) - start");

    EstadoProyecto estadoProyecto = new EstadoProyecto();
    estadoProyecto.setEstado(tipoEstadoProyecto);
    estadoProyecto.setProyectoId(proyecto.getId());
    estadoProyecto.setComentario(comentario);
    estadoProyecto.setFechaEstado(Instant.now());

    EstadoProyecto returnValue = estadoProyectoRepository.save(estadoProyecto);

    log.debug(
        "addEstadoProyecto(Proyecto proyecto, TipoEstadoProyectoEnum tipoEstadoProyecto, String comentario) - end");
    return returnValue;
  }

  /**
   * Se comprueba que los datos a guardar cumplan las validaciones oportunas
   * 
   * @param proyecto          datos del proyecto
   * @param unidadGestionRefs las unidades de gestión del usuario
   * 
   */
  private void validarDatos(Proyecto proyecto) {
    if (proyecto.getConvocatoriaId() != null) {
      Assert.isTrue(convocatoriaRepository.existsById(proyecto.getConvocatoriaId()),
          "La convocatoria con id '" + proyecto.getConvocatoriaId() + "' no existe");
    }

    if (proyecto.getFechaInicio() != null && proyecto.getFechaFin() != null) {
      Assert.isTrue(proyecto.getFechaFin().isAfter(proyecto.getFechaInicio()),
          "La fecha de fin debe ser posterior a la fecha de inicio");
    }

    // ModeloEjecucion correcto
    Optional<ModeloUnidad> modeloUnidad = modeloUnidadRepository.findByModeloEjecucionIdAndUnidadGestionRef(
        proyecto.getModeloEjecucion().getId(), proyecto.getUnidadGestionRef());

    Assert.isTrue(modeloUnidad.isPresent(), "ModeloEjecucion '" + proyecto.getModeloEjecucion().getNombre()
        + "' no disponible para la UnidadGestion " + proyecto.getUnidadGestionRef());

    if (proyecto.getCosteHora() != null && proyecto.getCosteHora()) {
      Assert.isTrue(proyecto.getTimesheet() != null && proyecto.getTimesheet(), "El proyecto requiere timesheet");
      Assert.isTrue(proyecto.getTipoHorasAnuales() != null,
          "El campo tipoHorasAnuales debe ser obligatorio para el proyecto");
    }

    // Validación de campos obligatorios según estados. Solo aplicaría en el
    // actualizar ya que en el crear el estado siempre será "Borrador"
    if (proyecto.getEstado() != null && proyecto.getEstado().getEstado() == EstadoProyecto.Estado.ABIERTO) {
      // En la validación del crear no pasará por aquí, aún no tendrá estado.
      Assert.isTrue(proyecto.getFinalidad() != null,
          "El campo finalidad debe ser obligatorio para el proyecto en estado 'Abierto'");

      Assert.isTrue(proyecto.getAmbitoGeografico() != null,
          "El campo ambitoGeografico debe ser obligatorio para el proyecto en estado 'Abierto'");

      Assert.isTrue(proyecto.getConfidencial() != null,
          "El campo confidencial debe ser obligatorio para el proyecto en estado 'Abierto'");

      Assert.isTrue(proyecto.getColaborativo() != null,
          "El campo colaborativo debe ser obligatorio para el proyecto en estado 'Abierto'");

      Assert.isTrue(proyecto.getCoordinadorExterno() != null,
          "El campo coordinadorExterno debe ser obligatorio para el proyecto en estado 'Abierto'");

      Assert.isTrue(
          proyecto.getEstado().getEstado() == EstadoProyecto.Estado.ABIERTO && proyecto.getTimesheet() != null,
          "El campo timesheet debe ser obligatorio para el proyecto en estado 'Abierto'");

      Assert.isTrue(proyecto.getPermitePaquetesTrabajo() != null,
          "El campo permitePaquetesTrabajo debe ser obligatorio para el proyecto en estado 'Abierto'");

      Assert.isTrue(proyecto.getCosteHora() != null,
          "El campo costeHora debe ser obligatorio para el proyecto en estado 'Abierto'");

      Assert.isTrue(proyecto.getContratos() != null,
          "El campo contratos debe ser obligatorio para el proyecto en estado 'Abierto'");

      Assert.isTrue(proyecto.getFacturacion() != null,
          "El campo facturacion debe ser obligatorio para el proyecto en estado 'Abierto'");

      Assert.isTrue(proyecto.getIva() != null,
          "El campo iva debe ser obligatorio para el proyecto en estado 'Abierto'");
    }
  }

  /**
   * Copia todos los datos de la {@link Convocatoria} al {@link Proyecto}
   * 
   * @param proyecto la entidad {@link Proyecto}
   */
  private void copyDatosConvocatoriaToProyecto(Proyecto proyecto) {
    this.copyEntidadesFinanciadoras(proyecto.getId(), proyecto.getConvocatoriaId());
    this.copyEntidadesGestoras(proyecto);
    this.copyEntidadesConvocantesDeConvocatoria(proyecto.getId(), proyecto.getConvocatoriaId());
    this.copyAreaTematica(proyecto);
    this.copyPeriodoSeguimiento(proyecto);
    this.copyConfiguracionEconomica(proyecto);
  }

  /**
   * Copia la informaci&oacute;n de EntidadesConvocantes de la Convocatoria en el
   * Proyecto
   * 
   * @param proyecto     el {@link Proyecto}
   * @param convocatoria la {@link Convocatoria}
   */
  private void copyEntidadesConvocantesDeConvocatoria(Long proyectoId, Long convocatoriaId) {
    log.debug("copiarEntidadesConvocatesDeConvocatoria(Proyecto proyecto, Convocatoria convocatoria) - start");
    Specification<ConvocatoriaEntidadConvocante> specByConvocatoriaId = ConvocatoriaEntidadConvocanteSpecifications
        .byConvocatoriaId(convocatoriaId);

    List<ConvocatoriaEntidadConvocante> convocatoriaEntidadConvocantes = convocatoriaEntidadConvocanteRepository
        .findAll(specByConvocatoriaId);

    for (ConvocatoriaEntidadConvocante convocatoriaEntidadConvocante : convocatoriaEntidadConvocantes) {
      if (proyectoEntidadConvocanteService.existsByProyectoIdAndEntidadRef(proyectoId,
          convocatoriaEntidadConvocante.getEntidadRef())) {
        ProyectoEntidadConvocante proyectoEntidadConvocante = proyectoEntidadConvocanteService
            .findByProyectoIdAndEntidadRef(proyectoId, convocatoriaEntidadConvocante.getEntidadRef());
        proyectoEntidadConvocante.setProyectoId(proyectoId);
        proyectoEntidadConvocante.setEntidadRef(convocatoriaEntidadConvocante.getEntidadRef());
        proyectoEntidadConvocante.setProgramaConvocatoria(convocatoriaEntidadConvocante.getPrograma());
        proyectoEntidadConvocanteService.update(proyectoEntidadConvocante);
      } else {
        ProyectoEntidadConvocante proyectoEntidadConvocante = new ProyectoEntidadConvocante();
        proyectoEntidadConvocante.setProyectoId(proyectoId);
        proyectoEntidadConvocante.setEntidadRef(convocatoriaEntidadConvocante.getEntidadRef());
        proyectoEntidadConvocante.setProgramaConvocatoria(convocatoriaEntidadConvocante.getPrograma());
        proyectoEntidadConvocanteService.create(proyectoEntidadConvocante);
      }
    }

    log.debug("copiarEntidadesConvocatesDeConvocatoria(Proyecto proyecto, Convocatoria convocatoria) - end");
  }

  /**
   * Copia la entidad área temática de una convocatoria a unproyecto
   * 
   * @param proyecto la entidad {@link Proyecto}
   */
  private void copyAreaTematica(Proyecto proyecto) {

    // si en la convocatoria se ha rellenado "ConvocatoriaAreaTematica" se rellenará
    // el campo "areaTematicaConvocatoria" de la tabla "ContextoProyecto" con el
    // campo "areaTematica" de la tabla "ConvocatoriaAreaTematica" de la
    // convocatoria, dejando vacío el campo "areaTematica" de la tabla
    // "ContextoProyecto" para que lo pueda seleccionar el usuario.
    Optional<ConvocatoriaAreaTematica> convocatoriaAreaTematica = convocatoriaAreaTematicaRepository
        .findByConvocatoriaId(proyecto.getConvocatoriaId());

    if (convocatoriaAreaTematica.isPresent()) {
      if (!contextoProyectoService.existsByProyecto(proyecto.getId())) {
        ContextoProyecto contextoProyectoNew = new ContextoProyecto();
        contextoProyectoNew.setProyectoId(proyecto.getId());
        contextoProyectoNew.setAreaTematicaConvocatoria(convocatoriaAreaTematica.get().getAreaTematica());
        contextoProyectoService.create(contextoProyectoNew);
      } else {
        ContextoProyecto contextoProyectoUpdate = contextoProyectoService.findByProyecto(proyecto.getId());
        contextoProyectoUpdate.setAreaTematicaConvocatoria(convocatoriaAreaTematica.get().getAreaTematica());
        contextoProyectoService.update(contextoProyectoUpdate, proyecto.getId());
      }

    }
  }

  /**
   * Copia las entidades financiadores de una convocatoria a un proyecto
   * 
   * @param proyectoId     Identificador del proyecto de destino
   * @param convocatoriaId Identificador de la convocatoria
   */
  private void copyEntidadesFinanciadoras(Long proyectoId, Long convocatoriaId) {
    log.debug("copyEntidadesFinanciadoras(Long proyectoId, Long convocatoriaId) - start");
    List<ConvocatoriaEntidadFinanciadora> entidadesConvocatoria = convocatoriaEntidadFinanciadoraRepository
        .findAllByConvocatoriaId(convocatoriaId);
    entidadesConvocatoria.stream().forEach((entidadConvocatoria) -> {
      log.debug("Copy ConvocatoriaEntidadFinanciadora with id: {0}", entidadConvocatoria.getId());
      ProyectoEntidadFinanciadora entidadProyecto = new ProyectoEntidadFinanciadora();
      entidadProyecto.setProyectoId(proyectoId);
      entidadProyecto.setEntidadRef(entidadConvocatoria.getEntidadRef());
      entidadProyecto.setFuenteFinanciacion(entidadConvocatoria.getFuenteFinanciacion());
      entidadProyecto.setTipoFinanciacion(entidadConvocatoria.getTipoFinanciacion());
      entidadProyecto.setPorcentajeFinanciacion(entidadConvocatoria.getPorcentajeFinanciacion());
      entidadProyecto.setAjena(DEFAULT_COPY_ENTIDAD_FINANCIADORA_AJENA_VALUE);

      this.proyectoEntidadFinanciadoraService.create(entidadProyecto);
    });
    log.debug("copyEntidadesFinanciadoras(Long proyectoId, Long convocatoriaId) - end");
  }

  /**
   * Copia los periodos de seguimiento de una convocatoria a un proyecto
   * 
   * @param proyecto El proyecto de destino
   */
  private void copyPeriodoSeguimiento(Proyecto proyecto) {
    log.debug("copyPeriodoSeguimiento(Proyecto proyecto) - start");
    List<ConvocatoriaPeriodoSeguimientoCientifico> listadoConvocatoriaSeguimiento = convocatoriaPeriodoSeguimientoCientificoRepository
        .findAllByConvocatoriaIdOrderByMesInicial(proyecto.getConvocatoriaId());
    listadoConvocatoriaSeguimiento.stream().forEach((convocatoriaSeguimiento) -> {
      log.debug("Copy ConvocatoriaPeriodoSeguimientoCientifico with id: {0}", convocatoriaSeguimiento.getId());
      ProyectoPeriodoSeguimiento entidadProyecto = new ProyectoPeriodoSeguimiento();
      entidadProyecto.setNumPeriodo(convocatoriaSeguimiento.getNumPeriodo());
      entidadProyecto.setProyectoId(proyecto.getId());
      entidadProyecto.setFechaInicio(Instant.from(proyecto.getFechaInicio().atZone(ZoneOffset.UTC)
          .plus(Period.ofMonths(convocatoriaSeguimiento.getMesInicial() - 1))));
      entidadProyecto.setFechaFin(Instant.from(proyecto.getFechaInicio().atZone(ZoneOffset.UTC)
          .plus(Period.ofMonths(convocatoriaSeguimiento.getMesFinal() - 1))));
      if (convocatoriaSeguimiento.getFechaInicioPresentacion() != null) {
        entidadProyecto.setFechaInicioPresentacion(convocatoriaSeguimiento.getFechaInicioPresentacion());
      }
      if (convocatoriaSeguimiento.getFechaFinPresentacion() != null) {
        entidadProyecto.setFechaFinPresentacion(convocatoriaSeguimiento.getFechaFinPresentacion());
      }
      entidadProyecto.setObservaciones(convocatoriaSeguimiento.getObservaciones());

      this.proyectoPeriodoSeguimientoService.create(entidadProyecto);
    });
    log.debug("copyPeriodoSeguimiento(Proyecto proyecto) - end");
  }

  /**
   * Copia las entidades gestoras de una convocatoria a un proyecto
   * 
   * @param proyecto la entidad {@link Proyecto}
   */
  private void copyEntidadesGestoras(Proyecto proyecto) {
    log.debug("copyEntidadesGestoras(Long proyectoId, Long convocatoriaId) - start");
    List<ConvocatoriaEntidadGestora> entidadesConvocatoria = convocatoriaEntidadGestoraRepository
        .findAllByConvocatoriaId(proyecto.getConvocatoriaId());
    entidadesConvocatoria.stream().forEach((entidadConvocatoria) -> {
      log.debug("Copy copyEntidadesGestoras with id: {0}", entidadConvocatoria.getId());
      ProyectoEntidadGestora entidadProyecto = new ProyectoEntidadGestora();
      entidadProyecto.setProyectoId(proyecto.getId());
      entidadProyecto.setEntidadRef(entidadConvocatoria.getEntidadRef());
      this.proyectoEntidadGestoraService.create(entidadProyecto);
    });
    log.debug("copyEntidadesGestoras(Long proyectoId, Long convocatoriaId) - end");
  }

  /**
   * Copia los datos generales de la {@link Solicitud} al {@link Proyecto}
   * 
   * @param proyecto la entidad {@link Proyecto}
   * @return la entidad {@link Proyecto} con los nuevos datos
   */
  private Proyecto copyDatosGeneralesSolicitudToProyecto(Proyecto proyecto, Solicitud solicitud,
      SolicitudProyecto solicitudProyecto) {
    log.debug(
        "copyDatosGenerales(Proyecto proyecto, Solicitud solicitud, SolicitudProyecto solicitudProyecto) - start");
    proyecto.setSolicitudId(solicitud.getId());
    proyecto.setConvocatoriaId(solicitud.getConvocatoriaId());
    proyecto.setTitulo(solicitudProyecto.getTitulo());
    proyecto.setAcronimo(solicitudProyecto.getAcronimo());
    proyecto.setUnidadGestionRef(solicitud.getUnidadGestionRef());
    proyecto.setCodigoExterno(solicitudProyecto.getCodExterno());
    if (solicitud.getConvocatoriaId() != null) {
      Convocatoria convocatoria = convocatoriaRepository.findById(solicitud.getConvocatoriaId())
          .orElseThrow(() -> new ConvocatoriaNotFoundException(solicitud.getConvocatoriaId()));
      proyecto.setFinalidad(convocatoria.getFinalidad());
      proyecto.setAmbitoGeografico(convocatoria.getAmbitoGeografico());
    } else {
      proyecto.setConvocatoriaExterna(solicitud.getConvocatoriaExterna());
    }
    proyecto.setColaborativo(solicitudProyecto.getColaborativo());
    proyecto.setCoordinadorExterno(solicitudProyecto.getCoordinadorExterno());
    log.debug("copyDatosGenerales(Proyecto proyecto, Solicitud solicitud, SolicitudProyecto solicitudProyecto) - end");
    return proyecto;
  }

  /**
   * Copia todos los datos de la {@link Solicitud} al {@link Proyecto}
   * 
   * @param proyecto          la entidad {@link Proyecto}
   * @param solicitudProyecto la entidad {@link SolicitudProyecto}
   */
  private void copyDatosSolicitudToProyecto(Proyecto proyecto, Solicitud solicitud,
      SolicitudProyecto solicitudProyecto) {
    log.debug(
        "copyDatosSolicitudToProyecto(Proyecto proyecto, Solicitud solicitud, SolicitudProyecto solicitudProyecto) - start");
    this.copyContexto(proyecto, solicitud, solicitudProyecto);
    this.copyAreasConocimiento(proyecto);
    this.copyCodigosUNESCO(proyecto);
    this.copyCodigosNABS(proyecto);
    this.copyCodigosCNAE(proyecto);
    this.copyEntidadesConvocantesDeSolicitud(proyecto);
    this.copyEntidadesFinanciadorasDeSolicitud(proyecto, solicitudProyecto.getId());
    this.copyMiembrosEquipo(proyecto, solicitudProyecto.getId());
    this.copySocios(proyecto, solicitudProyecto.getId());
    log.debug(
        "copyDatosSolicitudToProyecto(Proyecto proyecto, Solicitud solicitud, SolicitudProyecto solicitudProyecto) - end");
  }

  /**
   * Copia el los datos {@link ContextoProyecto} de la entidad
   * {@link SolicitudProyecto} al {@link Proyecto}
   * 
   * @param proyecto          la entidad {@link Proyecto}
   * @param solicitudProyecto la entidad {@link SolicitudProyecto}
   * @return la entidad {@link Proyecto} con los nuevos datos
   */
  private void copyContexto(Proyecto proyecto, Solicitud solicitud, SolicitudProyecto solicitudProyecto) {
    log.debug("copyContexto(Proyecto proyecto, Solicitud solicitud, SolicitudProyecto solicitudProyecto) - start");
    ContextoProyecto contextoProyectoNew = new ContextoProyecto();
    contextoProyectoNew.setProyectoId(proyecto.getId());
    contextoProyectoNew.setObjetivos(solicitudProyecto.getObjetivos());
    contextoProyectoNew.setResultadosPrevistos(solicitudProyecto.getResultadosPrevistos());
    contextoProyectoNew.setIntereses(solicitudProyecto.getIntereses());
    contextoProyectoNew.setAreaTematica(solicitudProyecto.getAreaTematica());

    if (solicitud.getConvocatoriaId() != null) {
      Optional<ConvocatoriaAreaTematica> convocatoriaAreaTematica = convocatoriaAreaTematicaRepository
          .findByConvocatoriaId(solicitud.getConvocatoriaId());

      if (convocatoriaAreaTematica.isPresent()) {
        contextoProyectoNew.setAreaTematicaConvocatoria(convocatoriaAreaTematica.get().getAreaTematica());
      }
    }

    contextoProyectoService.create(contextoProyectoNew);
    log.debug("copyContexto(Proyecto proyecto, Solicitud solicitud, SolicitudProyecto solicitudProyecto) - end");
  }

  private void copyAreasConocimiento(Proyecto proyecto) {
    log.debug("copyAreasConocimiento(Proyecto proyecto) - start");
    // TODO Se copian de las áreas definidas en la solicitud, por cada registro en
    // la tabla "SolicitudProyectoArea" se creará un registro en la tabla
    // "ProyectoArea" con la área indicada en la solicitud.
    log.debug("copyAreasConocimiento(Proyecto proyecto) - end");
  }

  private void copyCodigosUNESCO(Proyecto proyecto) {
    log.debug("copyAreasConocimiento(Proyecto proyecto) - start");
    // TODO Se copian de los códigos UNESCO definidos en la solicitud, por cada
    // registro en la tabla "SolicitudProyectoUnesco" se creará un registro en
    // la tabla "ProyectoUnesco" con él código indicado en la solicitud.
    log.debug("copyAreasConocimiento(Proyecto proyecto) - end");
  }

  private void copyCodigosNABS(Proyecto proyecto) {
    log.debug("copyAreasConocimiento(Proyecto proyecto) - start");
    // TODO Se copian de los códigos NABS definidos en la solicitud, por cada
    // registro en la tabla "SolicitudProyectoNabs" se creará un registro en la
    // tabla "ProyectoNabs" con él código indicado en la solicitud.
    log.debug("copyAreasConocimiento(Proyecto proyecto) - end");
  }

  private void copyCodigosCNAE(Proyecto proyecto) {
    log.debug("copyAreasConocimiento(Proyecto proyecto) - start");
    // TODO Se copian de los códigos CNAE definidos en la solicitud, por cada
    // registro en la tabla "SolicitudProyectoCnae" se creará un registro en la
    // tabla "ProyectoCnae" con él código indicado en la solicitud.
    log.debug("copyAreasConocimiento(Proyecto proyecto) - end");
  }

  /**
   * Copia las entidades convocantes de una {@link Solicitud} a un
   * {@link Proyecto}
   * 
   * @param proyecto entidad {@link Proyecto}
   */
  private void copyEntidadesConvocantesDeSolicitud(Proyecto proyecto) {
    log.debug("copyEntidadesConvocantesDeSolicitud(Proyecto proyecto) - start");
    List<SolicitudModalidad> entidadesSolicitud = solicitudModalidadRepository
        .findAllBySolicitudId(proyecto.getSolicitudId());
    entidadesSolicitud.stream().forEach((entidadSolicitud) -> {
      log.debug("Copy SolicitudModalidad with id: {0}", entidadSolicitud.getId());
      ProyectoEntidadConvocante entidadProyecto = new ProyectoEntidadConvocante();
      entidadProyecto.setProyectoId(proyecto.getId());
      entidadProyecto.setPrograma(entidadSolicitud.getPrograma());
      entidadProyecto.setEntidadRef(entidadSolicitud.getEntidadRef());

      this.proyectoEntidadConvocanteService.create(entidadProyecto);
    });
    log.debug("copyEntidadesConvocantesDeSolicitud(Proyecto proyecto) - end");
  }

  /**
   * Copia las entidades financiadoras de una {@link Solicitud} a un
   * {@link Proyecto}
   * 
   * @param proyecto entidad {@link Proyecto}
   */
  private void copyEntidadesFinanciadorasDeSolicitud(Proyecto proyecto, Long solicitudProyectoId) {
    log.debug("copyEntidadesFinanciadorasDeSolicitud(Proyecto proyecto, Long solicitudProyectoId) - start");
    List<SolicitudProyectoEntidadFinanciadoraAjena> entidadesSolicitud = solicitudProyectoEntidadFinanciadoraAjenaRepository
        .findAllBySolicitudProyectoId(solicitudProyectoId);
    entidadesSolicitud.stream().forEach((entidadSolicitud) -> {
      log.debug("Copy SolicitudProyectoEntidadFinanciadoraAjena with id: {0}", entidadSolicitud.getId());
      ProyectoEntidadFinanciadora entidadProyecto = new ProyectoEntidadFinanciadora();
      entidadProyecto.setProyectoId(proyecto.getId());
      entidadProyecto.setEntidadRef(entidadSolicitud.getEntidadRef());
      entidadProyecto.setFuenteFinanciacion(entidadSolicitud.getFuenteFinanciacion());
      entidadProyecto.setTipoFinanciacion(entidadSolicitud.getTipoFinanciacion());
      entidadProyecto.setPorcentajeFinanciacion(entidadSolicitud.getPorcentajeFinanciacion());
      entidadProyecto.setAjena(Boolean.TRUE);

      this.proyectoEntidadFinanciadoraService.create(entidadProyecto);
    });
    log.debug("copyEntidadesFinanciadorasDeSolicitud(Proyecto proyecto, Long solicitudProyectoId) - end");
  }

  /**
   * Copia todos los miembros del equipo de una {@link Solicitud} a un
   * {@link Proyecto}
   * 
   * @param proyecto entidad {@link Proyecto}
   */
  private void copyMiembrosEquipo(Proyecto proyecto, Long solicitudProyectoId) {
    log.debug("copyMiembrosEquipo(Proyecto proyecto) - start");
    List<SolicitudProyectoEquipo> entidadesSolicitud = solicitudEquipoRepository
        .findAllBySolicitudProyectoId(solicitudProyectoId);
    List<ProyectoEquipo> proyectoEquipos = new ArrayList<ProyectoEquipo>();
    entidadesSolicitud.stream().forEach((entidadSolicitud) -> {
      log.debug("Copy SolicitudProyectoEquipo with id: {0}", entidadSolicitud.getId());
      ProyectoEquipo proyectoEquipo = new ProyectoEquipo();
      proyectoEquipo.setProyectoId(proyecto.getId());
      if (entidadSolicitud.getMesInicio() != null && entidadSolicitud.getMesFin() != null) {
        proyectoEquipo.setFechaInicio(Instant.from(proyecto.getFechaInicio().atZone(ZoneOffset.UTC)
            .plus(Period.ofMonths(entidadSolicitud.getMesInicio() - 1))));
        proyectoEquipo.setFechaFin(Instant.from(
            proyecto.getFechaInicio().atZone(ZoneOffset.UTC).plus(Period.ofMonths(entidadSolicitud.getMesFin() - 1))));
      }
      proyectoEquipo.setRolProyecto(entidadSolicitud.getRolProyecto());
      proyectoEquipo.setPersonaRef(entidadSolicitud.getPersonaRef());
      proyectoEquipos.add(proyectoEquipo);
    });
    this.proyectoEquipoService.update(proyecto.getId(), proyectoEquipos);
    log.debug("copyMiembrosEquipo(Proyecto proyecto) - end");
  }

  /**
   * Copia todos los socios de una {@link Solicitud} a un {@link Proyecto}
   * 
   * @param proyecto entidad {@link Proyecto}
   */
  private void copySocios(Proyecto proyecto, Long solicitudProyectoId) {
    log.debug("copySocios(Proyecto proyecto) - start");
    List<SolicitudProyectoSocio> entidadesSolicitud = solicitudSocioRepository
        .findAllBySolicitudProyectoId(solicitudProyectoId);
    entidadesSolicitud.stream().forEach((entidadSolicitud) -> {
      log.debug("Copy SolicitudProyectoSocio with id: {0}", entidadSolicitud.getId());
      ProyectoSocio proyectoSocio = new ProyectoSocio();
      proyectoSocio.setProyectoId(proyecto.getId());
      proyectoSocio.setFechaInicio(Instant.from(
          proyecto.getFechaInicio().atZone(ZoneOffset.UTC).plus(Period.ofMonths(entidadSolicitud.getMesInicio() - 1))));
      proyectoSocio.setFechaFin(Instant.from(
          proyecto.getFechaInicio().atZone(ZoneOffset.UTC).plus(Period.ofMonths(entidadSolicitud.getMesFin() - 1))));
      proyectoSocio.setRolSocio(entidadSolicitud.getRolSocio());
      proyectoSocio.setEmpresaRef(entidadSolicitud.getEmpresaRef());
      proyectoSocio.setImporteConcedido(entidadSolicitud.getImporteSolicitado());
      proyectoSocio.setNumInvestigadores(entidadSolicitud.getNumInvestigadores());
      ProyectoSocio proyectoSocioCreado = this.proyectoSocioService.create(proyectoSocio);

      // ProyectoSocioEquipo
      List<SolicitudProyectoSocioEquipo> entidadesEquipoSolicitud = solicitudEquipoSocioRepository
          .findAllBySolicitudProyectoSocioId(entidadSolicitud.getId());

      List<ProyectoSocioEquipo> proyectoSocioEquipos = new ArrayList<ProyectoSocioEquipo>();
      entidadesEquipoSolicitud.stream().forEach((entidadEquipoSolicitud) -> {
        log.debug("Copy SolicitudProyectoSocioEquipo with id: {0}", entidadEquipoSolicitud.getId());
        ProyectoSocioEquipo proyectoSocioEquipo = new ProyectoSocioEquipo();
        proyectoSocioEquipo.setFechaInicio(Instant.from(proyecto.getFechaInicio().atZone(ZoneOffset.UTC)
            .plus(Period.ofMonths(entidadEquipoSolicitud.getMesInicio() - 1))));
        proyectoSocioEquipo.setFechaFin(Instant.from(proyecto.getFechaInicio().atZone(ZoneOffset.UTC)
            .plus(Period.ofMonths(entidadEquipoSolicitud.getMesFin() - 1))));
        proyectoSocioEquipo.setPersonaRef(entidadEquipoSolicitud.getPersonaRef());
        proyectoSocioEquipo.setRolProyecto(entidadEquipoSolicitud.getRolProyecto());

        proyectoSocioEquipos.add(proyectoSocioEquipo);
      });
      this.proyectoEquipoSocioService.update(proyectoSocio.getId(), proyectoSocioEquipos);

      // ProyectoSocioPeriodoPago
      List<SolicitudProyectoSocioPeriodoPago> entidadesPeriodoPagoSolicitud = solicitudPeriodoPagoRepository
          .findAllBySolicitudProyectoSocioId(entidadSolicitud.getId());

      List<ProyectoSocioPeriodoPago> proyectoSocioPeriodoPagos = new ArrayList<ProyectoSocioPeriodoPago>();
      entidadesPeriodoPagoSolicitud.stream().forEach((entidadPeriodoPagoSolicitud) -> {
        log.debug("Copy ProyectoSocioPeriodoPago with id: {0}", entidadPeriodoPagoSolicitud.getId());
        ProyectoSocioPeriodoPago proyectoSocioPeriodoPago = new ProyectoSocioPeriodoPago();
        proyectoSocioPeriodoPago.setFechaPrevistaPago(Instant.from(proyecto.getFechaInicio().atZone(ZoneOffset.UTC)
            .plus(Period.ofMonths(entidadPeriodoPagoSolicitud.getMes() - 1))));
        proyectoSocioPeriodoPago.setImporte(entidadPeriodoPagoSolicitud.getImporte());
        proyectoSocioPeriodoPago.setNumPeriodo(entidadPeriodoPagoSolicitud.getNumPeriodo());

        proyectoSocioPeriodoPagos.add(proyectoSocioPeriodoPago);

      });
      this.proyectoSocioPeriodoPagoService.update(proyectoSocio.getId(), proyectoSocioPeriodoPagos);

      // ProyectoSocioPeriodoJustificacion
      List<SolicitudProyectoSocioPeriodoJustificacion> entidadesPeriodoJustificacionSolicitud = solicitudPeriodoJustificacionRepository
          .findAllBySolicitudProyectoSocioId(entidadSolicitud.getId());

      entidadesPeriodoJustificacionSolicitud.stream().forEach((entidadPeriodoJustificacionSolicitud) -> {
        log.debug("Copy ProyectoSocioPeriodoJustificacion with id: {0}", entidadPeriodoJustificacionSolicitud.getId());
        ProyectoSocioPeriodoJustificacion proyectoSocioPeriodoJustificacion = new ProyectoSocioPeriodoJustificacion();
        proyectoSocioPeriodoJustificacion.setProyectoSocioId(proyectoSocioCreado.getId());
        proyectoSocioPeriodoJustificacion.setFechaInicio(Instant.from(proyecto.getFechaInicio().atZone(ZoneOffset.UTC)
            .plus(Period.ofMonths(entidadPeriodoJustificacionSolicitud.getMesInicial() - 1))));
        proyectoSocioPeriodoJustificacion.setFechaFin(Instant.from(proyecto.getFechaInicio().atZone(ZoneOffset.UTC)
            .plus(Period.ofMonths(entidadPeriodoJustificacionSolicitud.getMesFinal() - 1))));
        proyectoSocioPeriodoJustificacion.setNumPeriodo(entidadPeriodoJustificacionSolicitud.getNumPeriodo());
        proyectoSocioPeriodoJustificacion.setObservaciones(entidadPeriodoJustificacionSolicitud.getObservaciones());
        proyectoSocioPeriodoJustificacion
            .setFechaInicioPresentacion(entidadPeriodoJustificacionSolicitud.getFechaInicio());
        proyectoSocioPeriodoJustificacion.setFechaFinPresentacion(entidadPeriodoJustificacionSolicitud.getFechaFin());

        this.proyectoSocioPeriodoJustificacionService.create(proyectoSocioPeriodoJustificacion);
      });

    });
    log.debug("copySocios(Proyecto proyecto) - end");
  }

  /**
   * Copia toda la configuración económica de una {@link Convocatoria} a un
   * {@link Proyecto}
   * 
   * @param proyecto entidad {@link Proyecto}
   */
  private void copyConfiguracionEconomica(Proyecto proyecto) {
    // TODO cuando se implemente ProyectoConceptoGasto
    log.debug("copyConfiguracionEconomica(Proyecto proyecto) - start");
    List<ConvocatoriaConceptoGasto> entidadesConvocatoria = convocatoriaConceptoGastoRepository
        .findAllByConvocatoriaIdAndConceptoGastoActivoTrue(proyecto.getConvocatoriaId());

    entidadesConvocatoria.stream().forEach((entidadSolicitud) -> {
      log.debug("Copy ConvocatoriaConceptoGasto with id: {0}", entidadSolicitud.getId());

    });
    log.debug("copyConfiguracionEconomica(Proyecto proyecto) - end");
  }

  /**
   * Se comprueba que los datos de la {@link Solicitud} a copiar para crear el
   * {@link Proyecto} cumplan las validaciones oportunas
   * 
   * @param solicitud datos de la {@link Solicitud}
   * 
   */
  private void validarDatosSolicitud(Solicitud solicitud) {
    Assert.isTrue(solicitud.getEstado().getEstado() == EstadoSolicitud.Estado.CONCECIDA,
        "La solicitud debe estar en estado " + EstadoSolicitud.Estado.CONCECIDA);

    Assert.isTrue(!repository.existsBySolicitudId(solicitud.getId()),
        "La solicitud con id: " + solicitud.getId() + " ya está asociada a un proyecto");

    Assert.isTrue(solicitud.getFormularioSolicitud() == FormularioSolicitud.ESTANDAR,
        "El formulario de la solicitud debe ser de tipo " + FormularioSolicitud.ESTANDAR);
  }

  /**
   * Guarda la entidad {@link Proyecto} a partir de los datos de la entidad
   * {@link Solicitud}.
   * 
   * @param solicitudId identificador de la entidad {@link Solicitud} a copiar
   *                    datos.
   * @param proyecto    datos necesarios para crear el {@link Proyecto}
   * @return proyecto la entidad {@link Proyecto} persistida.
   */
  @Override
  @Transactional
  public Proyecto createProyectoBySolicitud(Long solicitudId, Proyecto proyecto) {
    log.debug("createProyectoBySolicitud(Long solicitudId, Proyecto proyecto) - start");
    Assert.isNull(proyecto.getId(), "Proyecto id tiene que ser null para crear un Proyecto");

    Solicitud solicitud = solicitudRepository.findById(solicitudId)
        .orElseThrow(() -> new SolicitudNotFoundException(solicitudId));

    this.validarDatosSolicitud(solicitud);

    SolicitudProyecto solicitudProyecto = solicitudProyectoRepository.findBySolicitudId(solicitudId)
        .orElseThrow(() -> new SolicitudNotFoundException(solicitudId));

    proyecto = this.copyDatosGeneralesSolicitudToProyecto(proyecto, solicitud, solicitudProyecto);

    // TODO: Add right authority
    Assert.isTrue(SgiSecurityContextHolder.hasAuthorityForUO("CSP-PRO-C", proyecto.getUnidadGestionRef()),
        "La Unidad de Gestión no es gestionable por el usuario");

    this.validarDatos(proyecto);

    proyecto.setActivo(Boolean.TRUE);

    // Crea el proyecto
    repository.save(proyecto);

    // Crea el estado inicial del proyecto
    EstadoProyecto estadoProyecto = addEstadoProyecto(proyecto, EstadoProyecto.Estado.BORRADOR, null);

    proyecto.setEstado(estadoProyecto);
    // Actualiza el estado actual del proyecto con el nuevo estado
    Proyecto returnValue = repository.save(proyecto);

    this.copyDatosSolicitudToProyecto(returnValue, solicitud, solicitudProyecto);

    // Si hay asignada una convocatoria se deben de rellenar las entidades
    // correspondientes con los datos de la convocatoria
    if (proyecto.getConvocatoriaId() != null) {
      this.copyDatosConvocatoriaToProyecto(returnValue);
    }

    log.debug("createProyectoBySolicitud(Long solicitudId, Proyecto proyecto) - end");
    return returnValue;
  }

}

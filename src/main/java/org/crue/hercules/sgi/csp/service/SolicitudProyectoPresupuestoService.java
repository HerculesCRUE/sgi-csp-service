package org.crue.hercules.sgi.csp.service;

import java.util.List;

import org.crue.hercules.sgi.csp.dto.SolicitudProyectoPresupuestoTotalConceptoGasto;
import org.crue.hercules.sgi.csp.dto.SolicitudProyectoPresupuestoTotales;
import org.crue.hercules.sgi.csp.model.Solicitud;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoDatos;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoPresupuesto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface para gestionar {@link SolicitudProyectoPresupuesto}.
 */

public interface SolicitudProyectoPresupuestoService {

  /**
   * Guarda la entidad {@link SolicitudProyectoPresupuesto}.
   * 
   * @param solicitudProyectoPresupuesto la entidad
   *                                     {@link SolicitudProyectoPresupuesto} a
   *                                     guardar.
   * @return SolicitudProyectoPresupuesto la entidad
   *         {@link SolicitudProyectoPresupuesto} persistida.
   */
  SolicitudProyectoPresupuesto create(SolicitudProyectoPresupuesto solicitudProyectoPresupuesto);

  /**
   * Actualiza los datos del {@link SolicitudProyectoPresupuesto}.
   * 
   * @param solicitudProyectoPresupuesto {@link SolicitudProyectoPresupuesto} con
   *                                     los datos actualizados.
   * 
   * @return SolicitudProyectoPresupuesto {@link SolicitudProyectoPresupuesto}
   *         actualizado.
   */
  SolicitudProyectoPresupuesto update(final SolicitudProyectoPresupuesto solicitudProyectoPresupuesto);

  /**
   * Comprueba la existencia del {@link SolicitudProyectoPresupuesto} por id.
   *
   * @param id el id de la entidad {@link SolicitudProyectoPresupuesto}.
   * @return true si existe y false en caso contrario.
   */
  boolean existsById(Long id);

  /**
   * Obtiene una entidad {@link SolicitudProyectoPresupuesto} por id.
   * 
   * @param id Identificador de la entidad {@link SolicitudProyectoPresupuesto}.
   * @return SolicitudProyectoPresupuesto la entidad
   *         {@link SolicitudProyectoPresupuesto}.
   */
  SolicitudProyectoPresupuesto findById(final Long id);

  /**
   * Elimina el {@link SolicitudProyectoPresupuesto}.
   *
   * @param id Id del {@link SolicitudProyectoPresupuesto}.
   */
  void delete(Long id);

  /**
   * Recupera la lista paginada de {@link SolicitudProyectoPresupuesto} de una
   * {@link Solicitud}.
   * 
   * @param solicitudId Identificador de la {@link Solicitud}.
   * @param query       parámentros de búsqueda.
   * @param paging      parámetros de paginación.
   * @return lista paginada.
   */
  Page<SolicitudProyectoPresupuesto> findAllBySolicitud(Long solicitudId, String query, Pageable paging);

  /**
   * Recupera la lista paginada de {@link SolicitudProyectoPresupuesto} de una
   * entidad en una {@link Solicitud}.
   * 
   * @param solicitudId Identificador de la {@link Solicitud}.
   * @param entidadRef  Identificador de la entidad.
   * @param ajena       es o no financiacionAjena.
   * @param query       parámentros de búsqueda.
   * @param paging      parámetros de paginación.
   * @return lista paginada.
   */
  Page<SolicitudProyectoPresupuesto> findAllBySolicitudAndEntidadRef(Long solicitudId, String entidadRef, boolean ajena,
      String query, Pageable paging);

  /**
   * Obtiene el {@link SolicitudProyectoPresupuestoTotales} de la
   * {@link Solicitud}.
   * 
   * @param solicitudId Identificador de la entidad {@link Solicitud}.
   * @return {@link SolicitudProyectoPresupuestoTotales}.
   */
  SolicitudProyectoPresupuestoTotales getTotales(Long solicitudId);

  /**
   * Obtiene los {@link SolicitudProyectoPresupuestoTotalConceptoGasto} de la
   * {@link Solicitud}.
   * 
   * @param solicitudId Id de la {@link Solicitud}.
   * @return lista de {@link SolicitudProyectoPresupuestoTotalConceptoGasto}.
   */
  List<SolicitudProyectoPresupuestoTotalConceptoGasto> findAllSolicitudProyectoPresupuestoTotalConceptoGastos(
      Long solicitudId);

  /**
   * Comprueba si existen datos vinculados a la {@link SolicitudProyectoDatos} de
   * {@link SolicitudProyectoPresupuesto} con el fin de permitir la edición de los
   * campo Desglose de presupuesto por entidades
   *
   * @param id Id del {@link SolicitudProyectoDatos}.
   * @return true existen datos vinculados/false no existen datos vinculados.
   */
  Boolean hasSolicitudPresupuesto(Long id);

}
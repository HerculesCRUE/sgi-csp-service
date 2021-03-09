package org.crue.hercules.sgi.csp.service;

import java.util.List;

import org.crue.hercules.sgi.csp.model.Proyecto;
import org.crue.hercules.sgi.csp.model.ProyectoDocumento;

/**
 * Service Interface para gestionar {@link ProyectoDocumento}.
 */
public interface ProyectoDocumentoService {

  /**
   * Guardar un nuevo {@link ProyectoDocumento}.
   *
   * @param proyectoDocumento la entidad {@link ProyectoDocumento} a guardar.
   * @return la entidad {@link ProyectoDocumento} persistida.
   */
  ProyectoDocumento create(ProyectoDocumento proyectoDocumento);

  /**
   * Actualizar {@link ProyectoDocumento}.
   *
   * @param proyectoDocumentoActualizar la entidad {@link ProyectoDocumento} a
   *                                    actualizar.
   * @return la entidad {@link ProyectoDocumento} persistida.
   */
  ProyectoDocumento update(ProyectoDocumento proyectoDocumentoActualizar);

  /**
   * Elimina el {@link ProyectoDocumento}.
   *
   * @param id Id del {@link ProyectoDocumento}.
   */
  void delete(Long id);

  /**
   * Obtiene {@link ProyectoDocumento} por su id.
   *
   * @param id el id de la entidad {@link ProyectoDocumento}.
   * @return la entidad {@link ProyectoDocumento}.
   */
  ProyectoDocumento findById(Long id);

  /**
   * Obtiene las {@link ProyectoDocumento} para una {@link Proyecto}.
   *
   * @param proyectoId el id de la {@link Proyecto}.
   * @return la lista de entidades {@link ProyectoDocumento} de la
   *         {@link Proyecto} paginadas.
   */
  List<ProyectoDocumento> findAllByProyecto(Long proyectoId);

}

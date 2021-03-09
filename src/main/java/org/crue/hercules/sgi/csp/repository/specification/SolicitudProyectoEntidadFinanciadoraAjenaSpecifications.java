package org.crue.hercules.sgi.csp.repository.specification;

import org.crue.hercules.sgi.csp.model.Solicitud;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoDatos;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoDatos_;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoEntidadFinanciadoraAjena;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoEntidadFinanciadoraAjena_;
import org.crue.hercules.sgi.csp.model.Solicitud_;
import org.springframework.data.jpa.domain.Specification;

public class SolicitudProyectoEntidadFinanciadoraAjenaSpecifications {

  /**
   * {@link SolicitudProyectoEntidadFinanciadoraAjena} de la {@link Solicitud} con
   * el id indicado.
   * 
   * @param id identificador del {@link SolicitudProyectoDatos}.
   * @return specification para obtener los
   *         {@link SolicitudProyectoEntidadFinanciadoraAjena} de la
   *         {@link Solicitud} con el id indicado.
   */
  public static Specification<SolicitudProyectoEntidadFinanciadoraAjena> bySolicitudId(Long id) {
    return (root, query, cb) -> {
      return cb.equal(root.get(SolicitudProyectoEntidadFinanciadoraAjena_.solicitudProyectoDatos)
          .get(SolicitudProyectoDatos_.solicitud).get(Solicitud_.id), id);
    };
  }

}
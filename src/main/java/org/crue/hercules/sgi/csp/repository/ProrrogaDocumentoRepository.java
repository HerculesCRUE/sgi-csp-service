package org.crue.hercules.sgi.csp.repository;

import java.util.List;

import org.crue.hercules.sgi.csp.model.ProrrogaDocumento;
import org.crue.hercules.sgi.csp.model.ProyectoProrroga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProrrogaDocumentoRepository
    extends JpaRepository<ProrrogaDocumento, Long>, JpaSpecificationExecutor<ProrrogaDocumento> {

  /**
   * 
   * Elimina los {@link ProrrogaDocumento} del {@link ProyectoProrroga} indicado.
   * 
   * @param proyectoProrrogaId Id de la {@link ProyectoProrroga}
   * @return {@link ProrrogaDocumento} eliminados
   */
  List<ProrrogaDocumento> deleteByProyectoProrrogaId(Long proyectoProrrogaId);
}

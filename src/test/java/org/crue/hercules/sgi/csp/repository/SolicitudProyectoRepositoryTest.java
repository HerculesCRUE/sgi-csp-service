package org.crue.hercules.sgi.csp.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.csp.enums.FormularioSolicitud;
import org.crue.hercules.sgi.csp.model.Solicitud;
import org.crue.hercules.sgi.csp.model.SolicitudProyecto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * SolicitudProyectoRepositoryTest
 */
@DataJpaTest
public class SolicitudProyectoRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SolicitudProyectoRepository repository;

  @Test
  public void findBySolicitudId_ReturnsSolicitudProyecto() throws Exception {

    // given: 2 SolicitudProyecto de los que 1 coincide con el idSolicitud
    // buscado

    // @formatter:off
    Solicitud solicitud1 = entityManager.persistAndFlush(Solicitud.builder()
        .creadorRef("user-001")
        .solicitanteRef("user-002")
        .unidadGestionRef("OTRI")
        .formularioSolicitud(FormularioSolicitud.AYUDAS_GRUPOS)
        .activo(Boolean.TRUE)
        .build());
    // @formatter:on
    SolicitudProyecto solicitudProyecto1 = entityManager
        .persistAndFlush(new SolicitudProyecto(solicitud1.getId(), "solicitud1", null, null, null, Boolean.TRUE,
            Boolean.TRUE, null, null, null, null, null, Boolean.FALSE, Boolean.TRUE));

    // @formatter:off
    Solicitud solicitud2 = entityManager.persistAndFlush(Solicitud.builder()
        .creadorRef("user-001")
        .solicitanteRef("user-002")
        .unidadGestionRef("OTRI")
        .formularioSolicitud(FormularioSolicitud.AYUDAS_GRUPOS)
        .activo(Boolean.TRUE)
        .build());
    // @formatter:on
    entityManager.persistAndFlush(new SolicitudProyecto(solicitud2.getId(), "solicitud2", null, null, null,
        Boolean.TRUE, Boolean.TRUE, null, null, null, null, null, Boolean.FALSE, Boolean.TRUE));

    Long convocatoriaIdBuscada = solicitud1.getId();

    // when: se busca el SolicitudProyecto por idSolicitud
    SolicitudProyecto solicitudProyectoEncontrado = repository.findBySolicitudId(convocatoriaIdBuscada).get();

    // then: Se recupera el SolicitudProyecto con el idSolicitud buscado
    Assertions.assertThat(solicitudProyectoEncontrado.getId()).as("getId").isNotNull();
    Assertions.assertThat(solicitudProyectoEncontrado.getTitulo()).as("getTitulo")
        .isEqualTo(solicitudProyecto1.getTitulo());

  }

  @Test
  public void findBySolicitudNoExiste_ReturnsNull() throws Exception {

    // given: 2 SolicitudProyecto de los que ninguno coincide con el
    // idSolicitud
    // buscado
    // @formatter:off
    Solicitud solicitud1 = entityManager.persistAndFlush(Solicitud.builder()
        .creadorRef("user-001")
        .solicitanteRef("user-002")
        .unidadGestionRef("OTRI")
        .formularioSolicitud(FormularioSolicitud.AYUDAS_GRUPOS)
        .activo(Boolean.TRUE)
        .build());
    // @formatter:on
    entityManager.persistAndFlush(new SolicitudProyecto(solicitud1.getId(), "solicitud1", null, null, null,
        Boolean.TRUE, Boolean.TRUE, null, null, null, null, null, Boolean.FALSE, Boolean.TRUE));
    // @formatter:off
    Solicitud solicitud2 = entityManager.persistAndFlush(Solicitud.builder()
        .creadorRef("user-001")
        .solicitanteRef("user-002")
        .unidadGestionRef("OTRI")
        .formularioSolicitud(FormularioSolicitud.AYUDAS_GRUPOS)
        .activo(Boolean.TRUE)
        .build());
    // @formatter:on
    entityManager.persistAndFlush(new SolicitudProyecto(solicitud2.getId(), "solicitud2", null, null, null,
        Boolean.TRUE, Boolean.TRUE, null, null, null, null, null, Boolean.FALSE, Boolean.TRUE));

    Long solicitudIdBuscada = 99999L;

    // when: se busca el SolicitudProyecto por solicitudId
    Optional<SolicitudProyecto> solicitudProyectoEncontrado = repository.findBySolicitudId(solicitudIdBuscada);

    // then: Se recupera el SolicitudProyecto con el solicitudId buscado
    Assertions.assertThat(solicitudProyectoEncontrado).isEqualTo(Optional.empty());
  }

}

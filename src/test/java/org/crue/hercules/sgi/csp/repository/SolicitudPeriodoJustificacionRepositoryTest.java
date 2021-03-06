package org.crue.hercules.sgi.csp.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.csp.enums.FormularioSolicitud;
import org.crue.hercules.sgi.csp.model.RolProyecto;
import org.crue.hercules.sgi.csp.model.RolSocio;
import org.crue.hercules.sgi.csp.model.Solicitud;
import org.crue.hercules.sgi.csp.model.SolicitudProyecto;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoSocioPeriodoJustificacion;
import org.crue.hercules.sgi.csp.model.SolicitudProyectoSocio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SolicitudPeriodoJustificacionRepositoryTest extends BaseRepositoryTest {

  @Autowired
  private SolicitudProyectoSocioPeriodoJustificacionRepository repository;

  @Test
  public void findAllBySolicitudProyectoSocioId_ReturnsSolicitudProyectoSocioPeriodoJustificacion() throws Exception {

    // given: 2 SolicitudProyectoSocioPeriodoJustificacion para el
    // solicitudProyectoSocio
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
    SolicitudProyecto solicitudProyecto = entityManager
        .persistAndFlush(new SolicitudProyecto(solicitud1.getId(), "solicitud1", null, null, null, Boolean.TRUE,
            Boolean.TRUE, null, null, null, null, null, Boolean.FALSE, Boolean.TRUE));

    // @formatter:off
    RolSocio rolSocio = RolSocio.builder()
        .abreviatura("001")
        .nombre("Lider")
        .descripcion("Lider")
        .coordinador(Boolean.FALSE)
        .activo(Boolean.TRUE)
        .build();
    entityManager.persistAndFlush(rolSocio);

    RolProyecto rolProyecto = RolProyecto.builder()
        .abreviatura("001")
        .nombre("Rol1")
        .descripcion("Rol1")
        .rolPrincipal(Boolean.FALSE)
        .responsableEconomico(Boolean.FALSE)
        .equipo(RolProyecto.Equipo.INVESTIGACION)
        .activo(Boolean.TRUE)
        .build();
    entityManager.persistAndFlush(rolProyecto);
    // @formatter:on

    SolicitudProyectoSocio solicitudProyectoSocio1 = entityManager.persistAndFlush(
        new SolicitudProyectoSocio(null, solicitudProyecto.getId(), rolSocio, "001", 1, 3, 3, new BigDecimal(468)));

    SolicitudProyectoSocio solicitudProyectoSocio2 = entityManager.persistAndFlush(
        new SolicitudProyectoSocio(null, solicitudProyecto.getId(), rolSocio, "002", 1, 3, 3, new BigDecimal(468)));

    SolicitudProyectoSocioPeriodoJustificacion solicitudProyectoSocioPeriodoJustificacion1 = entityManager
        .persistAndFlush(new SolicitudProyectoSocioPeriodoJustificacion(null, solicitudProyectoSocio1.getId(), 1, 2, 3,
            Instant.parse("2020-12-20T00:00:00Z"), Instant.parse("2021-03-20T00:00:00Z"), null));
    entityManager.persistAndFlush(new SolicitudProyectoSocioPeriodoJustificacion(null, solicitudProyectoSocio1.getId(),
        1, 4, 6, Instant.parse("2020-12-20T00:00:00Z"), Instant.parse("2021-03-20T00:00:00Z"), null));
    entityManager.persistAndFlush(new SolicitudProyectoSocioPeriodoJustificacion(null, solicitudProyectoSocio2.getId(),
        1, 4, 6, Instant.parse("2020-12-20T00:00:00Z"), Instant.parse("2021-03-20T00:00:00Z"), null));

    Long solicitudProyectoSocioBuscado = solicitudProyectoSocio1.getId();

    // when: se buscan los SolicitudProyectoSocioPeriodoJustificacion
    // por SolicitudProyectoSocioId
    List<SolicitudProyectoSocioPeriodoJustificacion> dataFound = repository
        .findAllBySolicitudProyectoSocioId(solicitudProyectoSocioBuscado);

    // then: Se recuperan los SolicitudProyectoSocioPeriodoJustificacion con el
    // SolicitudProyectoSocioId
    // buscado
    Assertions.assertThat(dataFound.size()).isEqualTo(2);
    Assertions.assertThat(dataFound.get(0).getId()).isEqualTo(solicitudProyectoSocioPeriodoJustificacion1.getId());
    Assertions.assertThat(dataFound.get(0).getSolicitudProyectoSocioId())
        .isEqualTo(solicitudProyectoSocioPeriodoJustificacion1.getSolicitudProyectoSocioId());
    Assertions.assertThat(dataFound.get(0).getObservaciones())
        .isEqualTo(solicitudProyectoSocioPeriodoJustificacion1.getObservaciones());
  }

}

package org.crue.hercules.sgi.csp.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.csp.enums.ClasificacionCVNEnum;
import org.crue.hercules.sgi.csp.enums.TipoDestinatarioEnum;
import org.crue.hercules.sgi.csp.enums.TipoEstadoConvocatoriaEnum;
import org.crue.hercules.sgi.csp.model.ConceptoGasto;
import org.crue.hercules.sgi.csp.model.Convocatoria;
import org.crue.hercules.sgi.csp.model.ConvocatoriaConceptoGasto;
import org.crue.hercules.sgi.csp.model.ConvocatoriaConceptoGastoCodigoEc;
import org.crue.hercules.sgi.csp.model.ModeloEjecucion;
import org.crue.hercules.sgi.csp.model.ModeloTipoFinalidad;
import org.crue.hercules.sgi.csp.model.TipoAmbitoGeografico;
import org.crue.hercules.sgi.csp.model.TipoFinalidad;
import org.crue.hercules.sgi.csp.model.TipoRegimenConcurrencia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class ConvocatoriaConceptoGastoCodigoEcRepositoryTest {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ConvocatoriaConceptoGastoCodigoEcRepository repository;

  @Test
  public void findByConvocatoriaConceptoGastoId_ReturnsListConvocatoriaConceptoGastoCodigoEc() throws Exception {
    // given: data ConvocatoriaConceptoGastoCodigoEc to find by
    // ConvocatoriaConceptoGasto and permitido
    ConvocatoriaConceptoGastoCodigoEc convocatoriaConceptoGastoCodigoEc1 = generarConvocatoriaConceptoGastoCodigoEc(
        "-001", true);

    generarConvocatoriaConceptoGastoCodigoEc("-002", true);

    // when: find by ConvocatoriaConceptoGasto and permitido
    List<ConvocatoriaConceptoGastoCodigoEc> dataFound = repository
        .findByConvocatoriaConceptoGastoId(convocatoriaConceptoGastoCodigoEc1.getConvocatoriaConceptoGasto().getId());

    // then: ConvocatoriaConceptoGasto is found
    Assertions.assertThat(dataFound.get(0)).isNotNull();
    Assertions.assertThat(dataFound.get(0).getId()).isEqualTo(convocatoriaConceptoGastoCodigoEc1.getId());
    Assertions.assertThat(dataFound.get(0).getConvocatoriaConceptoGasto().getId())
        .isEqualTo(convocatoriaConceptoGastoCodigoEc1.getConvocatoriaConceptoGasto().getId());
  }

  @Test
  public void findByConvocatoriaConceptoGastoId_ReturnsEmptyListConvocatoriaConceptoGastoCodigoEc() throws Exception {
    // given: data ConvocatoriaConceptoGastoCodigoEc to find by
    // ConvocatoriaConceptoGasto and permitido
    generarConvocatoriaConceptoGastoCodigoEc("-001", true);
    generarConvocatoriaConceptoGastoCodigoEc("-002", true);

    // when: find by ConvocatoriaConceptoGasto and permitido
    List<ConvocatoriaConceptoGastoCodigoEc> dataFound = repository.findByConvocatoriaConceptoGastoId(5L);

    // then: ConvocatoriaConceptoGastoCodigoEc is not found
    Assertions.assertThat(dataFound).size().isEqualTo(0);
  }

  /**
   * Función que genera ConvocatoriaConceptoGastoCodigoEc
   * 
   * @param suffix
   * @return el objeto ConvocatoriaConceptoGastoCodigoEc
   */
  private ConvocatoriaConceptoGastoCodigoEc generarConvocatoriaConceptoGastoCodigoEc(String suffix, Boolean permitido) {

    ModeloEjecucion modeloEjecucion = ModeloEjecucion.builder()//
        .nombre("nombreModeloEjecucion" + suffix)//
        .activo(Boolean.TRUE)//
        .build();
    entityManager.persistAndFlush(modeloEjecucion);

    TipoFinalidad tipoFinalidad = TipoFinalidad.builder()//
        .nombre("nombreTipoFinalidad" + suffix)//
        .activo(Boolean.TRUE)//
        .build();
    entityManager.persistAndFlush(tipoFinalidad);

    ModeloTipoFinalidad modeloTipoFinalidad = ModeloTipoFinalidad.builder()//
        .modeloEjecucion(modeloEjecucion)//
        .tipoFinalidad(tipoFinalidad)//
        .activo(Boolean.TRUE)//
        .build();
    entityManager.persistAndFlush(modeloTipoFinalidad);

    TipoRegimenConcurrencia tipoRegimenConcurrencia = TipoRegimenConcurrencia.builder()//
        .nombre("nombreTipoRegimenConcurrencia" + suffix)//
        .activo(Boolean.TRUE)//
        .build();
    entityManager.persistAndFlush(tipoRegimenConcurrencia);

    TipoAmbitoGeografico tipoAmbitoGeografico = TipoAmbitoGeografico.builder()//
        .nombre("nombreTipoAmbitoGeografico" + suffix)//
        .activo(Boolean.TRUE)//
        .build();
    entityManager.persistAndFlush(tipoAmbitoGeografico);

    Convocatoria convocatoria = Convocatoria.builder()//
        .unidadGestionRef("unidad" + suffix)//
        .modeloEjecucion(modeloEjecucion)//
        .codigo("codigo" + suffix)//
        .anio(2020)//
        .titulo("titulo" + suffix)//
        .objeto("objeto" + suffix)//
        .observaciones("observaciones" + suffix)//
        .finalidad(modeloTipoFinalidad.getTipoFinalidad())//
        .regimenConcurrencia(tipoRegimenConcurrencia)//
        .destinatarios(TipoDestinatarioEnum.INDIVIDUAL)//
        .colaborativos(Boolean.TRUE)//
        .estadoActual(TipoEstadoConvocatoriaEnum.REGISTRADA)//
        .duracion(12)//
        .ambitoGeografico(tipoAmbitoGeografico)//
        .clasificacionCVN(ClasificacionCVNEnum.AYUDAS).activo(Boolean.TRUE)//
        .build();
    entityManager.persistAndFlush(convocatoria);

    ConceptoGasto conceptoGasto = ConceptoGasto.builder()//
        .nombre("nombreConceptoGasto" + suffix)//
        .activo(Boolean.TRUE)//
        .build();
    entityManager.persistAndFlush(conceptoGasto);

    ConvocatoriaConceptoGasto convocatoriaConceptoGasto = ConvocatoriaConceptoGasto.builder()//
        .convocatoria(convocatoria)//
        .conceptoGasto(conceptoGasto)//
        .observaciones("obs-1")//
        .permitido(permitido).build();
    entityManager.persistAndFlush(convocatoriaConceptoGasto);

    ConvocatoriaConceptoGastoCodigoEc convocatoriaConceptoGastoCodigoEc = ConvocatoriaConceptoGastoCodigoEc.builder()
        .convocatoriaConceptoGasto(convocatoriaConceptoGasto).codigoEconomicoRef("cod-" + suffix).build();
    return entityManager.persistAndFlush(convocatoriaConceptoGastoCodigoEc);
  }
}
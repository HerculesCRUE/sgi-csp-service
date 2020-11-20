package org.crue.hercules.sgi.csp.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.csp.model.TipoAmbitoGeografico;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * TipoAmbitoGeograficoRepositoryTest
 */
@DataJpaTest
public class TipoAmbitoGeograficoRepositoryTest extends BaseRepositoryTest {

  @Autowired
  private TipoAmbitoGeograficoRepository repository;

  @Test
  public void findByNombre_ReturnsTipoAmbitoGeografico() throws Exception {

    // given: 2 TipoAmbitoGeografico de los que 1 coincide con el nombre buscado
    TipoAmbitoGeografico tipoAmbitoGeografico1 = new TipoAmbitoGeografico(null, "nombre-1", true);
    entityManager.persistAndFlush(tipoAmbitoGeografico1);

    TipoAmbitoGeografico tipoAmbitoGeografico2 = new TipoAmbitoGeografico(null, "nombre-2", true);
    entityManager.persistAndFlush(tipoAmbitoGeografico2);

    String nombreBuscado = "nombre-1";

    // when: se busca el TipoAmbitoGeografico nombre
    TipoAmbitoGeografico tipoAmbitoGeograficoEncontrado = repository.findByNombre(nombreBuscado).get();

    // then: Se recupera el TipoAmbitoGeografico con el nombre buscado
    Assertions.assertThat(tipoAmbitoGeograficoEncontrado.getId()).as("getId").isNotNull();
    Assertions.assertThat(tipoAmbitoGeograficoEncontrado.getNombre()).as("getNombre")
        .isEqualTo(tipoAmbitoGeografico1.getNombre());
    Assertions.assertThat(tipoAmbitoGeograficoEncontrado.getActivo()).as("getActivo")
        .isEqualTo(tipoAmbitoGeografico1.getActivo());
  }

  @Test
  public void findByNombreNoExiste_ReturnsNull() throws Exception {

    // given: 2 TipoAmbitoGeografico que no coinciden con el nombre buscado
    TipoAmbitoGeografico tipoAmbitoGeografico1 = new TipoAmbitoGeografico(null, "nombre-1", true);
    entityManager.persistAndFlush(tipoAmbitoGeografico1);

    TipoAmbitoGeografico tipoAmbitoGeografico2 = new TipoAmbitoGeografico(null, "nombre-2", true);
    entityManager.persistAndFlush(tipoAmbitoGeografico2);

    String nombreBuscado = "nombre-noexiste";

    // when: se busca el TipoAmbitoGeografico por nombre
    Optional<TipoAmbitoGeografico> tipoAmbitoGeograficoEncontrado = repository.findByNombre(nombreBuscado);

    // then: No hay ningun TipoAmbitoGeografico con el nombre buscado
    Assertions.assertThat(tipoAmbitoGeograficoEncontrado).isEqualTo(Optional.empty());
  }

}
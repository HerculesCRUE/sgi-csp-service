package org.crue.hercules.sgi.csp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.csp.exceptions.ConvocatoriaEntidadFinanciadoraNotFoundException;
import org.crue.hercules.sgi.csp.exceptions.ConvocatoriaNotFoundException;
import org.crue.hercules.sgi.csp.exceptions.FuenteFinanciacionNotFoundException;
import org.crue.hercules.sgi.csp.exceptions.TipoFinanciacionNotFoundException;
import org.crue.hercules.sgi.csp.model.Convocatoria;
import org.crue.hercules.sgi.csp.model.ConvocatoriaEntidadFinanciadora;
import org.crue.hercules.sgi.csp.model.FuenteFinanciacion;
import org.crue.hercules.sgi.csp.model.Programa;
import org.crue.hercules.sgi.csp.model.TipoFinanciacion;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaEntidadFinanciadoraRepository;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaRepository;
import org.crue.hercules.sgi.csp.repository.FuenteFinanciacionRepository;
import org.crue.hercules.sgi.csp.repository.TipoFinanciacionRepository;
import org.crue.hercules.sgi.csp.service.impl.ConvocatoriaEntidadFinanciadoraServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * ConvocatoriaEntidadFinanciadoraServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class ConvocatoriaEntidadFinanciadoraServiceTest extends BaseServiceTest {

  @Mock
  private ConvocatoriaEntidadFinanciadoraRepository repository;

  @Mock
  private ConvocatoriaRepository convocatoriaRepository;

  @Mock
  private FuenteFinanciacionRepository fuenteFinanciacionRepository;

  @Mock
  private TipoFinanciacionRepository tipoFinanciacionRepository;

  private ConvocatoriaEntidadFinanciadoraService service;

  @BeforeEach
  public void setUp() throws Exception {
    service = new ConvocatoriaEntidadFinanciadoraServiceImpl(repository, convocatoriaRepository,
        fuenteFinanciacionRepository, tipoFinanciacionRepository);
  }

  @Test
  public void create_ReturnsConvocatoriaEntidadFinanciadora() {
    // given: Un nuevo ConvocatoriaEntidadFinanciadora
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(null);

    BDDMockito.given(convocatoriaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getConvocatoria()));
    BDDMockito.given(fuenteFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getFuenteFinanciacion()));
    BDDMockito.given(tipoFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getTipoFinanciacion()));

    BDDMockito.given(repository.save(convocatoriaEntidadFinanciadora)).will((InvocationOnMock invocation) -> {
      ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadoraCreado = invocation.getArgument(0);
      convocatoriaEntidadFinanciadoraCreado.setId(1L);
      return convocatoriaEntidadFinanciadoraCreado;
    });

    // when: Creamos el ConvocatoriaEntidadFinanciadora
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadoraCreado = service
        .create(convocatoriaEntidadFinanciadora);

    // then: El ConvocatoriaEntidadFinanciadora se crea correctamente
    Assertions.assertThat(convocatoriaEntidadFinanciadoraCreado).as("isNotNull()").isNotNull();
    Assertions.assertThat(convocatoriaEntidadFinanciadoraCreado.getId()).as("getId()").isNotNull();
    Assertions.assertThat(convocatoriaEntidadFinanciadoraCreado.getConvocatoria().getId())
        .as("getConvocatoria().getId()").isEqualTo(convocatoriaEntidadFinanciadora.getConvocatoria().getId());
    Assertions.assertThat(convocatoriaEntidadFinanciadoraCreado.getEntidadRef()).as("getEntidadRef()")
        .isEqualTo(convocatoriaEntidadFinanciadora.getEntidadRef());
    Assertions.assertThat(convocatoriaEntidadFinanciadoraCreado.getFuenteFinanciacion().getId())
        .as("getFuenteFinanciacion().getId()")
        .isEqualTo(convocatoriaEntidadFinanciadora.getFuenteFinanciacion().getId());
    Assertions.assertThat(convocatoriaEntidadFinanciadoraCreado.getTipoFinanciacion().getId())
        .as("getTipoFinanciacion().getId()").isEqualTo(convocatoriaEntidadFinanciadora.getTipoFinanciacion().getId());
    Assertions.assertThat(convocatoriaEntidadFinanciadoraCreado.getPorcentajeFinanciacion())
        .as("getPorcentajeFinanciacion())").isEqualTo(convocatoriaEntidadFinanciadora.getPorcentajeFinanciacion());
  }

  @Test
  public void create_WithId_ThrowsIllegalArgumentException() {
    // given: Un nuevo ConvocatoriaEntidadFinanciadora que ya tiene id
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(1L);

    // when: Creamos el ConvocatoriaEntidadFinanciadora
    // then: Lanza una excepcion porque el ConvocatoriaEntidadFinanciadora ya tiene
    // id
    Assertions.assertThatThrownBy(() -> service.create(convocatoriaEntidadFinanciadora))
        .isInstanceOf(IllegalArgumentException.class).hasMessage(
            "ConvocatoriaEntidadFinanciadora id tiene que ser null para crear un nuevo ConvocatoriaEntidadFinanciadora");
  }

  @Test
  public void create_WithNegativePorcentajeFinanciacion_ThrowsIllegalArgumentException() {
    // given: Un nuevo ConvocatoriaEntidadFinanciadora con porcentaje negativo
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(null);
    convocatoriaEntidadFinanciadora.setPorcentajeFinanciacion(-10);

    // when: Creamos el ConvocatoriaEntidadFinanciadora
    // then: Lanza una excepcion porque el PorcentajeFinanciacion es negativo
    Assertions.assertThatThrownBy(() -> service.create(convocatoriaEntidadFinanciadora))
        .isInstanceOf(IllegalArgumentException.class).hasMessage("PorcentajeFinanciacion no puede ser negativo");
  }

  @Test
  public void create_WithoutConvocatoriaId_ThrowsIllegalArgumentException() {
    // given: a ConvocatoriaEntidadFinanciadora without ConvocatoriaId
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(null);
    convocatoriaEntidadFinanciadora.getConvocatoria().setId(null);

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaEntidadFinanciadora
        () -> service.create(convocatoriaEntidadFinanciadora))
        // then: throw exception as ConvocatoriaId is null
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Id Convocatoria no puede ser null para crear ConvocatoriaEntidadFinanciadora");
  }

  @Test
  public void create_WithNoExistingConvocatoria_ThrowsConvocatoriaNotFoundException() {
    // given: a ConvocatoriaEntidadFinanciadora with non existing Convocatoria
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(null);

    BDDMockito.given(convocatoriaRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaEntidadFinanciadora
        () -> service.create(convocatoriaEntidadFinanciadora))
        // then: throw exception as Convocatoria is not found
        .isInstanceOf(ConvocatoriaNotFoundException.class);
  }

  @Test
  public void create_WithNoExistingFuenteFinanciacion_ThrowsFuenteFinanciacionNotFoundException() {
    // given: a ConvocatoriaEntidadFinanciadora with non existing FuenteFinanciacion
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(null);

    BDDMockito.given(convocatoriaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getConvocatoria()));
    BDDMockito.given(fuenteFinanciacionRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaEntidadFinanciadora
        () -> service.create(convocatoriaEntidadFinanciadora))
        // then: throw exception as FuenteFinanciacion is not found
        .isInstanceOf(FuenteFinanciacionNotFoundException.class);
  }

  @Test
  public void create_WithFuenteFinanciacionActivoFalse_ThrowsIllegalArgumentException() {
    // given: a ConvocatoriaEntidadFinanciadora with FuenteFinanciacion activo=false
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(null);
    convocatoriaEntidadFinanciadora.getFuenteFinanciacion().setActivo(false);

    BDDMockito.given(convocatoriaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getConvocatoria()));
    BDDMockito.given(fuenteFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getFuenteFinanciacion()));

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaEntidadFinanciadora
        () -> service.create(convocatoriaEntidadFinanciadora))
        // then: throw exception as FuenteFinanciacion is not activo
        .isInstanceOf(IllegalArgumentException.class).hasMessage("La FuenteFinanciacion debe estar Activo");
  }

  @Test
  public void create_WithNoExistingTipoFinanciacion_ThrowsFuenteFinanciacionNotFoundException() {
    // given: a ConvocatoriaEntidadFinanciadora with non existing TipoFinanciacion
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(null);

    BDDMockito.given(convocatoriaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getConvocatoria()));
    BDDMockito.given(fuenteFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getFuenteFinanciacion()));
    BDDMockito.given(tipoFinanciacionRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaEntidadFinanciadora
        () -> service.create(convocatoriaEntidadFinanciadora))
        // then: throw exception as TipoFinanciacion is not found
        .isInstanceOf(TipoFinanciacionNotFoundException.class);
  }

  @Test
  public void create_WithTipoFinanciacionActivoFalse_ThrowsIllegalArgumentException() {
    // given: a ConvocatoriaEntidadFinanciadora with TipoFinanciacion activo=false
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(null);
    convocatoriaEntidadFinanciadora.getTipoFinanciacion().setActivo(false);

    BDDMockito.given(convocatoriaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getConvocatoria()));
    BDDMockito.given(fuenteFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getFuenteFinanciacion()));
    BDDMockito.given(tipoFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getTipoFinanciacion()));

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaEntidadFinanciadora
        () -> service.create(convocatoriaEntidadFinanciadora))
        // then: throw exception as TipoFinanciacion is not activo
        .isInstanceOf(IllegalArgumentException.class).hasMessage("El TipoFinanciacion debe estar Activo");
  }

  @Test
  public void update_ReturnsConvocatoriaEntidadFinanciadora() {
    // given: Un nuevo ConvocatoriaEntidadFinanciadora con el nombre actualizado
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(1L);
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadoraPorcentajeActualizado = generarMockConvocatoriaEntidadFinanciadora(
        1L);
    convocatoriaEntidadFinanciadoraPorcentajeActualizado.setPorcentajeFinanciacion(1);

    BDDMockito.given(fuenteFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getFuenteFinanciacion()));
    BDDMockito.given(tipoFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getTipoFinanciacion()));

    BDDMockito.given(repository.findById(ArgumentMatchers.<Long>any()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora));

    BDDMockito.given(repository.save(ArgumentMatchers.<ConvocatoriaEntidadFinanciadora>any()))
        .will((InvocationOnMock invocation) -> invocation.getArgument(0));

    // when: Actualizamos el ConvocatoriaEntidadFinanciadora
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadoraActualizado = service
        .update(convocatoriaEntidadFinanciadoraPorcentajeActualizado);

    // then: El ConvocatoriaEntidadFinanciadora se actualiza correctamente.
    Assertions.assertThat(convocatoriaEntidadFinanciadoraActualizado).as("isNotNull()").isNotNull();
    Assertions.assertThat(convocatoriaEntidadFinanciadoraActualizado.getId()).as("getId()")
        .isEqualTo(convocatoriaEntidadFinanciadora.getId());
    Assertions.assertThat(convocatoriaEntidadFinanciadoraActualizado.getConvocatoria().getId())
        .as("getConvocatoria().getId()").isEqualTo(convocatoriaEntidadFinanciadora.getConvocatoria().getId());
    Assertions.assertThat(convocatoriaEntidadFinanciadoraActualizado.getEntidadRef()).as("getEntidadRef()")
        .isEqualTo(convocatoriaEntidadFinanciadora.getEntidadRef());
    Assertions.assertThat(convocatoriaEntidadFinanciadoraActualizado.getFuenteFinanciacion().getId())
        .as("getFuenteFinanciacion().getId()")
        .isEqualTo(convocatoriaEntidadFinanciadora.getFuenteFinanciacion().getId());
    Assertions.assertThat(convocatoriaEntidadFinanciadoraActualizado.getTipoFinanciacion().getId())
        .as("getTipoFinanciacion().getId()").isEqualTo(convocatoriaEntidadFinanciadora.getTipoFinanciacion().getId());
    Assertions.assertThat(convocatoriaEntidadFinanciadoraActualizado.getPorcentajeFinanciacion())
        .as("getPorcentajeFinanciacion())")
        .isEqualTo(convocatoriaEntidadFinanciadoraPorcentajeActualizado.getPorcentajeFinanciacion());
  }

  @Test
  public void update_WithNegativePorcentajeFinanciacion_ThrowsIllegalArgumentException() {
    // given: Un ConvocatoriaEntidadFinanciadora con porcentaje negativo
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(1L);
    convocatoriaEntidadFinanciadora.setPorcentajeFinanciacion(-10);

    // when: Actualizamos el ConvocatoriaEntidadFinanciadora
    // then: Lanza una excepcion porque el PorcentajeFinanciacion es negativo
    Assertions.assertThatThrownBy(() -> service.update(convocatoriaEntidadFinanciadora))
        .isInstanceOf(IllegalArgumentException.class).hasMessage("PorcentajeFinanciacion no puede ser negativo");
  }

  @Test
  public void update_WithIdNotExist_ThrowsConvocatoriaEntidadFinanciadoraNotFoundException() {
    // given: Un ConvocatoriaEntidadFinanciadora a actualizar con un id que no
    // existe
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(1L);

    BDDMockito.given(fuenteFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getFuenteFinanciacion()));
    BDDMockito.given(tipoFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getTipoFinanciacion()));

    BDDMockito.given(repository.findById(ArgumentMatchers.<Long>any())).willReturn(Optional.empty());

    // when: Actualizamos el ConvocatoriaEntidadFinanciadora
    // then: Lanza una excepcion porque el ConvocatoriaEntidadFinanciadora no existe
    Assertions.assertThatThrownBy(() -> service.update(convocatoriaEntidadFinanciadora))
        .isInstanceOf(ConvocatoriaEntidadFinanciadoraNotFoundException.class);
  }

  @Test
  public void update_WithNoExistingFuenteFinanciacion_ThrowsFuenteFinanciacionNotFoundException() {
    // given: a ConvocatoriaEntidadFinanciadora with non existing FuenteFinanciacion
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(1L);

    BDDMockito.given(fuenteFinanciacionRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(
        // when: update ConvocatoriaEntidadFinanciadora
        () -> service.update(convocatoriaEntidadFinanciadora))
        // then: throw exception as FuenteFinanciacion is not found
        .isInstanceOf(FuenteFinanciacionNotFoundException.class);
  }

  @Test
  public void update_WithNoExistingTipoFinanciacion_ThrowsTipoFinanciacionNotFoundException() {
    // given: a ConvocatoriaEntidadFinanciadora with non existing TipoFinanciacion
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(1L);

    BDDMockito.given(fuenteFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora.getFuenteFinanciacion()));
    BDDMockito.given(tipoFinanciacionRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(
        // when: update ConvocatoriaEntidadFinanciadora
        () -> service.update(convocatoriaEntidadFinanciadora))
        // then: throw exception as TipoFinanciacion is not found
        .isInstanceOf(TipoFinanciacionNotFoundException.class);
  }

  @Test
  public void update_WithFuenteFinanciacionActivoFalse_ThrowsIllegalArgumentException() {
    // given: a ConvocatoriaEntidadFinanciadora with FuenteFinanciacion activo=false
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(1L);
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadoraActualizada = generarMockConvocatoriaEntidadFinanciadora(
        1L);
    convocatoriaEntidadFinanciadoraActualizada.getFuenteFinanciacion().setId(2L);
    convocatoriaEntidadFinanciadoraActualizada.getFuenteFinanciacion().setActivo(false);

    BDDMockito.given(fuenteFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadoraActualizada.getFuenteFinanciacion()));
    BDDMockito.given(tipoFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadoraActualizada.getTipoFinanciacion()));

    BDDMockito.given(repository.findById(ArgumentMatchers.<Long>any()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora));

    Assertions.assertThatThrownBy(
        // when: update ConvocatoriaEntidadFinanciadora
        () -> service.update(convocatoriaEntidadFinanciadoraActualizada))
        // then: throw exception as FuenteFinanciacion is not activo
        .isInstanceOf(IllegalArgumentException.class).hasMessage("La FuenteFinanciacion debe estar Activo");
  }

  @Test
  public void update_WithTipoFinanciacionActivoFalse_ThrowsIllegalArgumentException() {
    // given: a ConvocatoriaEntidadFinanciadora with TipoFinanciacion activo=false
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = generarMockConvocatoriaEntidadFinanciadora(1L);
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadoraActualizada = generarMockConvocatoriaEntidadFinanciadora(
        1L);
    convocatoriaEntidadFinanciadoraActualizada.getTipoFinanciacion().setId(2L);
    convocatoriaEntidadFinanciadoraActualizada.getTipoFinanciacion().setActivo(false);

    BDDMockito.given(fuenteFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadoraActualizada.getFuenteFinanciacion()));
    BDDMockito.given(tipoFinanciacionRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadoraActualizada.getTipoFinanciacion()));

    BDDMockito.given(repository.findById(ArgumentMatchers.<Long>any()))
        .willReturn(Optional.of(convocatoriaEntidadFinanciadora));

    Assertions.assertThatThrownBy(
        // when: update ConvocatoriaEntidadFinanciadora
        () -> service.update(convocatoriaEntidadFinanciadoraActualizada))
        // then: throw exception as TipoFinanciacion is not activo
        .isInstanceOf(IllegalArgumentException.class).hasMessage("El TipoFinanciacion debe estar Activo");
  }

  @Test
  public void delete_WithExistingId_ReturnsConvocatoriaEntidadFinanciadora() {
    // given: existing ConvocatoriaEntidadFinanciadora
    Long id = 1L;

    BDDMockito.given(repository.existsById(ArgumentMatchers.anyLong())).willReturn(Boolean.TRUE);
    BDDMockito.doNothing().when(repository).deleteById(ArgumentMatchers.anyLong());

    Assertions.assertThatCode(
        // when: delete by existing id
        () -> service.delete(id))
        // then: no exception is thrown
        .doesNotThrowAnyException();
  }

  @Test
  public void delete_WithNoExistingId_ThrowsNotFoundException() throws Exception {
    // given: no existing id
    Long id = 1L;

    BDDMockito.given(repository.existsById(ArgumentMatchers.anyLong())).willReturn(Boolean.FALSE);

    Assertions.assertThatThrownBy(
        // when: delete
        () -> service.delete(id))
        // then: NotFoundException is thrown
        .isInstanceOf(ConvocatoriaEntidadFinanciadoraNotFoundException.class);
  }

  @Test
  public void findAllByConvocatoria_ReturnsPage() {
    // given: Una lista con 37 ConvocatoriaEntidadFinanciadora para la Convocatoria
    Long convocatoriaId = 1L;
    List<ConvocatoriaEntidadFinanciadora> convocatoriasEntidadesFinanciadoras = new ArrayList<>();
    for (long i = 1; i <= 37; i++) {
      convocatoriasEntidadesFinanciadoras.add(generarMockConvocatoriaEntidadFinanciadora(i));
    }

    BDDMockito.given(repository.findAll(ArgumentMatchers.<Specification<ConvocatoriaEntidadFinanciadora>>any(),
        ArgumentMatchers.<Pageable>any())).willAnswer((InvocationOnMock invocation) -> {
          Pageable pageable = invocation.getArgument(1, Pageable.class);
          int size = pageable.getPageSize();
          int index = pageable.getPageNumber();
          int fromIndex = size * index;
          int toIndex = fromIndex + size;
          toIndex = toIndex > convocatoriasEntidadesFinanciadoras.size() ? convocatoriasEntidadesFinanciadoras.size()
              : toIndex;
          List<ConvocatoriaEntidadFinanciadora> content = convocatoriasEntidadesFinanciadoras.subList(fromIndex,
              toIndex);
          Page<ConvocatoriaEntidadFinanciadora> pageResponse = new PageImpl<>(content, pageable,
              convocatoriasEntidadesFinanciadoras.size());
          return pageResponse;

        });

    // when: Get page=3 with pagesize=10
    Pageable paging = PageRequest.of(3, 10);
    Page<ConvocatoriaEntidadFinanciadora> page = service.findAllByConvocatoria(convocatoriaId, null, paging);

    // then: Devuelve la pagina 3 con los ConvocatoriaEntidadFinanciadora del 31 al
    // 37
    Assertions.assertThat(page.getContent().size()).as("getContent().size()").isEqualTo(7);
    Assertions.assertThat(page.getNumber()).as("getNumber()").isEqualTo(3);
    Assertions.assertThat(page.getSize()).as("getSize()").isEqualTo(10);
    Assertions.assertThat(page.getTotalElements()).as("getTotalElements()").isEqualTo(37);
    for (int i = 31; i <= 37; i++) {
      ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = page.getContent()
          .get(i - (page.getSize() * page.getNumber()) - 1);
      Assertions.assertThat(convocatoriaEntidadFinanciadora.getId()).isEqualTo(Long.valueOf(i));
      Assertions.assertThat(convocatoriaEntidadFinanciadora.getEntidadRef()).isEqualTo("entidad-" + i);
    }
  }

  @Test
  public void findById_ReturnsConvocatoriaEntidadFinanciadora() {
    // given: Un ConvocatoriaEntidadFinanciadora con el id buscado
    Long idBuscado = 1L;
    BDDMockito.given(repository.findById(idBuscado))
        .willReturn(Optional.of(generarMockConvocatoriaEntidadFinanciadora(idBuscado)));

    // when: Buscamos el ConvocatoriaEntidadFinanciadora por su id
    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = service.findById(idBuscado);

    // then: el ConvocatoriaEntidadFinanciadora
    Assertions.assertThat(convocatoriaEntidadFinanciadora).as("isNotNull()").isNotNull();
    Assertions.assertThat(convocatoriaEntidadFinanciadora.getId()).as("getId()").isEqualTo(idBuscado);
  }

  @Test
  public void findById_WithIdNotExist_ThrowsConvocatoriaEntidadFinanciadoraNotFoundException() throws Exception {
    // given: Ningun ConvocatoriaEntidadFinanciadora con el id buscado
    Long idBuscado = 1L;
    BDDMockito.given(repository.findById(idBuscado)).willReturn(Optional.empty());

    // when: Buscamos el ConvocatoriaEntidadFinanciadora por su id
    // then: lanza un ConvocatoriaEntidadFinanciadoraNotFoundException
    Assertions.assertThatThrownBy(() -> service.findById(idBuscado))
        .isInstanceOf(ConvocatoriaEntidadFinanciadoraNotFoundException.class);
  }

  /**
   * Función que devuelve un objeto ConvocatoriaEntidadFinanciadora
   * 
   * @param id id del ConvocatoriaEntidadFinanciadora
   * @return el objeto ConvocatoriaEntidadFinanciadora
   */
  private ConvocatoriaEntidadFinanciadora generarMockConvocatoriaEntidadFinanciadora(Long id) {
    Convocatoria convocatoria = new Convocatoria();
    convocatoria.setId(id == null ? 1 : id);

    FuenteFinanciacion fuenteFinanciacion = new FuenteFinanciacion();
    fuenteFinanciacion.setId(id == null ? 1 : id);
    fuenteFinanciacion.setActivo(true);

    TipoFinanciacion tipoFinanciacion = new TipoFinanciacion();
    tipoFinanciacion.setId(id == null ? 1 : id);
    tipoFinanciacion.setActivo(true);

    Programa programa = new Programa();
    programa.setId(id);

    ConvocatoriaEntidadFinanciadora convocatoriaEntidadFinanciadora = new ConvocatoriaEntidadFinanciadora();
    convocatoriaEntidadFinanciadora.setId(id);
    convocatoriaEntidadFinanciadora.setConvocatoria(convocatoria);
    convocatoriaEntidadFinanciadora.setEntidadRef("entidad-" + (id == null ? 0 : id));
    convocatoriaEntidadFinanciadora.setFuenteFinanciacion(fuenteFinanciacion);
    convocatoriaEntidadFinanciadora.setTipoFinanciacion(tipoFinanciacion);
    convocatoriaEntidadFinanciadora.setPorcentajeFinanciacion(50);

    return convocatoriaEntidadFinanciadora;
  }

}
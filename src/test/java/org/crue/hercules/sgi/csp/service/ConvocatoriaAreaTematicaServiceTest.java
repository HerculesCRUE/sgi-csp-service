package org.crue.hercules.sgi.csp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.csp.exceptions.AreaTematicaNotFoundException;
import org.crue.hercules.sgi.csp.exceptions.ConvocatoriaAreaTematicaNotFoundException;
import org.crue.hercules.sgi.csp.exceptions.ConvocatoriaNotFoundException;
import org.crue.hercules.sgi.csp.model.AreaTematica;
import org.crue.hercules.sgi.csp.model.Convocatoria;
import org.crue.hercules.sgi.csp.model.ConvocatoriaAreaTematica;
import org.crue.hercules.sgi.csp.repository.AreaTematicaRepository;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaAreaTematicaRepository;
import org.crue.hercules.sgi.csp.repository.ConvocatoriaRepository;
import org.crue.hercules.sgi.csp.service.impl.ConvocatoriaAreaTematicaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class ConvocatoriaAreaTematicaServiceTest extends BaseServiceTest {

  @Mock
  private ConvocatoriaAreaTematicaRepository convocatoriaAreaTematicaRepository;
  @Mock
  private ConvocatoriaRepository convocatoriaRepository;
  @Mock
  private AreaTematicaRepository areaTematicaRepository;

  private ConvocatoriaAreaTematicaService service;

  @BeforeEach
  public void setUp() throws Exception {
    service = new ConvocatoriaAreaTematicaServiceImpl(convocatoriaAreaTematicaRepository, convocatoriaRepository,
        areaTematicaRepository);
  }

  @Test
  public void create_ReturnsConvocatoriaAreaTematica() {
    // given: new ConvocatoriaAreaTematica
    ConvocatoriaAreaTematica newConvocatoriaAreaTematica = generarConvocatoriaAreaTematica(null, 1L, 1L);

    BDDMockito.given(convocatoriaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(newConvocatoriaAreaTematica.getConvocatoria()));
    BDDMockito.given(areaTematicaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(newConvocatoriaAreaTematica.getAreaTematica()));
    BDDMockito.given(convocatoriaAreaTematicaRepository
        .findByConvocatoriaIdAndAreaTematicaId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
        .willReturn(Optional.empty());

    BDDMockito.given(convocatoriaAreaTematicaRepository.save(ArgumentMatchers.<ConvocatoriaAreaTematica>any()))
        .willAnswer(new Answer<ConvocatoriaAreaTematica>() {
          @Override
          public ConvocatoriaAreaTematica answer(InvocationOnMock invocation) throws Throwable {
            ConvocatoriaAreaTematica givenData = invocation.getArgument(0, ConvocatoriaAreaTematica.class);
            ConvocatoriaAreaTematica newData = new ConvocatoriaAreaTematica();
            BeanUtils.copyProperties(givenData, newData);
            newData.setId(1L);
            return newData;
          }
        });

    // when: create ConvocatoriaAreaTematica
    ConvocatoriaAreaTematica createdConvocatoriaAreaTematica = service.create(newConvocatoriaAreaTematica);

    // then: new ConvocatoriaAreaTematica is created
    Assertions.assertThat(createdConvocatoriaAreaTematica).isNotNull();
    Assertions.assertThat(createdConvocatoriaAreaTematica.getId()).isNotNull();
    Assertions.assertThat(createdConvocatoriaAreaTematica.getConvocatoria().getId())
        .isEqualTo(newConvocatoriaAreaTematica.getConvocatoria().getId());
    Assertions.assertThat(createdConvocatoriaAreaTematica.getAreaTematica().getId())
        .isEqualTo(newConvocatoriaAreaTematica.getAreaTematica().getId());
    Assertions.assertThat(createdConvocatoriaAreaTematica.getObservaciones())
        .isEqualTo(newConvocatoriaAreaTematica.getObservaciones());
  }

  @Test
  public void create_WithId_ThrowsIllegalArgumentException() {
    // given: a ConvocatoriaAreaTematica with id filled
    ConvocatoriaAreaTematica newConvocatoriaAreaTematica = generarConvocatoriaAreaTematica(1L, 1L, 1L);

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaAreaTematica
        () -> service.create(newConvocatoriaAreaTematica))
        // then: throw exception as id can't be provided
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void create_WithoutConvocatoriaId_ThrowsIllegalArgumentException() {
    // given: a ConvocatoriaAreaTematica without ConvocatoriaId
    ConvocatoriaAreaTematica newConvocatoriaAreaTematica = generarConvocatoriaAreaTematica(null, null, 1L);

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaAreaTematica
        () -> service.create(newConvocatoriaAreaTematica))
        // then: throw exception as ConvocatoriaId is null
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void create_WithoutAreaTematica_ThrowsIllegalArgumentException() {
    // given: a ConvocatoriaAreaTematica without AreaTematica
    ConvocatoriaAreaTematica newConvocatoriaAreaTematica = generarConvocatoriaAreaTematica(null, 1L, null);

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaAreaTematica
        () -> service.create(newConvocatoriaAreaTematica))
        // then: throw exception as AreaTematica is null
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void create_WithNoExistingConvocatoria_404() {
    // given: a ConvocatoriaAreaTematica with non existing Convocatoria
    ConvocatoriaAreaTematica newConvocatoriaAreaTematica = generarConvocatoriaAreaTematica(null, 1L, 1L);

    BDDMockito.given(convocatoriaRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaAreaTematica
        () -> service.create(newConvocatoriaAreaTematica))
        // then: throw exception as Convocatoria is not found
        .isInstanceOf(ConvocatoriaNotFoundException.class);
  }

  @Test
  public void create_WithNoExistingAreaTematica_404() {
    // given: a ConvocatoriaAreaTematica with non existing AreaTematica
    ConvocatoriaAreaTematica newConvocatoriaAreaTematica = generarConvocatoriaAreaTematica(null, 1L, 1L);

    BDDMockito.given(convocatoriaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(newConvocatoriaAreaTematica.getConvocatoria()));
    BDDMockito.given(areaTematicaRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaAreaTematica
        () -> service.create(newConvocatoriaAreaTematica))
        // then: throw exception as AreaTematica is not found
        .isInstanceOf(AreaTematicaNotFoundException.class);
  }

  @Test
  public void create_WithDuplicatedConvocatoriaIdAndAreaTematicaId_ThrowsIllegalArgumentException() {
    // given: a ConvocatoriaAreaTematica assigned with same
    // Convocatoria And EntidadRef
    ConvocatoriaAreaTematica newConvocatoriaAreaTematica = generarConvocatoriaAreaTematica(null, 1L, 1L);
    ConvocatoriaAreaTematica ConvocatoriaAreaTematicaExistente = generarConvocatoriaAreaTematica(1L, 1L, 1L);

    BDDMockito.given(convocatoriaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(newConvocatoriaAreaTematica.getConvocatoria()));
    BDDMockito.given(areaTematicaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(newConvocatoriaAreaTematica.getAreaTematica()));
    BDDMockito.given(convocatoriaAreaTematicaRepository
        .findByConvocatoriaIdAndAreaTematicaId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(ConvocatoriaAreaTematicaExistente));

    Assertions.assertThatThrownBy(
        // when: create ConvocatoriaAreaTematica
        () -> service.create(newConvocatoriaAreaTematica))
        // then: throw exception as assigned with same Convocatoria And EntidadRef
        // exists
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void update_WithExistingId_ReturnsConvocatoriaAreaTematica() {
    // given: existing ConvocatoriaAreaTematica
    ConvocatoriaAreaTematica convocatoriaAreaTematica = generarConvocatoriaAreaTematica(1L, 1L, 1L);

    BDDMockito.given(convocatoriaAreaTematicaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(convocatoriaAreaTematica));

    BDDMockito.given(convocatoriaAreaTematicaRepository.save(ArgumentMatchers.<ConvocatoriaAreaTematica>any()))
        .willAnswer(new Answer<ConvocatoriaAreaTematica>() {
          @Override
          public ConvocatoriaAreaTematica answer(InvocationOnMock invocation) throws Throwable {
            ConvocatoriaAreaTematica givenData = invocation.getArgument(0, ConvocatoriaAreaTematica.class);
            givenData.setObservaciones("observaciones-modificadas");
            return givenData;
          }
        });

    // when: update ConvocatoriaAreaTematica
    ConvocatoriaAreaTematica updated = service.update(convocatoriaAreaTematica);

    // then: ConvocatoriaAreaTematica is updated
    Assertions.assertThat(updated).isNotNull();
    Assertions.assertThat(updated.getId()).isNotNull();
    Assertions.assertThat(updated.getId()).isEqualTo(convocatoriaAreaTematica.getId());
    Assertions.assertThat(updated.getConvocatoria().getId())
        .isEqualTo(convocatoriaAreaTematica.getConvocatoria().getId());
    Assertions.assertThat(updated.getAreaTematica().getId())
        .isEqualTo(convocatoriaAreaTematica.getAreaTematica().getId());
    Assertions.assertThat(updated.getObservaciones()).isEqualTo("observaciones-modificadas");
  }

  @Test
  public void update_WithNoExistingId_ThrowsNotFoundException() throws Exception {
    // given: no existing id
    ConvocatoriaAreaTematica convocatoriaAreaTematica = generarConvocatoriaAreaTematica(1L, 1L, 1L);

    BDDMockito.given(convocatoriaAreaTematicaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.empty());

    Assertions.assertThatThrownBy(
        // when: update non existing ConvocatoriaAreaTematica
        () -> service.update(convocatoriaAreaTematica))
        // then: NotFoundException is thrown
        .isInstanceOf(ConvocatoriaAreaTematicaNotFoundException.class);
  }

  @Test
  public void update_WithoutId_ThrowsIllegalArgumentException() {
    // given: a ConvocatoriaAreaTematica without id filled
    ConvocatoriaAreaTematica convocatoriaAreaTematica = generarConvocatoriaAreaTematica(null, 1L, 1L);

    Assertions.assertThatThrownBy(
        // when: update ConvocatoriaAreaTematica
        () -> service.update(convocatoriaAreaTematica))
        // then: throw exception as id must be provided
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void delete_WithExistingId_ReturnsConvocatoriaAreaTematica() {
    // given: existing ConvocatoriaAreaTematica
    Long id = 1L;

    BDDMockito.given(convocatoriaAreaTematicaRepository.existsById(ArgumentMatchers.anyLong()))
        .willReturn(Boolean.TRUE);
    BDDMockito.doNothing().when(convocatoriaAreaTematicaRepository).deleteById(ArgumentMatchers.anyLong());

    Assertions.assertThatCode(
        // when: delete by existing id
        () -> service.delete(id))
        // then: no exception is thrown
        .doesNotThrowAnyException();
  }

  @Test
  public void delete_WithoutId_ThrowsIllegalArgumentException() throws Exception {
    // given: no id
    Long id = null;

    Assertions.assertThatThrownBy(
        // when: delete
        () -> service.delete(id))
        // then: IllegalArgumentException is thrown
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void delete_WithNoExistingId_ThrowsNotFoundException() throws Exception {
    // given: no existing id
    Long id = 1L;

    BDDMockito.given(convocatoriaAreaTematicaRepository.existsById(ArgumentMatchers.anyLong()))
        .willReturn(Boolean.FALSE);

    Assertions.assertThatThrownBy(
        // when: delete
        () -> service.delete(id))
        // then: NotFoundException is thrown
        .isInstanceOf(ConvocatoriaAreaTematicaNotFoundException.class);
  }

  @Test
  public void findById_WithExistingId_ReturnsConvocatoriaAreaTematica() throws Exception {
    // given: existing ConvocatoriaAreaTematica
    ConvocatoriaAreaTematica givenConvocatoriaAreaTematica = generarConvocatoriaAreaTematica(1L, 1L, 1L);

    BDDMockito.given(convocatoriaAreaTematicaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.of(givenConvocatoriaAreaTematica));

    // when: find by id ConvocatoriaAreaTematica
    ConvocatoriaAreaTematica convocatoriaAreaTematica = service.findById(givenConvocatoriaAreaTematica.getId());

    // then: returns ConvocatoriaAreaTematica
    Assertions.assertThat(convocatoriaAreaTematica).isNotNull();
    Assertions.assertThat(convocatoriaAreaTematica.getId()).isNotNull();
    Assertions.assertThat(convocatoriaAreaTematica.getId()).isEqualTo(convocatoriaAreaTematica.getId());
    Assertions.assertThat(convocatoriaAreaTematica.getConvocatoria().getId())
        .isEqualTo(convocatoriaAreaTematica.getConvocatoria().getId());
    Assertions.assertThat(convocatoriaAreaTematica.getAreaTematica().getId())
        .isEqualTo(convocatoriaAreaTematica.getAreaTematica().getId());
    Assertions.assertThat(convocatoriaAreaTematica.getObservaciones())
        .isEqualTo(convocatoriaAreaTematica.getObservaciones());
  }

  @Test
  public void findById_WithNoExistingId_ThrowsNotFoundException() throws Exception {
    // given: no existing id
    Long id = 1L;
    BDDMockito.given(convocatoriaAreaTematicaRepository.findById(ArgumentMatchers.anyLong()))
        .willReturn(Optional.empty());

    Assertions.assertThatThrownBy(
        // when: find by non existing id
        () -> service.findById(id))
        // then: NotFoundException is thrown
        .isInstanceOf(ConvocatoriaAreaTematicaNotFoundException.class);
  }

  @Test
  public void findAllByConvocatoria_ReturnsPage() {
    // given: Una lista con 37 ConvocatoriaAreaTematica para la Convocatoria
    Long convocatoriaId = 1L;
    List<ConvocatoriaAreaTematica> convocatoriasAreasTematicas = new ArrayList<>();
    for (long i = 1; i <= 37; i++) {
      convocatoriasAreasTematicas.add(generarConvocatoriaAreaTematica(i, convocatoriaId, i));
    }

    BDDMockito
        .given(convocatoriaAreaTematicaRepository
            .findAll(ArgumentMatchers.<Specification<ConvocatoriaAreaTematica>>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer(new Answer<Page<ConvocatoriaAreaTematica>>() {
          @Override
          public Page<ConvocatoriaAreaTematica> answer(InvocationOnMock invocation) throws Throwable {
            Pageable pageable = invocation.getArgument(1, Pageable.class);
            int size = pageable.getPageSize();
            int index = pageable.getPageNumber();
            int fromIndex = size * index;
            int toIndex = fromIndex + size;
            toIndex = toIndex > convocatoriasAreasTematicas.size() ? convocatoriasAreasTematicas.size() : toIndex;
            List<ConvocatoriaAreaTematica> content = convocatoriasAreasTematicas.subList(fromIndex, toIndex);
            Page<ConvocatoriaAreaTematica> page = new PageImpl<>(content, pageable, convocatoriasAreasTematicas.size());
            return page;
          }
        });

    // when: Get page=3 with pagesize=10
    Pageable paging = PageRequest.of(3, 10);
    Page<ConvocatoriaAreaTematica> page = service.findAllByConvocatoria(convocatoriaId, null, paging);

    // then: Devuelve la pagina 3 con los ConvocatoriaAreaTematica del 31 al 37
    Assertions.assertThat(page.getContent().size()).as("getContent().size()").isEqualTo(7);
    Assertions.assertThat(page.getNumber()).as("getNumber()").isEqualTo(3);
    Assertions.assertThat(page.getSize()).as("getSize()").isEqualTo(10);
    Assertions.assertThat(page.getTotalElements()).as("getTotalElements()").isEqualTo(37);
    for (int i = 31; i <= 37; i++) {
      ConvocatoriaAreaTematica ConvocatoriaAreaTematica = page.getContent()
          .get(i - (page.getSize() * page.getNumber()) - 1);
      Assertions.assertThat(ConvocatoriaAreaTematica.getId()).isEqualTo(Long.valueOf(i));
      Assertions.assertThat(ConvocatoriaAreaTematica.getConvocatoria().getId()).isEqualTo(convocatoriaId);
      Assertions.assertThat(ConvocatoriaAreaTematica.getAreaTematica().getId()).isEqualTo(i);
      Assertions.assertThat(ConvocatoriaAreaTematica.getObservaciones()).isEqualTo("observaciones-" + i);
    }
  }

  /**
   * Función que devuelve un objeto ConvocatoriaAreaTematica
   * 
   * @param convocatoriaAreaTematicaId
   * @param convocatoriaId
   * @param areaTematicaId
   * @return el objeto ConvocatoriaAreaTematica
   */
  private ConvocatoriaAreaTematica generarConvocatoriaAreaTematica(Long convocatoriaAreaTematicaId, Long convocatoriaId,
      Long areaTematicaId) {

    return ConvocatoriaAreaTematica.builder().id(convocatoriaAreaTematicaId)
        .convocatoria(Convocatoria.builder().id(convocatoriaId).build())
        .areaTematica(AreaTematica.builder().id(areaTematicaId).build())
        .observaciones("observaciones-" + convocatoriaAreaTematicaId).build();
  }
}
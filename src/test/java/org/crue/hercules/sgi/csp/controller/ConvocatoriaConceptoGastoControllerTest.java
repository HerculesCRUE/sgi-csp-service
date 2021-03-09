package org.crue.hercules.sgi.csp.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.csp.exceptions.ConvocatoriaConceptoGastoNotFoundException;
import org.crue.hercules.sgi.csp.model.ConceptoGasto;
import org.crue.hercules.sgi.csp.model.Convocatoria;
import org.crue.hercules.sgi.csp.model.ConvocatoriaConceptoGasto;
import org.crue.hercules.sgi.csp.model.ConvocatoriaConceptoGastoCodigoEc;
import org.crue.hercules.sgi.csp.service.ConvocatoriaConceptoGastoCodigoEcService;
import org.crue.hercules.sgi.csp.service.ConvocatoriaConceptoGastoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * ConvocatoriaConceptoGastoControllerTest
 */
@WebMvcTest(ConvocatoriaConceptoGastoController.class)
public class ConvocatoriaConceptoGastoControllerTest extends BaseControllerTest {

  @MockBean
  private ConvocatoriaConceptoGastoService service;
  @MockBean
  private ConvocatoriaConceptoGastoCodigoEcService convocatoriaConceptoGastoCodigoEcService;

  private static final String PATH_PARAMETER_ID = "/{id}";
  private static final String CONTROLLER_BASE_PATH = "/convocatoriaconceptogastos";
  private static final String PATH_CONCEPTO_GASTO_CODIGO_EC = "/convocatoriagastocodigoec";

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-C" })
  public void create_ReturnsConvocatoriaConceptoGasto() throws Exception {
    // given: new ConvocatoriaConceptoGasto
    ConvocatoriaConceptoGasto convocatoriaConceptoGasto = generarMockConvocatoriaConceptoGasto(null);

    BDDMockito.given(service.create(ArgumentMatchers.<ConvocatoriaConceptoGasto>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          ConvocatoriaConceptoGasto newConvocatoriaConceptoGasto = new ConvocatoriaConceptoGasto();
          BeanUtils.copyProperties(invocation.getArgument(0), newConvocatoriaConceptoGasto);
          newConvocatoriaConceptoGasto.setId(1L);
          return newConvocatoriaConceptoGasto;
        });

    // when: create ConvocatoriaConceptoGasto
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(convocatoriaConceptoGasto)))
        .andDo(MockMvcResultHandlers.print())
        // then: new ConvocatoriaConceptoGasto is created
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("conceptoGasto.nombre")
            .value(convocatoriaConceptoGasto.getConceptoGasto().getNombre()))
        .andExpect(MockMvcResultMatchers.jsonPath("conceptoGasto.descripcion")
            .value(convocatoriaConceptoGasto.getConceptoGasto().getDescripcion()))
        .andExpect(MockMvcResultMatchers.jsonPath("conceptoGasto.activo")
            .value(convocatoriaConceptoGasto.getConceptoGasto().getActivo()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-C" })
  public void create_WithId_Returns400() throws Exception {
    // given: a ConvocatoriaConceptoGasto with id filled
    ConvocatoriaConceptoGasto convocatoriaConceptoGasto = generarMockConvocatoriaConceptoGasto(1L);

    BDDMockito.given(service.create(ArgumentMatchers.<ConvocatoriaConceptoGasto>any()))
        .willThrow(new IllegalArgumentException());

    // when: create ConvocatoriaConceptoGasto
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(convocatoriaConceptoGasto)))
        .andDo(MockMvcResultHandlers.print())
        // then: 400 error
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-E" })
  public void update_ReturnsConvocatoriaConceptoGasto() throws Exception {
    // given: Existing ConvocatoriaConceptoGasto to be updated
    ConvocatoriaConceptoGasto convocatoriaConceptoGastoExistente = generarMockConvocatoriaConceptoGasto(1L);
    ConvocatoriaConceptoGasto convocatoriaConceptoGasto = generarMockConvocatoriaConceptoGasto(1L);
    convocatoriaConceptoGasto.setObservaciones("Observaciones nuevas");

    BDDMockito.given(service.update(ArgumentMatchers.<ConvocatoriaConceptoGasto>any()))
        .willAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    // when: update ConvocatoriaConceptoGasto
    mockMvc
        .perform(MockMvcRequestBuilders
            .put(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, convocatoriaConceptoGastoExistente.getId())
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convocatoriaConceptoGasto)))
        .andDo(MockMvcResultHandlers.print())
        // then: ConvocatoriaConceptoGasto is updated
        .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("observaciones").value(convocatoriaConceptoGasto.getObservaciones()))
        .andExpect(MockMvcResultMatchers.jsonPath("conceptoGasto.nombre")
            .value(convocatoriaConceptoGastoExistente.getConceptoGasto().getNombre()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-E" })
  public void update_WithNoExistingId_Returns404() throws Exception {
    // given: No existing Id
    Long id = 1L;
    ConvocatoriaConceptoGasto convocatoriaConceptoGasto = generarMockConvocatoriaConceptoGasto(1L);

    BDDMockito.willThrow(new ConvocatoriaConceptoGastoNotFoundException(id)).given(service)
        .update(ArgumentMatchers.<ConvocatoriaConceptoGasto>any());

    // when: update ConvocatoriaConceptoGasto
    mockMvc
        .perform(MockMvcRequestBuilders.put(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, id)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convocatoriaConceptoGasto)))
        .andDo(MockMvcResultHandlers.print())
        // then: 404 error
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-V" })
  public void findById_WithExistingId_ReturnsConvocatoriaConceptoGasto() throws Exception {
    // given: existing id
    BDDMockito.given(service.findById(ArgumentMatchers.anyLong())).willAnswer((InvocationOnMock invocation) -> {
      return generarMockConvocatoriaConceptoGasto(invocation.getArgument(0));
    });

    // when: find by existing id
    mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, 1L)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: response is OK
        .andExpect(MockMvcResultMatchers.status().isOk())
        // and the requested ConvocatoriaConceptoGasto is resturned as JSON object
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-V" })
  public void findById_WithNoExistingId_Returns404() throws Exception {
    // given: no existing id
    BDDMockito.given(service.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new ConvocatoriaConceptoGastoNotFoundException(1L);
    });

    // when: find by non existing id
    mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, 1L)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).
        // then: HTTP code 404 NotFound pressent
        andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-B" })
  public void delete_WithExistingId_Return204() throws Exception {
    // given: existing id
    Long id = 1L;
    BDDMockito.doNothing().when(service).delete(ArgumentMatchers.anyLong());

    // when: delete by id
    mockMvc
        .perform(MockMvcRequestBuilders.delete(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, id)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: 204
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    Mockito.verify(service, Mockito.times(1)).delete(ArgumentMatchers.anyLong());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-B" })
  public void delete_NoExistingId_Return404() throws Exception {
    // given: non existing id
    Long id = 1L;

    BDDMockito.willThrow(new ConvocatoriaConceptoGastoNotFoundException(id)).given(service)
        .delete(ArgumentMatchers.<Long>any());

    // when: delete by non existing id
    mockMvc
        .perform(MockMvcRequestBuilders.delete(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, id)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: 404 error
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  /**
   * 
   * CONVOCATORIA CÓDIGOS ECONÓMICOS
   * 
   */

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CONV-E" })
  public void findAllConvocatoriaConceptoGastoCodigoEc_ReturnsPage() throws Exception {
    // given: Una lista con 37 ConvocatoriaConceptoGastoCodigoEc para la
    // Convocatoria
    Long convocatoriaId = 1L;

    List<ConvocatoriaConceptoGastoCodigoEc> convocatoriaConceptoGastoCodigoEcs = new ArrayList<>();
    for (long i = 1; i <= 37; i++) {
      convocatoriaConceptoGastoCodigoEcs.add(generarMockConvocatoriaConceptoGastoCodigoEc(i, true));
    }

    BDDMockito.given(convocatoriaConceptoGastoCodigoEcService
        .findAllByConvocatoriaConceptoGasto(ArgumentMatchers.<Long>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer(new Answer<Page<ConvocatoriaConceptoGastoCodigoEc>>() {
          @Override
          public Page<ConvocatoriaConceptoGastoCodigoEc> answer(InvocationOnMock invocation) throws Throwable {
            List<ConvocatoriaConceptoGastoCodigoEc> content = new ArrayList<>();
            for (ConvocatoriaConceptoGastoCodigoEc convocatoriaConceptoGastoCodigoEc : convocatoriaConceptoGastoCodigoEcs) {
              content.add(convocatoriaConceptoGastoCodigoEc);
            }
            return new PageImpl<>(content);
          }
        });

    // when: Get page=3 with pagesize=10
    MvcResult requestResult = mockMvc
        .perform(MockMvcRequestBuilders
            .get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID + PATH_CONCEPTO_GASTO_CODIGO_EC, convocatoriaId)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(37))).andReturn();

    List<ConvocatoriaConceptoGastoCodigoEc> convocatoriaConceptoGastoCodigoEcResponse = mapper.readValue(
        requestResult.getResponse().getContentAsString(), new TypeReference<List<ConvocatoriaConceptoGastoCodigoEc>>() {
        });

    for (int i = 31; i <= 37; i++) {
      ConvocatoriaConceptoGastoCodigoEc convocatoriaConceptoGastoCodigoEc = convocatoriaConceptoGastoCodigoEcResponse
          .get(i - 1);
      Assertions.assertThat(convocatoriaConceptoGastoCodigoEc.getCodigoEconomicoRef()).isEqualTo("cod-" + i);
    }
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CONV-E" })
  public void findAllConvocatoriaConceptoGastoCodigoEc_EmptyList_Returns204() throws Exception {
    // given: Una lista vacia de ConvocatoriaConceptoGastoCodigoEc para la
    // Convocatoria

    BDDMockito.given(convocatoriaConceptoGastoCodigoEcService
        .findAllByConvocatoriaConceptoGasto(ArgumentMatchers.<Long>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(new PageImpl<>(Collections.emptyList()));

    // when: Get page=0 with pagesize=10
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID + PATH_CONCEPTO_GASTO_CODIGO_EC, 1L)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: Devuelve un 204
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  private ConvocatoriaConceptoGasto generarMockConvocatoriaConceptoGasto(Long id) {
    return generarMockConvocatoriaConceptoGasto(id, true);
  }

  /**
   * Función que devuelve un objeto ConvocatoriaConceptoGasto
   * 
   * @param id        id del ConvocatoriaConceptoGasto
   * @param permitido true/false
   * @return el objeto ConvocatoriaConceptoGasto
   */
  private ConvocatoriaConceptoGasto generarMockConvocatoriaConceptoGasto(Long id, Boolean permitido) {
    ConceptoGasto conceptoGasto = new ConceptoGasto();
    conceptoGasto.setDescripcion("Descripcion");
    conceptoGasto.setNombre("nombre-" + (id != null ? id : 1));
    conceptoGasto.setActivo(true);

    ConvocatoriaConceptoGasto convocatoriaConceptoGasto = new ConvocatoriaConceptoGasto();
    convocatoriaConceptoGasto.setId(id);
    convocatoriaConceptoGasto.setConceptoGasto(conceptoGasto);
    convocatoriaConceptoGasto.setPermitido(permitido);
    convocatoriaConceptoGasto
        .setConvocatoria(Convocatoria.builder().id(id).activo(Boolean.TRUE).codigo("codigo" + id).build());
    convocatoriaConceptoGasto.setObservaciones("Obs-" + (id != null ? id : 1));

    return convocatoriaConceptoGasto;
  }

  /**
   * Función que devuelve un objeto ConvocatoriaConceptoGastoCodigoEc
   * 
   * @param id id del ConvocatoriaConceptoGastoCodigoEc
   * @return el objeto ConvocatoriaConceptoGastoCodigoEc
   */
  private ConvocatoriaConceptoGastoCodigoEc generarMockConvocatoriaConceptoGastoCodigoEc(Long id, Boolean permitido) {

    ConvocatoriaConceptoGasto convocatoriaConceptoGasto = generarMockConvocatoriaConceptoGasto(id == null ? 1 : id,
        permitido);

    ConvocatoriaConceptoGastoCodigoEc convocatoriaConceptoGastoCodigoEc = new ConvocatoriaConceptoGastoCodigoEc();
    convocatoriaConceptoGastoCodigoEc.setId(id);
    convocatoriaConceptoGastoCodigoEc.setCodigoEconomicoRef("cod-" + id);
    convocatoriaConceptoGastoCodigoEc.setConvocatoriaConceptoGasto(convocatoriaConceptoGasto);
    convocatoriaConceptoGastoCodigoEc.setFechaInicio(LocalDate.now());
    convocatoriaConceptoGastoCodigoEc.setFechaFin(LocalDate.now());

    return convocatoriaConceptoGastoCodigoEc;
  }

}

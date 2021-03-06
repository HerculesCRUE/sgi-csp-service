package org.crue.hercules.sgi.csp.controller;

import java.time.Instant;

import org.crue.hercules.sgi.csp.exceptions.ConvocatoriaHitoNotFoundException;
import org.crue.hercules.sgi.csp.model.ConvocatoriaHito;
import org.crue.hercules.sgi.csp.model.TipoHito;
import org.crue.hercules.sgi.csp.service.ConvocatoriaHitoService;
import org.crue.hercules.sgi.csp.service.TipoHitoService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * ConvocatoriaHitoControllerTest
 */
@WebMvcTest(ConvocatoriaHitoController.class)
public class ConvocatoriaHitoControllerTest extends BaseControllerTest {

  @MockBean
  private ConvocatoriaHitoService service;

  @MockBean
  private TipoHitoService tipoHitoService;

  private static final String PATH_PARAMETER_ID = "/{id}";
  private static final String CONTROLLER_BASE_PATH = "/convocatoriahitos";

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CHIT-C" })
  public void create_ReturnsConvocatoriaHito() throws Exception {
    // given: new ConvocatoriaHito

    ConvocatoriaHito convocatoriaHito = generarMockConvocatoriaHito(null);
    convocatoriaHito.getTipoHito().setId(1L);
    convocatoriaHito.setConvocatoriaId(1L);

    BDDMockito.given(service.create(ArgumentMatchers.<ConvocatoriaHito>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          ConvocatoriaHito newConvocatoriaHito = new ConvocatoriaHito();
          BeanUtils.copyProperties(invocation.getArgument(0), newConvocatoriaHito);
          newConvocatoriaHito.setId(1L);
          return newConvocatoriaHito;
        });

    // when: create ConvocatoriaHito
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(convocatoriaHito)))
        .andDo(MockMvcResultHandlers.print())
        // then: new ConvocatoriaHito is created
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("convocatoriaId").value(convocatoriaHito.getConvocatoriaId()))
        .andExpect(MockMvcResultMatchers.jsonPath("fecha").value("2020-10-19T00:00:00Z"))
        .andExpect(MockMvcResultMatchers.jsonPath("comentario").value("comentario" + convocatoriaHito.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("tipoHito.id").value(convocatoriaHito.getTipoHito().getId()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CHIT-C" })
  public void create_WithId_Returns400() throws Exception {
    // given: a ConvocatoriaHito with id filled
    ConvocatoriaHito convocatoriaHito = generarMockConvocatoriaHito(1L);

    BDDMockito.given(service.create(ArgumentMatchers.<ConvocatoriaHito>any()))
        .willThrow(new IllegalArgumentException());

    // when: create ConvocatoriaHito
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(convocatoriaHito)))
        .andDo(MockMvcResultHandlers.print())
        // then: 400 error
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CHIT-E" })
  public void update_ReturnsConvocatoriaHito() throws Exception {
    // given: Existing ConvocatoriaHito to be updated
    ConvocatoriaHito convocatoriaHitoExistente = generarMockConvocatoriaHito(1L);
    ConvocatoriaHito convocatoriaHito = generarMockConvocatoriaHito(1L);

    BDDMockito.given(service.update(ArgumentMatchers.<ConvocatoriaHito>any()))
        .willAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    // when: update ConvocatoriaHito
    mockMvc
        .perform(MockMvcRequestBuilders.put(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, convocatoriaHitoExistente.getId())
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convocatoriaHito)))
        .andDo(MockMvcResultHandlers.print())
        // then: ConvocatoriaHito is updated
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(convocatoriaHitoExistente.getId()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("convocatoriaId").value(convocatoriaHitoExistente.getConvocatoriaId()))
        .andExpect(MockMvcResultMatchers.jsonPath("fecha").value("2020-10-19T00:00:00Z"))
        .andExpect(MockMvcResultMatchers.jsonPath("comentario").value("comentario1"))
        .andExpect(MockMvcResultMatchers.jsonPath("tipoHito.id").value(convocatoriaHito.getTipoHito().getId()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CHIT-E" })
  public void update_WithNoExistingId_Returns404() throws Exception {
    // given: No existing Id
    Long id = 1L;
    ConvocatoriaHito convocatoriaHito = generarMockConvocatoriaHito(1L);

    BDDMockito.willThrow(new ConvocatoriaHitoNotFoundException(id)).given(service)
        .update(ArgumentMatchers.<ConvocatoriaHito>any());

    // when: update ConvocatoriaHito
    mockMvc
        .perform(MockMvcRequestBuilders.put(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, id)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convocatoriaHito)))
        .andDo(MockMvcResultHandlers.print())
        // then: 404 error
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CHIT-B" })
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
  @WithMockUser(username = "user", authorities = { "CSP-CHIT-B" })
  public void delete_NoExistingId_Return404() throws Exception {
    // given: non existing id
    Long id = 1L;

    BDDMockito.willThrow(new ConvocatoriaHitoNotFoundException(id)).given(service).delete(ArgumentMatchers.<Long>any());

    // when: delete by non existing id
    mockMvc
        .perform(MockMvcRequestBuilders.delete(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, id)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: 404 error
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CHIT-V" })
  public void findById_WithExistingId_ReturnsConvocatoriaHito() throws Exception {
    // given: existing id
    BDDMockito.given(service.findById(ArgumentMatchers.anyLong())).willAnswer((InvocationOnMock invocation) -> {
      return generarMockConvocatoriaHito(invocation.getArgument(0));
    });

    // when: find by existing id
    mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, 1L)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: response is OK
        .andExpect(MockMvcResultMatchers.status().isOk())
        // and the requested ConvocatoriaHito is resturned as JSON object
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
        .andExpect(MockMvcResultMatchers.jsonPath("convocatoriaId").value(1L))
        .andExpect(MockMvcResultMatchers.jsonPath("fecha").value("2020-10-19T00:00:00Z"))
        .andExpect(MockMvcResultMatchers.jsonPath("comentario").value("comentario1"));

  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CHIT-V" })
  public void findById_WithNoExistingId_Returns404() throws Exception {
    // given: no existing id
    BDDMockito.given(service.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new ConvocatoriaHitoNotFoundException(1L);
    });

    // when: find by non existing id
    mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, 1L)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).
        // then: HTTP code 404 NotFound pressent
        andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  /**
   * Función que devuelve un objeto ConvocatoriaHito
   * 
   * @param id id del ConvocatoriaHito
   * @return el objeto ConvocatoriaHito
   */
  private ConvocatoriaHito generarMockConvocatoriaHito(Long id) {
    TipoHito tipoHito = new TipoHito();
    tipoHito.setId(id == null ? 1 : id);
    tipoHito.setActivo(true);

    ConvocatoriaHito convocatoriaHito = new ConvocatoriaHito();
    convocatoriaHito.setId(id);
    convocatoriaHito.setConvocatoriaId(id == null ? 1 : id);
    convocatoriaHito.setFecha(Instant.parse("2020-10-19T00:00:00Z"));
    convocatoriaHito.setComentario("comentario" + id);
    convocatoriaHito.setGeneraAviso(true);
    convocatoriaHito.setTipoHito(tipoHito);

    return convocatoriaHito;
  }

}

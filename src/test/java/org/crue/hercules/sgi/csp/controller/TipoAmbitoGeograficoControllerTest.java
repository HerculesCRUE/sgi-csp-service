package org.crue.hercules.sgi.csp.controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.csp.exceptions.TipoAmbitoGeograficoNotFoundException;
import org.crue.hercules.sgi.csp.model.TipoAmbitoGeografico;
import org.crue.hercules.sgi.csp.service.TipoAmbitoGeograficoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.invocation.InvocationOnMock;
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
 * TipoAmbitoGeograficoControllerTest
 */
@WebMvcTest(TipoAmbitoGeograficoController.class)
public class TipoAmbitoGeograficoControllerTest extends BaseControllerTest {

  @MockBean
  private TipoAmbitoGeograficoService service;

  private static final String PATH_PARAMETER_ID = "/{id}";
  private static final String CONTROLLER_BASE_PATH = "/tipoambitogeograficos";

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-C" })
  public void create_ReturnsModeloTipoAmbitoGeografico() throws Exception {
    // given: new TipoAmbitoGeografico
    TipoAmbitoGeografico tipoAmbitoGeografico = generarMockTipoAmbitoGeografico(1L);

    BDDMockito.given(service.create(ArgumentMatchers.<TipoAmbitoGeografico>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          TipoAmbitoGeografico newTipoAmbitoGeografico = new TipoAmbitoGeografico();
          BeanUtils.copyProperties(invocation.getArgument(0), newTipoAmbitoGeografico);
          newTipoAmbitoGeografico.setId(1L);
          return newTipoAmbitoGeografico;
        });

    // when: create TipoAmbitoGeografico
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(tipoAmbitoGeografico)))
        .andDo(MockMvcResultHandlers.print())
        // then: new TipoAmbitoGeografico is created
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("nombre").value(tipoAmbitoGeografico.getNombre()))
        .andExpect(MockMvcResultMatchers.jsonPath("activo").value(tipoAmbitoGeografico.getActivo()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-C" })
  public void create_WithId_Returns400() throws Exception {
    // given: a TipoAmbitoGeografico with id filled
    TipoAmbitoGeografico tipoAmbitoGeografico = generarMockTipoAmbitoGeografico(1L);

    BDDMockito.given(service.create(ArgumentMatchers.<TipoAmbitoGeografico>any()))
        .willThrow(new IllegalArgumentException());

    // when: create TipoAmbitoGeografico
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(tipoAmbitoGeografico)))
        .andDo(MockMvcResultHandlers.print())
        // then: 400 error
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-E" })
  public void update_ReturnsTipoAmbitoGeografico() throws Exception {
    // given: Existing TipoAmbitoGeografico to be updated
    TipoAmbitoGeografico tipoAmbitoGeograficoExistente = generarMockTipoAmbitoGeografico(1L);
    TipoAmbitoGeografico tipoAmbitoGeografico = generarMockTipoAmbitoGeografico(1L);
    tipoAmbitoGeografico.setNombre("nuevo-nombre");

    BDDMockito.given(service.update(ArgumentMatchers.<TipoAmbitoGeografico>any()))
        .willAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    // when: update TipoAmbitoGeografico
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, tipoAmbitoGeograficoExistente.getId())
                .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(tipoAmbitoGeografico)))
        .andDo(MockMvcResultHandlers.print())
        // then: TipoAmbitoGeografico is updated
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(tipoAmbitoGeograficoExistente.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("nombre").value(tipoAmbitoGeografico.getNombre()))
        .andExpect(MockMvcResultMatchers.jsonPath("activo").value(tipoAmbitoGeograficoExistente.getActivo()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-E" })
  public void update_WithNoExistingId_Returns404() throws Exception {
    // given: No existing Id
    Long id = 1L;
    TipoAmbitoGeografico tipoAmbitoGeografico = generarMockTipoAmbitoGeografico(1L);

    BDDMockito.willThrow(new TipoAmbitoGeograficoNotFoundException(id)).given(service)
        .update(ArgumentMatchers.<TipoAmbitoGeografico>any());

    // when: update TipoAmbitoGeografico
    mockMvc
        .perform(MockMvcRequestBuilders.put(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, id)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(tipoAmbitoGeografico)))
        .andDo(MockMvcResultHandlers.print())
        // then: 404 error
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-B" })
  public void delete_WithExistingId_Return204() throws Exception {
    // given: existing id
    TipoAmbitoGeografico tipoAmbitoGeografico = generarMockTipoAmbitoGeografico(1L);
    BDDMockito.given(service.disable(ArgumentMatchers.<Long>any())).willAnswer((InvocationOnMock invocation) -> {
      TipoAmbitoGeografico tipoAmbitoGeograficoDisabled = new TipoAmbitoGeografico();
      BeanUtils.copyProperties(tipoAmbitoGeografico, tipoAmbitoGeograficoDisabled);
      tipoAmbitoGeograficoDisabled.setActivo(false);
      return tipoAmbitoGeograficoDisabled;
    });

    // when: delete by id
    mockMvc
        .perform(MockMvcRequestBuilders.delete(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, tipoAmbitoGeografico.getId())
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: 204
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-B" })
  public void delete_NoExistingId_Return404() throws Exception {
    // given: non existing id
    Long id = 1L;

    BDDMockito.willThrow(new TipoAmbitoGeograficoNotFoundException(id)).given(service)
        .disable(ArgumentMatchers.<Long>any());

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
  @WithMockUser(username = "user", authorities = { "CSP-TDOC-V" })
  public void findAll_ReturnsPage() throws Exception {
    // given: Una lista con 37 TipoAmbitoGeografico
    List<TipoAmbitoGeografico> tipoAmbitoGeograficos = new ArrayList<>();
    for (long i = 1; i <= 37; i++) {
      tipoAmbitoGeograficos.add(generarMockTipoAmbitoGeografico(i, "TipoAmbitoGeografico" + String.format("%03d", i)));
    }

    Integer page = 3;
    Integer pageSize = 10;

    BDDMockito.given(service.findAll(ArgumentMatchers.<String>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          Pageable pageable = invocation.getArgument(1, Pageable.class);
          int size = pageable.getPageSize();
          int index = pageable.getPageNumber();
          int fromIndex = size * index;
          int toIndex = fromIndex + size;
          toIndex = toIndex > tipoAmbitoGeograficos.size() ? tipoAmbitoGeograficos.size() : toIndex;
          List<TipoAmbitoGeografico> content = tipoAmbitoGeograficos.subList(fromIndex, toIndex);
          Page<TipoAmbitoGeografico> pageResponse = new PageImpl<>(content, pageable, tipoAmbitoGeograficos.size());
          return pageResponse;
        });

    // when: Get page=3 with pagesize=10
    MvcResult requestResult = mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .header("X-Page", page).header("X-Page-Size", pageSize).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: Devuelve la pagina 3 con los TipoAmbitoGeografico del 31 al 37
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.header().string("X-Page", "3"))
        .andExpect(MockMvcResultMatchers.header().string("X-Page-Total-Count", "7"))
        .andExpect(MockMvcResultMatchers.header().string("X-Page-Size", "10"))
        .andExpect(MockMvcResultMatchers.header().string("X-Total-Count", "37"))
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(7))).andReturn();

    List<TipoAmbitoGeografico> tipoAmbitoGeograficosResponse = mapper
        .readValue(requestResult.getResponse().getContentAsString(), new TypeReference<List<TipoAmbitoGeografico>>() {
        });

    for (int i = 31; i <= 37; i++) {
      TipoAmbitoGeografico tipoAmbitoGeografico = tipoAmbitoGeograficosResponse.get(i - (page * pageSize) - 1);
      Assertions.assertThat(tipoAmbitoGeografico.getNombre())
          .isEqualTo("TipoAmbitoGeografico" + String.format("%03d", i));
    }
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-TDOC-V" })
  public void findAll_EmptyList_Returns204() throws Exception {
    // given: Una lista vacia de TipoAmbitoGeografico
    List<TipoAmbitoGeografico> tipoAmbitoGeograficos = new ArrayList<>();

    Integer page = 0;
    Integer pageSize = 10;

    BDDMockito.given(service.findAll(ArgumentMatchers.<String>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          Pageable pageable = invocation.getArgument(1, Pageable.class);
          Page<TipoAmbitoGeografico> pageResponse = new PageImpl<>(tipoAmbitoGeograficos, pageable, 0);
          return pageResponse;
        });

    // when: Get page=0 with pagesize=10
    mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .header("X-Page", page).header("X-Page-Size", pageSize).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: Devuelve un 204
        .andExpect(MockMvcResultMatchers.status().isNoContent());

  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-TDOC-V" })
  public void findAllTodos_ReturnsPage() throws Exception {
    // given: Una lista con 37 TipoAmbitoGeografico
    List<TipoAmbitoGeografico> tipoAmbitoGeograficos = new ArrayList<>();
    for (long i = 1; i <= 37; i++) {
      tipoAmbitoGeograficos.add(generarMockTipoAmbitoGeografico(i, "TipoAmbitoGeografico" + String.format("%03d", i)));
    }

    Integer page = 3;
    Integer pageSize = 10;

    BDDMockito.given(service.findAllTodos(ArgumentMatchers.<String>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          Pageable pageable = invocation.getArgument(1, Pageable.class);
          int size = pageable.getPageSize();
          int index = pageable.getPageNumber();
          int fromIndex = size * index;
          int toIndex = fromIndex + size;
          toIndex = toIndex > tipoAmbitoGeograficos.size() ? tipoAmbitoGeograficos.size() : toIndex;
          List<TipoAmbitoGeografico> content = tipoAmbitoGeograficos.subList(fromIndex, toIndex);
          Page<TipoAmbitoGeografico> pageResponse = new PageImpl<>(content, pageable, tipoAmbitoGeograficos.size());
          return pageResponse;
        });

    // when: Get page=3 with pagesize=10
    MvcResult requestResult = mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + "/todos")
            .with(SecurityMockMvcRequestPostProcessors.csrf()).header("X-Page", page).header("X-Page-Size", pageSize)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: Devuelve la pagina 3 con los TipoAmbitoGeografico del 31 al 37
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.header().string("X-Page", "3"))
        .andExpect(MockMvcResultMatchers.header().string("X-Page-Total-Count", "7"))
        .andExpect(MockMvcResultMatchers.header().string("X-Page-Size", "10"))
        .andExpect(MockMvcResultMatchers.header().string("X-Total-Count", "37"))
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(7))).andReturn();

    List<TipoAmbitoGeografico> tipoAmbitoGeograficosResponse = mapper
        .readValue(requestResult.getResponse().getContentAsString(), new TypeReference<List<TipoAmbitoGeografico>>() {
        });

    for (int i = 31; i <= 37; i++) {
      TipoAmbitoGeografico tipoAmbitoGeografico = tipoAmbitoGeograficosResponse.get(i - (page * pageSize) - 1);
      Assertions.assertThat(tipoAmbitoGeografico.getNombre())
          .isEqualTo("TipoAmbitoGeografico" + String.format("%03d", i));
    }
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-TDOC-V" })
  public void findAllTodos_EmptyList_Returns204() throws Exception {
    // given: Una lista vacia de TipoAmbitoGeografico
    List<TipoAmbitoGeografico> tipoAmbitoGeograficos = new ArrayList<>();

    Integer page = 0;
    Integer pageSize = 10;

    BDDMockito.given(service.findAllTodos(ArgumentMatchers.<String>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          Pageable pageable = invocation.getArgument(1, Pageable.class);
          Page<TipoAmbitoGeografico> pageResponse = new PageImpl<>(tipoAmbitoGeograficos, pageable, 0);
          return pageResponse;
        });

    // when: Get page=0 with pagesize=10
    mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + "/todos")
            .with(SecurityMockMvcRequestPostProcessors.csrf()).header("X-Page", page).header("X-Page-Size", pageSize)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: Devuelve un 204
        .andExpect(MockMvcResultMatchers.status().isNoContent());

  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-V" })
  public void findById_WithExistingId_ReturnsTipoAmbitoGeografico() throws Exception {
    // given: existing id
    BDDMockito.given(service.findById(ArgumentMatchers.anyLong())).willAnswer((InvocationOnMock invocation) -> {
      return generarMockTipoAmbitoGeografico(invocation.getArgument(0));
    });

    // when: find by existing id
    mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, 1L)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: response is OK
        .andExpect(MockMvcResultMatchers.status().isOk())
        // and the requested TipoAmbitoGeografico is resturned as JSON object
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-V" })
  public void findById_WithNoExistingId_Returns404() throws Exception {
    // given: no existing id
    BDDMockito.given(service.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new TipoAmbitoGeograficoNotFoundException(1L);
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
   * Función que devuelve un objeto TipoAmbitoGeografico
   * 
   * @param id id del TipoAmbitoGeografico
   * @return el objeto TipoAmbitoGeografico
   */
  private TipoAmbitoGeografico generarMockTipoAmbitoGeografico(Long id) {
    return generarMockTipoAmbitoGeografico(id, "nombre-" + id);
  }

  /**
   * Función que devuelve un objeto TipoAmbitoGeografico
   * 
   * @param id     id del TipoAmbitoGeografico
   * @param nombre nombre del TipoAmbitoGeografico
   * @return el objeto TipoAmbitoGeografico
   */
  private TipoAmbitoGeografico generarMockTipoAmbitoGeografico(Long id, String nombre) {
    TipoAmbitoGeografico tipoAmbitoGeografico = new TipoAmbitoGeografico();
    tipoAmbitoGeografico.setId(id);
    tipoAmbitoGeografico.setNombre(nombre);
    tipoAmbitoGeografico.setActivo(true);

    return tipoAmbitoGeografico;
  }

}

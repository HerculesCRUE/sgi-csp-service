package org.crue.hercules.sgi.csp.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.csp.exceptions.ProyectoSocioNotFoundException;
import org.crue.hercules.sgi.csp.model.EstadoProyecto;
import org.crue.hercules.sgi.csp.model.ModeloEjecucion;
import org.crue.hercules.sgi.csp.model.Proyecto;
import org.crue.hercules.sgi.csp.model.ProyectoSocio;
import org.crue.hercules.sgi.csp.model.ProyectoSocioEquipo;
import org.crue.hercules.sgi.csp.model.ProyectoSocioPeriodoJustificacion;
import org.crue.hercules.sgi.csp.model.ProyectoSocioPeriodoPago;
import org.crue.hercules.sgi.csp.model.RolProyecto;
import org.crue.hercules.sgi.csp.model.RolSocio;
import org.crue.hercules.sgi.csp.service.ProyectoSocioEquipoService;
import org.crue.hercules.sgi.csp.service.ProyectoSocioPeriodoJustificacionService;
import org.crue.hercules.sgi.csp.service.ProyectoSocioPeriodoPagoService;
import org.crue.hercules.sgi.csp.service.ProyectoSocioService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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
 * ProyectoSocioControllerTest
 */
@WebMvcTest(ProyectoSocioController.class)
public class ProyectoSocioControllerTest extends BaseControllerTest {

  @MockBean
  private ProyectoSocioService service;

  /** ProyectoSocioPeriodoJustificacionService service */
  @MockBean
  private ProyectoSocioPeriodoJustificacionService proyectoSocioPeriodoJustificacionService;

  @MockBean
  private ProyectoSocioEquipoService proyectoSocioEquipoService;

  @MockBean
  private ProyectoSocioPeriodoPagoService proyectoSocioPeriodoPagoService;

  private static final String PATH_PARAMETER_ID = "/{id}";
  private static final String CONTROLLER_BASE_PATH = "/proyectosocios";

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-PRO-C" })
  public void create_ReturnsProyectoSocio() throws Exception {
    // given: new ProyectoSocio
    ProyectoSocio proyectoSocio = generarMockProyectoSocio(1L);

    BDDMockito.given(service.create(ArgumentMatchers.<ProyectoSocio>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          ProyectoSocio givenData = invocation.getArgument(0, ProyectoSocio.class);
          ProyectoSocio newData = new ProyectoSocio();
          BeanUtils.copyProperties(givenData, newData);
          newData.setId(proyectoSocio.getId());
          return newData;
        });

    // when: create ProyectoSocio
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(proyectoSocio)))
        .andDo(MockMvcResultHandlers.print())
        // then: new ProyectoSocio is created
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("proyecto.id").value(proyectoSocio.getProyecto().getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("empresaRef").value(proyectoSocio.getEmpresaRef()))
        .andExpect(MockMvcResultMatchers.jsonPath("rolSocio.id").value(proyectoSocio.getRolSocio().getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("fechaInicio").value(proyectoSocio.getFechaInicio().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("fechaFin").value(proyectoSocio.getFechaFin().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("numInvestigadores").value(proyectoSocio.getNumInvestigadores()))
        .andExpect(MockMvcResultMatchers.jsonPath("importeConcedido").value(proyectoSocio.getImporteConcedido()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-PRO-C" })
  public void create_WithId_Returns400() throws Exception {
    // given: a ProyectoSocio with id filled
    ProyectoSocio newProyectoSocio = generarMockProyectoSocio(1L);

    BDDMockito.given(service.create(ArgumentMatchers.<ProyectoSocio>any())).willThrow(new IllegalArgumentException());

    // when: create ProyectoSocio
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(newProyectoSocio)))
        .andDo(MockMvcResultHandlers.print())
        // then: 400 error
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-PRO-E" })
  public void update_WithExistingId_ReturnsProyectoSocio() throws Exception {
    // given: existing ProyectoSocio
    ProyectoSocio proyectoSocioExistente = generarMockProyectoSocio(1L);
    ProyectoSocio proyectoSocio = generarMockProyectoSocio(1L);
    proyectoSocio.setNumInvestigadores(10);

    BDDMockito.given(service.findById(ArgumentMatchers.<Long>any())).willReturn(proyectoSocioExistente);
    BDDMockito.given(service.update(ArgumentMatchers.<ProyectoSocio>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          ProyectoSocio givenData = invocation.getArgument(0, ProyectoSocio.class);
          return givenData;
        });

    // when: update ProyectoSocio
    mockMvc
        .perform(MockMvcRequestBuilders.put(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, proyectoSocioExistente.getId())
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(proyectoSocio)))
        .andDo(MockMvcResultHandlers.print())
        // then: ProyectoSocio is updated
        .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(proyectoSocioExistente.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("proyecto.id").value(proyectoSocioExistente.getProyecto().getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("empresaRef").value(proyectoSocioExistente.getEmpresaRef()))
        .andExpect(MockMvcResultMatchers.jsonPath("rolSocio.id").value(proyectoSocioExistente.getRolSocio().getId()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("fechaInicio").value(proyectoSocioExistente.getFechaInicio().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("fechaFin").value(proyectoSocioExistente.getFechaFin().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("numInvestigadores").value(proyectoSocio.getNumInvestigadores()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("importeConcedido").value(proyectoSocioExistente.getImporteConcedido()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-PRO-E" })
  public void update_WithNoExistingId_Returns404() throws Exception {
    // given: a ProyectoSocio with non existing id
    ProyectoSocio proyectoSocio = generarMockProyectoSocio(1L);

    BDDMockito.willThrow(new ProyectoSocioNotFoundException(proyectoSocio.getId())).given(service)
        .findById(ArgumentMatchers.<Long>any());
    BDDMockito.given(service.update(ArgumentMatchers.<ProyectoSocio>any()))
        .willThrow(new ProyectoSocioNotFoundException(proyectoSocio.getId()));

    // when: update ProyectoSocio
    mockMvc
        .perform(MockMvcRequestBuilders.put(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, proyectoSocio.getId())
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(proyectoSocio)))
        .andDo(MockMvcResultHandlers.print())
        // then: 404 error
        .andExpect(MockMvcResultMatchers.status().isNotFound());
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

    BDDMockito.willThrow(new ProyectoSocioNotFoundException(id)).given(service).delete(ArgumentMatchers.<Long>any());

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
  @WithMockUser(username = "user", authorities = { "CSP-PRO-V" })
  public void existsById_WithExistingId_Returns200() throws Exception {
    // given: existing id
    Long id = 1L;
    BDDMockito.given(service.existsById(ArgumentMatchers.anyLong())).willReturn(Boolean.TRUE);

    // when: exists by id
    mockMvc
        .perform(MockMvcRequestBuilders.head(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, id)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andDo(MockMvcResultHandlers.print())
        // then: 200 OK
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-PRO-V" })
  public void existsById_WithNoExistingId_Returns204() throws Exception {
    // given: no existing id
    Long id = 1L;
    BDDMockito.given(service.existsById(ArgumentMatchers.anyLong())).willReturn(Boolean.FALSE);

    // when: exists by id
    mockMvc
        .perform(MockMvcRequestBuilders.head(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, id)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andDo(MockMvcResultHandlers.print())
        // then: 204 No Content
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-RSOC-V" })
  public void findById_WithExistingId_ReturnsProyectoSocio() throws Exception {
    // given: existing id
    ProyectoSocio proyectoSocioExistente = generarMockProyectoSocio(1L);
    BDDMockito.given(service.findById(ArgumentMatchers.<Long>any())).willReturn(proyectoSocioExistente);

    // when: find by existing id
    mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, 1L)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: response is OK
        .andExpect(MockMvcResultMatchers.status().isOk())
        // and the requested ProyectoSocio is resturned as JSON object
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-RSOC-V" })
  public void findById_WithNoExistingId_Returns404() throws Exception {
    // given: no existing id
    BDDMockito.given(service.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new ProyectoSocioNotFoundException(1L);
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
   * 
   * proyecto socio periodo pago
   * 
   */

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CENTGES-V" })
  public void findAllProyectoSocioPeriodoPago_ReturnsPage() throws Exception {
    // given: Una lista con 37 ProyectoSocioPeriodoPago para la
    // ProyectoSocioPeriodoPago
    Long proyectoSocioId = 1L;

    List<ProyectoSocioPeriodoPago> proyectoSocioPeriodoPagos = new ArrayList<>();
    for (long i = 1; i <= 37; i++) {
      ProyectoSocioPeriodoPago proyectoSocioPeriodoPago = generarMockProyectoSocioPeriodoPago(i);
      proyectoSocioPeriodoPago.setImporte(new BigDecimal(i));
      proyectoSocioPeriodoPagos.add(proyectoSocioPeriodoPago);

    }

    Integer page = 3;
    Integer pageSize = 10;

    BDDMockito
        .given(proyectoSocioPeriodoPagoService.findAllByProyectoSocio(ArgumentMatchers.<Long>any(),
            ArgumentMatchers.<String>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          Pageable pageable = invocation.getArgument(2, Pageable.class);
          int size = pageable.getPageSize();
          int index = pageable.getPageNumber();
          int fromIndex = size * index;
          int toIndex = fromIndex + size;
          toIndex = toIndex > proyectoSocioPeriodoPagos.size() ? proyectoSocioPeriodoPagos.size() : toIndex;
          List<ProyectoSocioPeriodoPago> content = proyectoSocioPeriodoPagos.subList(fromIndex, toIndex);
          Page<ProyectoSocioPeriodoPago> pageResponse = new PageImpl<>(content, pageable,
              proyectoSocioPeriodoPagos.size());
          return pageResponse;
        });

    // when: Get page=3 with pagesize=10
    MvcResult requestResult = mockMvc
        .perform(MockMvcRequestBuilders
            .get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID + "/proyectosocioperiodopagos", proyectoSocioId)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).header("X-Page", page).header("X-Page-Size", pageSize)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: Devuelve la pagina 3 con los ProyectoSocioPeriodoPago del 31 al
        // 37
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.header().string("X-Page", "3"))
        .andExpect(MockMvcResultMatchers.header().string("X-Page-Total-Count", "7"))
        .andExpect(MockMvcResultMatchers.header().string("X-Page-Size", "10"))
        .andExpect(MockMvcResultMatchers.header().string("X-Total-Count", "37"))
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(7))).andReturn();

    List<ProyectoSocioPeriodoPago> proyectoSocioResponse = mapper.readValue(
        requestResult.getResponse().getContentAsString(), new TypeReference<List<ProyectoSocioPeriodoPago>>() {
        });

    for (int i = 31; i <= 37; i++) {
      ProyectoSocioPeriodoPago proyectoPeriodoPagoRecuperado = proyectoSocioResponse.get(i - (page * pageSize) - 1);
      Assertions.assertThat(proyectoPeriodoPagoRecuperado.getImporte()).isEqualTo(new BigDecimal(i));
    }
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CENTGES-V" })
  public void findAllProyectoSocioPeriodoPago_Returns204() throws Exception {
    // given: Una lista vacia de ProyectoSocioPeriodoPago para la
    // ProyectoSocio
    Long proyectoSocioId = 1L;
    List<ProyectoSocioPeriodoPago> proyectoSocioPeriodoPago = new ArrayList<>();

    Integer page = 0;
    Integer pageSize = 10;

    BDDMockito
        .given(proyectoSocioPeriodoPagoService.findAllByProyectoSocio(ArgumentMatchers.<Long>any(),
            ArgumentMatchers.<String>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          Pageable pageable = invocation.getArgument(2, Pageable.class);
          Page<ProyectoSocioPeriodoPago> pageResponse = new PageImpl<>(proyectoSocioPeriodoPago, pageable, 0);
          return pageResponse;
        });

    // when: Get page=0 with pagesize=10
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID + "/proyectosocioperiodopagos", proyectoSocioId)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).header("X-Page", page).header("X-Page-Size", pageSize)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: Devuelve un 204
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  /**
   * 
   * proyecto socio equipo
   * 
   */

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CENTGES-V" })
  public void findAllProyectoSocioEquipo_ReturnsPage() throws Exception {
    // given: Una lista con 37 ProyectoSocioEquipo para la
    // ProyectoSocioEquipo
    Long proyectoSocioId = 1L;

    List<ProyectoSocioEquipo> proyectoSocioEquipos = new ArrayList<>();
    for (long i = 1; i <= 37; i++) {
      ProyectoSocioEquipo proyectoSocioEquipo = generarMockProyectoSocioEquipo(i);
      proyectoSocioEquipo.setPersonaRef("00" + i);
      proyectoSocioEquipos.add(proyectoSocioEquipo);

    }

    Integer page = 3;
    Integer pageSize = 10;

    BDDMockito
        .given(proyectoSocioEquipoService.findAllByProyectoSocio(ArgumentMatchers.<Long>any(),
            ArgumentMatchers.<String>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          Pageable pageable = invocation.getArgument(2, Pageable.class);
          int size = pageable.getPageSize();
          int index = pageable.getPageNumber();
          int fromIndex = size * index;
          int toIndex = fromIndex + size;
          toIndex = toIndex > proyectoSocioEquipos.size() ? proyectoSocioEquipos.size() : toIndex;
          List<ProyectoSocioEquipo> content = proyectoSocioEquipos.subList(fromIndex, toIndex);
          Page<ProyectoSocioEquipo> pageResponse = new PageImpl<>(content, pageable, proyectoSocioEquipos.size());
          return pageResponse;
        });

    // when: Get page=3 with pagesize=10
    MvcResult requestResult = mockMvc
        .perform(MockMvcRequestBuilders
            .get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID + "/proyectosocioequipos", proyectoSocioId)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).header("X-Page", page).header("X-Page-Size", pageSize)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: Devuelve la pagina 3 con los ProyectoSocioEquipo del 31 al
        // 37
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.header().string("X-Page", "3"))
        .andExpect(MockMvcResultMatchers.header().string("X-Page-Total-Count", "7"))
        .andExpect(MockMvcResultMatchers.header().string("X-Page-Size", "10"))
        .andExpect(MockMvcResultMatchers.header().string("X-Total-Count", "37"))
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(7))).andReturn();

    List<ProyectoSocioEquipo> proyectoSocioResponse = mapper.readValue(requestResult.getResponse().getContentAsString(),
        new TypeReference<List<ProyectoSocioEquipo>>() {
        });

    for (int i = 31; i <= 37; i++) {
      ProyectoSocioEquipo proyectoPeriodoPagoRecuperado = proyectoSocioResponse.get(i - (page * pageSize) - 1);
      Assertions.assertThat(proyectoPeriodoPagoRecuperado.getPersonaRef()).isEqualTo("00" + i);
    }
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CENTGES-V" })
  public void findAllProyectoSocioEquipo_Returns204() throws Exception {
    // given: Una lista vacia de ProyectoSocioEquipo para la
    // ProyectoSocio
    Long proyectoSocioId = 1L;
    List<ProyectoSocioEquipo> proyectoSocioEquipo = new ArrayList<>();

    Integer page = 0;
    Integer pageSize = 10;

    BDDMockito
        .given(proyectoSocioEquipoService.findAllByProyectoSocio(ArgumentMatchers.<Long>any(),
            ArgumentMatchers.<String>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          Pageable pageable = invocation.getArgument(2, Pageable.class);
          Page<ProyectoSocioEquipo> pageResponse = new PageImpl<>(proyectoSocioEquipo, pageable, 0);
          return pageResponse;
        });

    // when: Get page=0 with pagesize=10
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID + "/proyectosocioequipos", proyectoSocioId)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).header("X-Page", page).header("X-Page-Size", pageSize)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: Devuelve un 204
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  /**
   * 
   * PROYECTO PERIODO JUSTIFICACION
   * 
   */

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CENTGES-V" })
  public void findAllProyectoSocioPeriodoJustificacion_ReturnsPage() throws Exception {
    // given: Una lista con 37 ProyectoSocioPeriodoJustificacion para el
    // ProyectoSocio
    Long proyectoSocioId = 1L;

    List<ProyectoSocioPeriodoJustificacion> proyectoSociosPeriodoJustificaciones = new ArrayList<>();
    for (long i = 1; i <= 37; i++) {
      proyectoSociosPeriodoJustificaciones.add(generarMockProyectoSocioPeriodoJustificacion(i));
    }

    Integer page = 3;
    Integer pageSize = 10;

    BDDMockito
        .given(proyectoSocioPeriodoJustificacionService.findAllByProyectoSocio(ArgumentMatchers.<Long>any(),
            ArgumentMatchers.<String>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          Pageable pageable = invocation.getArgument(2, Pageable.class);
          int size = pageable.getPageSize();
          int index = pageable.getPageNumber();
          int fromIndex = size * index;
          int toIndex = fromIndex + size;
          toIndex = toIndex > proyectoSociosPeriodoJustificaciones.size() ? proyectoSociosPeriodoJustificaciones.size()
              : toIndex;
          List<ProyectoSocioPeriodoJustificacion> content = proyectoSociosPeriodoJustificaciones.subList(fromIndex,
              toIndex);
          Page<ProyectoSocioPeriodoJustificacion> pageResponse = new PageImpl<>(content, pageable,
              proyectoSociosPeriodoJustificaciones.size());
          return pageResponse;
        });

    // when: Get page=3 with pagesize=10
    MvcResult requestResult = mockMvc
        .perform(MockMvcRequestBuilders
            .get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID + "/proyectosocioperiodojustificaciones", proyectoSocioId)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).header("X-Page", page).header("X-Page-Size", pageSize)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: Devuelve la pagina 3 con los ProyectoSocioEntidadConvocante del 31 al
        // 37
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.header().string("X-Page", "3"))
        .andExpect(MockMvcResultMatchers.header().string("X-Page-Total-Count", "7"))
        .andExpect(MockMvcResultMatchers.header().string("X-Page-Size", "10"))
        .andExpect(MockMvcResultMatchers.header().string("X-Total-Count", "37"))
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(7))).andReturn();

    List<ProyectoSocioPeriodoJustificacion> proyectoSociosPeriodoJustificacionesResponse = mapper.readValue(
        requestResult.getResponse().getContentAsString(), new TypeReference<List<ProyectoSocioPeriodoJustificacion>>() {
        });

    for (int i = 31; i <= 37; i++) {
      ProyectoSocioPeriodoJustificacion proyectoSocioPeriodoJustificacion = proyectoSociosPeriodoJustificacionesResponse
          .get(i - (page * pageSize) - 1);
      Assertions.assertThat(proyectoSocioPeriodoJustificacion.getObservaciones()).isEqualTo("observaciones-" + i);
    }
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CENTGES-V" })
  public void findAllProyectoSocioPeriodoJustificacion_EmptyList_Returns204() throws Exception {
    // given: Una lista vacia de ProyectoSocioPeriodoJustificacion para la
    // ProyectoSocio
    Long proyectoSocioId = 1L;
    List<ProyectoSocioPeriodoJustificacion> proyectoSociosPeriodoJustificaciones = new ArrayList<>();

    Integer page = 0;
    Integer pageSize = 10;

    BDDMockito
        .given(proyectoSocioPeriodoJustificacionService.findAllByProyectoSocio(ArgumentMatchers.<Long>any(),
            ArgumentMatchers.<String>any(), ArgumentMatchers.<Pageable>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          Pageable pageable = invocation.getArgument(2, Pageable.class);
          Page<ProyectoSocioPeriodoJustificacion> pageResponse = new PageImpl<>(proyectoSociosPeriodoJustificaciones,
              pageable, 0);
          return pageResponse;
        });

    // when: Get page=0 with pagesize=10
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID + "/proyectosocioperiodojustificaciones", proyectoSocioId)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).header("X-Page", page).header("X-Page-Size", pageSize)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: Devuelve un 204
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  /**
   * Función que genera un ProyectoSocio
   * 
   * @param proyectoSocioId Identificador del {@link ProyectoSocio}
   * @return el ProyectoSocio
   */
  private ProyectoSocio generarMockProyectoSocio(Long proyectoSocioId) {

    String suffix = String.format("%03d", proyectoSocioId);

    ProyectoSocio proyectoSocio = ProyectoSocio.builder()//
        .id(proyectoSocioId)//
        .proyecto(Proyecto.builder()//
            .id(1L)//
            .estado(//
                EstadoProyecto.builder()//
                    .id(1L)//
                    .estado(EstadoProyecto.Estado.BORRADOR)//
                    .build())
            .build())//
        .empresaRef("empresa-" + suffix)//
        .rolSocio(RolSocio.builder().id(1L).coordinador(true).build())//
        .fechaInicio(LocalDate.of(2021, 1, 11))//
        .fechaFin(LocalDate.of(2022, 1, 11))//
        .numInvestigadores(5)//
        .importeConcedido(BigDecimal.valueOf(1000))//
        .build();

    return proyectoSocio;
  }

  /**
   * Función que devuelve un objeto ProyectoSocioPeriodoPago
   * 
   * @param id id del ProyectoSocioPeriodoPago
   * 
   * @return el objeto ProyectoSocioPeriodoPago
   */
  private ProyectoSocioPeriodoPago generarMockProyectoSocioPeriodoPago(Long id) {
    ModeloEjecucion modeloEjecucion1 = new ModeloEjecucion(id, "nombre-1", "descripcion-1", true);

    Proyecto proyecto1 = Proyecto.builder()//
        .id(id).titulo("proyecto 1").acronimo("PR1").fechaInicio(LocalDate.of(2020, 11, 20))
        .fechaFin(LocalDate.of(2021, 11, 20)).unidadGestionRef("OPE").modeloEjecucion(modeloEjecucion1)
        .activo(Boolean.TRUE).build();

    RolSocio rolSocio = RolSocio.builder()//
        .id(id).abreviatura("001")//
        .nombre("nombre-001")//
        .descripcion("descripcion-001")//
        .coordinador(Boolean.FALSE)//
        .activo(Boolean.TRUE)//
        .build();

    ProyectoSocio proyectoSocio1 = ProyectoSocio.builder()//
        .id(id).proyecto(proyecto1).empresaRef("empresa-0041").rolSocio(rolSocio).build();

    ProyectoSocioPeriodoPago proyectoSocioPeriodoPago = new ProyectoSocioPeriodoPago(id, proyectoSocio1, 1,
        new BigDecimal(3500), LocalDate.of(2021, 4, 10), null);

    return proyectoSocioPeriodoPago;
  }

  /**
   * Función que genera un ProyectoSocioEquipo
   * 
   * @param id Identificador
   * @return el ProyectoSocioEquipo
   */
  private ProyectoSocioEquipo generarMockProyectoSocioEquipo(Long id) {

    ModeloEjecucion modeloEjecucion1 = new ModeloEjecucion(null, "nombre-1", "descripcion-1", true);

    Proyecto proyecto1 = Proyecto.builder()//
        .id(id).titulo("proyecto 1").acronimo("PR1").fechaInicio(LocalDate.of(2020, 11, 20))
        .fechaFin(LocalDate.of(2021, 11, 20)).unidadGestionRef("OPE").modeloEjecucion(modeloEjecucion1)
        .activo(Boolean.TRUE).build();

    RolSocio rolSocio = RolSocio.builder()//
        .id(id).abreviatura("001")//
        .nombre("nombre-001")//
        .descripcion("descripcion-001")//
        .coordinador(Boolean.FALSE)//
        .activo(Boolean.TRUE)//
        .build();

    RolProyecto rolProyecto = RolProyecto.builder()//
        .id(id).abreviatura("001")//
        .nombre("nombre-001")//
        .descripcion("descripcion-001")//
        .activo(Boolean.TRUE)//
        .build();

    ProyectoSocio proyectoSocio1 = ProyectoSocio.builder()//
        .id(id).proyecto(proyecto1).empresaRef("empresa-0041").rolSocio(rolSocio).build();

    ProyectoSocioEquipo proyectoSocioEquipo = new ProyectoSocioEquipo(id, proyectoSocio1, rolProyecto, "001",
        LocalDate.of(2021, 4, 10), null);

    return proyectoSocioEquipo;
  }

  /**
   * Función que devuelve un objeto ProyectoSocioPeriodoJustificacion
   * 
   * @param id id del ProyectoSocioPeriodoJustificacion
   * @return el objeto ProyectoSocioPeriodoJustificacion
   */
  private ProyectoSocioPeriodoJustificacion generarMockProyectoSocioPeriodoJustificacion(Long id) {
    ProyectoSocio convocatoria = new ProyectoSocio();
    convocatoria.setId(id == null ? 1 : id);

    ProyectoSocioPeriodoJustificacion convocatoriaPeriodoJustificacion = new ProyectoSocioPeriodoJustificacion();
    convocatoriaPeriodoJustificacion.setId(id);
    convocatoriaPeriodoJustificacion.setProyectoSocio(convocatoria);
    convocatoriaPeriodoJustificacion.setNumPeriodo(1);
    convocatoriaPeriodoJustificacion.setFechaInicio(LocalDate.of(2020, 10, 10));
    convocatoriaPeriodoJustificacion.setFechaFin(LocalDate.of(2020, 10, 10));
    convocatoriaPeriodoJustificacion.setFechaInicioPresentacion(LocalDateTime.of(2020, 10, 10, 0, 0, 0));
    convocatoriaPeriodoJustificacion.setFechaFinPresentacion(LocalDateTime.of(2020, 11, 20, 0, 0, 0));
    convocatoriaPeriodoJustificacion.setObservaciones("observaciones-" + id);

    return convocatoriaPeriodoJustificacion;
  }

}
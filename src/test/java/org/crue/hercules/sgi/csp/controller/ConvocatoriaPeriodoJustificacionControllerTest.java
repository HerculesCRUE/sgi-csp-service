package org.crue.hercules.sgi.csp.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.crue.hercules.sgi.csp.enums.TipoJustificacionEnum;
import org.crue.hercules.sgi.csp.exceptions.ConvocatoriaPeriodoJustificacionNotFoundException;
import org.crue.hercules.sgi.csp.model.Convocatoria;
import org.crue.hercules.sgi.csp.model.ConvocatoriaPeriodoJustificacion;
import org.crue.hercules.sgi.csp.service.ConvocatoriaPeriodoJustificacionService;
import org.crue.hercules.sgi.csp.service.ProgramaService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * ConvocatoriaPeriodoJustificacionControllerTest
 */
@WebMvcTest(ConvocatoriaPeriodoJustificacionController.class)
public class ConvocatoriaPeriodoJustificacionControllerTest extends BaseControllerTest {

  @MockBean
  private ConvocatoriaPeriodoJustificacionService service;

  @MockBean
  private ProgramaService programaService;

  private static final String PATH_PARAMETER_ID = "/{id}";
  private static final String CONTROLLER_BASE_PATH = "/convocatoriaperiodojustificaciones";

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-C" })
  public void updateConvocatoriaPeriodoJustificacionesConvocatoria_ReturnsConvocatoriaPeriodoJustificacionList()
      throws Exception {
    // given: una lista con uno de los ConvocatoriaPeriodoJustificacion actualizado,
    // otro nuevo y sin los otros 3 periodos existentes
    Long convocatoriaId = 1L;
    ConvocatoriaPeriodoJustificacion newConvocatoriaPeriodoJustificacion = generarMockConvocatoriaPeriodoJustificacion(
        null, 27, 30, TipoJustificacionEnum.FINAL, 1L);
    ConvocatoriaPeriodoJustificacion updatedConvocatoriaPeriodoJustificacion = generarMockConvocatoriaPeriodoJustificacion(
        4L, 24, 26, TipoJustificacionEnum.PERIODICA, 1L);

    List<ConvocatoriaPeriodoJustificacion> convocatoriaPeriodoJustificaciones = Arrays
        .asList(updatedConvocatoriaPeriodoJustificacion, newConvocatoriaPeriodoJustificacion);

    BDDMockito.given(service.updateConvocatoriaPeriodoJustificacionesConvocatoria(ArgumentMatchers.anyLong(),
        ArgumentMatchers.<ConvocatoriaPeriodoJustificacion>anyList())).will((InvocationOnMock invocation) -> {
          List<ConvocatoriaPeriodoJustificacion> periodoJustificaciones = invocation.getArgument(1);
          return periodoJustificaciones.stream().map(periodoJustificacion -> {
            if (periodoJustificacion.getId() == null) {
              periodoJustificacion.setId(5L);
            }
            periodoJustificacion.getConvocatoria().setId(convocatoriaId);
            return periodoJustificacion;
          }).collect(Collectors.toList());
        });

    // when: updateConvocatoriaPeriodoJustificacionesConvocatoria
    mockMvc
        .perform(MockMvcRequestBuilders.patch(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, convocatoriaId)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convocatoriaPeriodoJustificaciones)))
        .andDo(MockMvcResultHandlers.print())
        // then: Se crea el nuevo ConvocatoriaPeriodoJustificacion, se actualiza el
        // existe y se eliminan el resto
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(convocatoriaPeriodoJustificaciones.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].convocatoria.id").value(convocatoriaId))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].numPeriodo")
            .value(convocatoriaPeriodoJustificaciones.get(0).getNumPeriodo()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].mesInicial")
            .value(convocatoriaPeriodoJustificaciones.get(0).getMesInicial()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].mesFinal")
            .value(convocatoriaPeriodoJustificaciones.get(0).getMesFinal()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].fechaInicioPresentacion").value("2020-10-10"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].fechaFinPresentacion").value("2020-11-20"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].observaciones")
            .value(convocatoriaPeriodoJustificaciones.get(0).getObservaciones()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].tipoJustificacion")
            .value(convocatoriaPeriodoJustificaciones.get(0).getTipoJustificacion().getValue()))

        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(5))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].convocatoria.id").value(convocatoriaId))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].numPeriodo")
            .value(convocatoriaPeriodoJustificaciones.get(1).getNumPeriodo()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].mesInicial")
            .value(convocatoriaPeriodoJustificaciones.get(1).getMesInicial()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].mesFinal")
            .value(convocatoriaPeriodoJustificaciones.get(1).getMesFinal()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].fechaInicioPresentacion").value("2020-10-10"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].fechaFinPresentacion").value("2020-11-20"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].observaciones")
            .value(convocatoriaPeriodoJustificaciones.get(1).getObservaciones()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].tipoJustificacion")
            .value(convocatoriaPeriodoJustificaciones.get(1).getTipoJustificacion().getValue()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-E" })
  public void updateConvocatoriaPeriodoJustificacionesConvocatoria_WithNoExistingId_Returns404() throws Exception {
    // given: No existing Id
    Long id = 1L;
    ConvocatoriaPeriodoJustificacion convocatoriaPeriodoJustificacion = generarMockConvocatoriaPeriodoJustificacion(1L);

    BDDMockito.willThrow(new ConvocatoriaPeriodoJustificacionNotFoundException(id)).given(service)
        .updateConvocatoriaPeriodoJustificacionesConvocatoria(ArgumentMatchers.anyLong(),
            ArgumentMatchers.<ConvocatoriaPeriodoJustificacion>anyList());

    // when: updateConvocatoriaPeriodoJustificacionesConvocatoria
    mockMvc
        .perform(MockMvcRequestBuilders.patch(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, id)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(Arrays.asList(convocatoriaPeriodoJustificacion))))
        .andDo(MockMvcResultHandlers.print())
        // then: 404 error
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-V" })
  public void findById_WithExistingId_ReturnsConvocatoriaPeriodoJustificacion() throws Exception {
    // given: existing id
    BDDMockito.given(service.findById(ArgumentMatchers.anyLong())).willAnswer((InvocationOnMock invocation) -> {
      return generarMockConvocatoriaPeriodoJustificacion(invocation.getArgument(0));
    });

    // when: find by existing id
    mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, 1L)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: response is OK
        .andExpect(MockMvcResultMatchers.status().isOk())
        // and the requested ConvocatoriaPeriodoJustificacion is resturned as JSON
        // object
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
        .andExpect(MockMvcResultMatchers.jsonPath("convocatoria.id").value(1L))
        .andExpect(MockMvcResultMatchers.jsonPath("numPeriodo").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("mesInicial").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("mesFinal").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("fechaInicioPresentacion").value("2020-10-10"))
        .andExpect(MockMvcResultMatchers.jsonPath("fechaFinPresentacion").value("2020-11-20"))
        .andExpect(MockMvcResultMatchers.jsonPath("observaciones").value("observaciones-1"))
        .andExpect(MockMvcResultMatchers.jsonPath("tipoJustificacion").value("periodica"));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-ME-V" })
  public void findById_WithNoExistingId_Returns404() throws Exception {
    // given: no existing id
    BDDMockito.given(service.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new ConvocatoriaPeriodoJustificacionNotFoundException(1L);
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
   * Función que devuelve un objeto ConvocatoriaPeriodoJustificacion
   * 
   * @param id id del ConvocatoriaPeriodoJustificacion
   * @return el objeto ConvocatoriaPeriodoJustificacion
   */
  private ConvocatoriaPeriodoJustificacion generarMockConvocatoriaPeriodoJustificacion(Long id) {
    return generarMockConvocatoriaPeriodoJustificacion(id, 1, 2, TipoJustificacionEnum.PERIODICA, id);
  }

  /**
   * Función que devuelve un objeto ConvocatoriaPeriodoJustificacion
   * 
   * @param id             id del ConvocatoriaPeriodoJustificacion
   * @param mesInicial     Mes inicial
   * @param mesFinal       Mes final
   * @param tipo           Tipo justificacion
   * @param convocatoriaId Id Convocatoria
   * @return el objeto ConvocatoriaPeriodoJustificacion
   */
  private ConvocatoriaPeriodoJustificacion generarMockConvocatoriaPeriodoJustificacion(Long id, Integer mesInicial,
      Integer mesFinal, TipoJustificacionEnum tipo, Long convocatoriaId) {
    Convocatoria convocatoria = new Convocatoria();
    convocatoria.setId(convocatoriaId == null ? 1 : convocatoriaId);

    ConvocatoriaPeriodoJustificacion convocatoriaPeriodoJustificacion = new ConvocatoriaPeriodoJustificacion();
    convocatoriaPeriodoJustificacion.setId(id);
    convocatoriaPeriodoJustificacion.setConvocatoria(convocatoria);
    convocatoriaPeriodoJustificacion.setNumPeriodo(1);
    convocatoriaPeriodoJustificacion.setMesInicial(mesInicial);
    convocatoriaPeriodoJustificacion.setMesFinal(mesFinal);
    convocatoriaPeriodoJustificacion.setFechaInicioPresentacion(LocalDate.of(2020, 10, 10));
    convocatoriaPeriodoJustificacion.setFechaFinPresentacion(LocalDate.of(2020, 11, 20));
    convocatoriaPeriodoJustificacion.setObservaciones("observaciones-" + id);
    convocatoriaPeriodoJustificacion.setTipoJustificacion(tipo);

    return convocatoriaPeriodoJustificacion;
  }

}

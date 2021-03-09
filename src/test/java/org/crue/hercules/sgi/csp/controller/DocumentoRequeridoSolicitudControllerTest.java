package org.crue.hercules.sgi.csp.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.crue.hercules.sgi.csp.enums.FormularioSolicitud;
import org.crue.hercules.sgi.csp.exceptions.DocumentoRequeridoSolicitudNotFoundException;
import org.crue.hercules.sgi.csp.model.ConfiguracionSolicitud;
import org.crue.hercules.sgi.csp.model.Convocatoria;
import org.crue.hercules.sgi.csp.model.ConvocatoriaFase;
import org.crue.hercules.sgi.csp.model.DocumentoRequeridoSolicitud;
import org.crue.hercules.sgi.csp.model.TipoDocumento;
import org.crue.hercules.sgi.csp.model.TipoFase;
import org.crue.hercules.sgi.csp.service.DocumentoRequeridoSolicitudService;
import org.crue.hercules.sgi.csp.service.ProgramaService;
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
 * DocumentoRequeridoSolicitudControllerTest
 */
@WebMvcTest(DocumentoRequeridoSolicitudController.class)
public class DocumentoRequeridoSolicitudControllerTest extends BaseControllerTest {

  @MockBean
  private DocumentoRequeridoSolicitudService service;

  @MockBean
  private ProgramaService programaService;

  private static final String PATH_PARAMETER_ID = "/{id}";
  private static final String CONTROLLER_BASE_PATH = "/documentorequiridosolicitudes";

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CONV-C" })
  public void create_ReturnsModeloDocumentoRequeridoSolicitud() throws Exception {
    // given: new DocumentoRequeridoSolicitud
    DocumentoRequeridoSolicitud documentoRequeridoSolicitud = generarMockDocumentoRequeridoSolicitud(1L);
    documentoRequeridoSolicitud.setId(null);

    BDDMockito.given(service.create(ArgumentMatchers.<DocumentoRequeridoSolicitud>any()))
        .willAnswer((InvocationOnMock invocation) -> {
          DocumentoRequeridoSolicitud newDocumentoRequeridoSolicitud = new DocumentoRequeridoSolicitud();
          BeanUtils.copyProperties(invocation.getArgument(0), newDocumentoRequeridoSolicitud);
          newDocumentoRequeridoSolicitud.setId(1L);
          return newDocumentoRequeridoSolicitud;
        });

    // when: create DocumentoRequeridoSolicitud
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(documentoRequeridoSolicitud)))
        .andDo(MockMvcResultHandlers.print())
        // then: new DocumentoRequeridoSolicitud is created
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("configuracionSolicitud.id")
            .value(documentoRequeridoSolicitud.getConfiguracionSolicitud().getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("tipoDocumento.id")
            .value(documentoRequeridoSolicitud.getTipoDocumento().getId()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("observaciones").value(documentoRequeridoSolicitud.getObservaciones()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CONV-C" })
  public void create_WithId_Returns400() throws Exception {
    // given: a DocumentoRequeridoSolicitud with id filled
    DocumentoRequeridoSolicitud documentoRequeridoSolicitud = generarMockDocumentoRequeridoSolicitud(1L);

    BDDMockito.given(service.create(ArgumentMatchers.<DocumentoRequeridoSolicitud>any()))
        .willThrow(new IllegalArgumentException());

    // when: create DocumentoRequeridoSolicitud
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(documentoRequeridoSolicitud)))
        .andDo(MockMvcResultHandlers.print())
        // then: 400 error
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CONV-E" })
  public void update_ReturnsDocumentoRequeridoSolicitud() throws Exception {
    // given: Existing DocumentoRequeridoSolicitud to be updated
    DocumentoRequeridoSolicitud documentoRequeridoSolicitudExistente = generarMockDocumentoRequeridoSolicitud(1L);
    DocumentoRequeridoSolicitud documentoRequeridoSolicitud = generarMockDocumentoRequeridoSolicitud(1L);
    documentoRequeridoSolicitud.setObservaciones("observaciones-nuevas");

    BDDMockito.given(service.findById(ArgumentMatchers.<Long>any())).willReturn(documentoRequeridoSolicitudExistente);
    BDDMockito.given(service.update(ArgumentMatchers.<DocumentoRequeridoSolicitud>any()))
        .willAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    // when: update DocumentoRequeridoSolicitud
    mockMvc
        .perform(MockMvcRequestBuilders
            .put(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, documentoRequeridoSolicitudExistente.getId())
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(documentoRequeridoSolicitud)))
        .andDo(MockMvcResultHandlers.print())
        // then: DocumentoRequeridoSolicitud is updated
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(documentoRequeridoSolicitudExistente.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("configuracionSolicitud.id")
            .value(documentoRequeridoSolicitudExistente.getConfiguracionSolicitud().getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("tipoDocumento.id")
            .value(documentoRequeridoSolicitudExistente.getTipoDocumento().getId()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("observaciones").value(documentoRequeridoSolicitud.getObservaciones()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CONV-E" })
  public void update_WithNoExistingId_Returns404() throws Exception {
    // given: No existing Id
    Long id = 1L;
    DocumentoRequeridoSolicitud documentoRequeridoSolicitud = generarMockDocumentoRequeridoSolicitud(id);

    BDDMockito.willThrow(new DocumentoRequeridoSolicitudNotFoundException(id)).given(service)
        .update(ArgumentMatchers.<DocumentoRequeridoSolicitud>any());

    // when: update DocumentoRequeridoSolicitud
    mockMvc
        .perform(MockMvcRequestBuilders.put(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, id)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(documentoRequeridoSolicitud)))
        .andDo(MockMvcResultHandlers.print())
        // then: 404 error
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CONV-B" })
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
  @WithMockUser(username = "user", authorities = { "CSP-CONV-B" })
  public void delete_NoExistingId_Return404() throws Exception {
    // given: non existing id
    Long id = 1L;

    BDDMockito.willThrow(new DocumentoRequeridoSolicitudNotFoundException(id)).given(service)
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

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CONV-V" })
  public void findById_WithExistingId_ReturnsDocumentoRequeridoSolicitud() throws Exception {
    // given: existing id
    Long id = 1L;
    DocumentoRequeridoSolicitud documentoRequeridoSolicitud = generarMockDocumentoRequeridoSolicitud(id);

    BDDMockito.given(service.findById(ArgumentMatchers.anyLong())).willReturn((documentoRequeridoSolicitud));

    // when: find by existing id
    mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, 1L)
            .with(SecurityMockMvcRequestPostProcessors.csrf()).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: response is OK
        .andExpect(MockMvcResultMatchers.status().isOk())
        // and the requested DocumentoRequeridoSolicitud is resturned as JSON
        // object
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(documentoRequeridoSolicitud.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("configuracionSolicitud.id")
            .value(documentoRequeridoSolicitud.getConfiguracionSolicitud().getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("tipoDocumento.id")
            .value(documentoRequeridoSolicitud.getTipoDocumento().getId()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("observaciones").value(documentoRequeridoSolicitud.getObservaciones()));
  }

  @Test
  @WithMockUser(username = "user", authorities = { "CSP-CONV-V" })
  public void findById_WithNoExistingId_Returns404() throws Exception {
    // given: no existing id
    BDDMockito.given(service.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new DocumentoRequeridoSolicitudNotFoundException(1L);
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
   * Genera un objeto ConfiguracionSolicitud
   * 
   * @param configuracionSolicitudId
   * @param convocatoriaId
   * @param convocatoriaFaseId
   * @return
   */
  private ConfiguracionSolicitud generarMockConfiguracionSolicitud(Long configuracionSolicitudId, Long convocatoriaId,
      Long convocatoriaFaseId) {

    Convocatoria convocatoria = Convocatoria.builder()//
        .id(convocatoriaId)//
        .estado(Convocatoria.Estado.BORRADOR)//
        .activo(Boolean.TRUE)//
        .build();

    TipoFase tipoFase = TipoFase.builder()//
        .id(convocatoriaFaseId)//
        .nombre("nombre-1")//
        .activo(Boolean.TRUE)//
        .build();

    ConvocatoriaFase convocatoriaFase = ConvocatoriaFase.builder()//
        .id(convocatoriaFaseId)//
        .convocatoria(convocatoria)//
        .tipoFase(tipoFase)//
        .fechaInicio(LocalDateTime.of(2020, 10, 1, 17, 18, 19))//
        .fechaFin(LocalDateTime.of(2020, 10, 15, 17, 18, 19))//
        .observaciones("observaciones")//
        .build();

    ConfiguracionSolicitud configuracionSolicitud = ConfiguracionSolicitud.builder()//
        .id(configuracionSolicitudId)//
        .convocatoria(convocatoria)//
        .tramitacionSGI(Boolean.TRUE)//
        .fasePresentacionSolicitudes(convocatoriaFase)//
        .importeMaximoSolicitud(BigDecimal.valueOf(12345))//
        .formularioSolicitud(FormularioSolicitud.ESTANDAR)//
        .build();

    return configuracionSolicitud;

  }

  /**
   * Función que devuelve un objeto TipoDocumento
   * 
   * @param id id del TipoDocumento
   * @return el objeto TipoDocumento
   */
  private TipoDocumento generarMockTipoDocumento(Long id) {

    return TipoDocumento.builder()//
        .id(id)//
        .nombre("nombreTipoDocumento-" + id)//
        .descripcion("descripcionTipoDocumento-" + id)//
        .activo(Boolean.TRUE)//
        .build();
  }

  /**
   * Función que devuelve un objeto DocumentoRequeridoSolicitud
   * 
   * @param id id del DocumentoRequeridoSolicitud
   * @return el objeto DocumentoRequeridoSolicitud
   */
  private DocumentoRequeridoSolicitud generarMockDocumentoRequeridoSolicitud(Long id) {

    return DocumentoRequeridoSolicitud.builder()//
        .id(id)//
        .configuracionSolicitud(generarMockConfiguracionSolicitud(id, id, id))//
        .tipoDocumento(generarMockTipoDocumento(id))//
        .observaciones("observacionesDocumentoRequeridoSolicitud-" + id)//
        .build();
  }

}
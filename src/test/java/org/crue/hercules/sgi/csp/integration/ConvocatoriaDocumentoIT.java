package org.crue.hercules.sgi.csp.integration;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.csp.model.Convocatoria;
import org.crue.hercules.sgi.csp.model.ConvocatoriaDocumento;
import org.crue.hercules.sgi.csp.model.ModeloEjecucion;
import org.crue.hercules.sgi.csp.model.TipoDocumento;
import org.crue.hercules.sgi.csp.model.TipoFase;
import org.crue.hercules.sgi.framework.test.security.Oauth2WireMockInitializer;
import org.crue.hercules.sgi.framework.test.security.Oauth2WireMockInitializer.TokenBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

/**
 * Test de integracion de ConvocatoriaDocumento.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = { Oauth2WireMockInitializer.class })
public class ConvocatoriaDocumentoIT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private TokenBuilder tokenBuilder;

  private static final String PATH_PARAMETER_ID = "/{id}";
  private static final String CONTROLLER_BASE_PATH = "/convocatoriadocumentos";

  private HttpEntity<ConvocatoriaDocumento> buildRequest(HttpHeaders headers, ConvocatoriaDocumento entity)
      throws Exception {
    headers = (headers != null ? headers : new HttpHeaders());
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set("Authorization", String.format("bearer %s",
        tokenBuilder.buildToken("user", "CSP-CONV-C", "CSP-CONV-E", "CSP-CONV-B", "CSP-CONV-V")));

    HttpEntity<ConvocatoriaDocumento> request = new HttpEntity<>(entity, headers);
    return request;
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void create_ReturnsConvocatoriaDocumento() throws Exception {

    // given: new ConvocatoriaDocumento
    ConvocatoriaDocumento convocatoriaDocumento = generarMockConvocatoriaDocumento(null, 1L, 1L);

    // when: create ConvocatoriaDocumento
    final ResponseEntity<ConvocatoriaDocumento> response = restTemplate.exchange(CONTROLLER_BASE_PATH, HttpMethod.POST,
        buildRequest(null, convocatoriaDocumento), ConvocatoriaDocumento.class);

    // then: new ConvocatoriaDocumento is created
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    ConvocatoriaDocumento responseData = response.getBody();
    Assertions.assertThat(responseData.getId()).as("getId()").isNotNull();
    Assertions.assertThat(responseData.getConvocatoria().getId()).as("getConvocatoria().getId()")
        .isEqualTo(convocatoriaDocumento.getConvocatoria().getId());
    Assertions.assertThat(responseData.getTipoFase().getId()).as("getTipoFase().getId()")
        .isEqualTo(convocatoriaDocumento.getTipoFase().getId());
    Assertions.assertThat(responseData.getTipoDocumento().getId()).as("getTipoDocumento().getId()")
        .isEqualTo(convocatoriaDocumento.getTipoDocumento().getId());
    Assertions.assertThat(responseData.getNombre()).as("getNombre()").isEqualTo(convocatoriaDocumento.getNombre());
    Assertions.assertThat(responseData.getPublico()).as("getPublico()").isEqualTo(convocatoriaDocumento.getPublico());
    Assertions.assertThat(responseData.getObservaciones()).as("getObservaciones()")
        .isEqualTo(convocatoriaDocumento.getObservaciones());
    Assertions.assertThat(responseData.getDocumentoRef()).as("getDocumentoRef()")
        .isEqualTo(convocatoriaDocumento.getDocumentoRef());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void update_ReturnsConvocatoriaDocumento() throws Exception {

    // given: existing ConvocatoriaDocumento to be updated
    ConvocatoriaDocumento convocatoriaDocumento = generarMockConvocatoriaDocumento(1L, 1L, 1L);
    convocatoriaDocumento.getTipoDocumento().setId(2L);
    convocatoriaDocumento.setNombre("nombre-modificado");
    convocatoriaDocumento.setObservaciones("observaciones-modificadas");

    // when: update ConvocatoriaDocumento
    final ResponseEntity<ConvocatoriaDocumento> response = restTemplate.exchange(
        CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, HttpMethod.PUT, buildRequest(null, convocatoriaDocumento),
        ConvocatoriaDocumento.class, convocatoriaDocumento.getId());

    // then: ConvocatoriaDocumento is updated
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    ConvocatoriaDocumento responseData = response.getBody();
    Assertions.assertThat(responseData).isNotNull();
    Assertions.assertThat(responseData.getId()).isNotNull();
    Assertions.assertThat(responseData.getId()).as("getId()").isEqualTo(1L);
    Assertions.assertThat(responseData.getConvocatoria().getId()).as("getConvocatoria().getId()").isEqualTo(1L);
    Assertions.assertThat(responseData.getTipoFase().getId()).as("getTipoFase().getId()").isEqualTo(1L);
    Assertions.assertThat(responseData.getTipoDocumento().getId()).as("getTipoDocumento().getId()").isEqualTo(2L);
    Assertions.assertThat(responseData.getNombre()).as("getNombre()").isEqualTo("nombre-modificado");
    Assertions.assertThat(responseData.getPublico()).as("getPublico()").isEqualTo(Boolean.TRUE);
    Assertions.assertThat(responseData.getObservaciones()).as("getObservaciones()")
        .isEqualTo("observaciones-modificadas");
    Assertions.assertThat(responseData.getDocumentoRef()).as("getDocumentoRef()").isEqualTo("documentoRef-1");
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void delete_Return204() throws Exception {
    // given: existing ConvocatoriaDocumento to be deleted
    Long convocatoriaDocumentoId = 1L;

    // when: delete ConvocatoriaDocumento
    final ResponseEntity<ConvocatoriaDocumento> response = restTemplate.exchange(
        CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, HttpMethod.DELETE, buildRequest(null, null),
        ConvocatoriaDocumento.class, convocatoriaDocumentoId);

    // then: ConvocatoriaDocumento deleted
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void findById_ReturnsConvocatoriaDocumento() throws Exception {
    Long convocatoriaDocumentoId = 2L;

    final ResponseEntity<ConvocatoriaDocumento> response = restTemplate.exchange(
        CONTROLLER_BASE_PATH + PATH_PARAMETER_ID, HttpMethod.GET, buildRequest(null, null), ConvocatoriaDocumento.class,
        convocatoriaDocumentoId);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    ConvocatoriaDocumento responseData = response.getBody();
    Assertions.assertThat(responseData).isNotNull();
    Assertions.assertThat(responseData.getId()).isNotNull();
    Assertions.assertThat(responseData.getId()).as("getId()").isEqualTo(convocatoriaDocumentoId);
    Assertions.assertThat(responseData.getConvocatoria().getId()).as("getConvocatoria().getId()").isEqualTo(1L);
    Assertions.assertThat(responseData.getTipoFase().getId()).as("getTipoFase().getId()").isEqualTo(2L);
    Assertions.assertThat(responseData.getTipoDocumento().getId()).as("getTipoDocumento().getId()").isEqualTo(2L);
    Assertions.assertThat(responseData.getNombre()).as("getNombre()").isEqualTo("nombre doc-2");
    Assertions.assertThat(responseData.getPublico()).as("getPublico()").isEqualTo(Boolean.TRUE);
    Assertions.assertThat(responseData.getObservaciones()).as("getObservaciones()")
        .isEqualTo("observacionesConvocatoriaDocumento-2");
    Assertions.assertThat(responseData.getDocumentoRef()).as("getDocumentoRef()").isEqualTo("documentoRef-2");
  }

  /**
   * Función que devuelve un objeto ConvocatoriaDocumento
   * 
   * @param id
   * @param tipoFaseId
   * @param tipoDocumentoId
   * @return el objeto ConvocatoriaDocumento
   */
  private ConvocatoriaDocumento generarMockConvocatoriaDocumento(Long id, Long tipoFaseId, Long tipoDocumentoId) {

    ModeloEjecucion modeloEjecucion = ModeloEjecucion.builder().id(1L).build();
    Convocatoria convocatoria = Convocatoria.builder().id(1L).modeloEjecucion(modeloEjecucion).build();
    TipoFase tipoFase = TipoFase.builder().id(1L).build();
    TipoDocumento tipoDocumento = TipoDocumento.builder().id(tipoDocumentoId).build();

    return ConvocatoriaDocumento.builder()//
        .id(id)//
        .convocatoria(convocatoria)//
        .tipoFase(tipoFase)//
        .tipoDocumento(tipoDocumento)//
        .nombre("nombre doc-" + id)//
        .publico(Boolean.TRUE)//
        .observaciones("observacionesConvocatoriaDocumento-" + id)//
        .documentoRef("documentoRef-" + id)//
        .build();
  }
}

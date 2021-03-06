package org.crue.hercules.sgi.csp.integration;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.csp.model.RolProyecto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Test de integracion de RolProyecto.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RolProyectoIT extends BaseIT {

  private static final String PATH_PARAMETER_ID = "/{id}";
  private static final String PATH_PARAMETER_DESACTIVAR = "/desactivar";
  private static final String PATH_PARAMETER_REACTIVAR = "/reactivar";
  private static final String PATH_PARAMETER_TODOS = "/todos";
  private static final String CONTROLLER_BASE_PATH = "/rolproyectos";

  private HttpEntity<RolProyecto> buildRequest(HttpHeaders headers, RolProyecto entity) throws Exception {
    headers = (headers != null ? headers : new HttpHeaders());
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set("Authorization", String.format("bearer %s", tokenBuilder.buildToken("user", "SYSADMIN", "CSP-RPRO-B",
        "CSP-RPRO-C", "CSP-RPRO-E", "CSP-RPRO-V", "CSP-RPRO-X")));

    HttpEntity<RolProyecto> request = new HttpEntity<>(entity, headers);
    return request;
  }

  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void create_ReturnsRolProyecto() throws Exception {

    // given: new RolProyecto
    RolProyecto rolProyecto = generarMockRolProyecto(1L);
    rolProyecto.setId(null);

    // when: create RolProyecto
    final ResponseEntity<RolProyecto> response = restTemplate.exchange(CONTROLLER_BASE_PATH, HttpMethod.POST,
        buildRequest(null, rolProyecto), RolProyecto.class);

    // then: new RolProyecto is created
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    RolProyecto responseData = response.getBody();
    Assertions.assertThat(responseData.getId()).as("getId()").isNotNull();
    Assertions.assertThat(responseData.getAbreviatura()).as("getAbreviatura()").isEqualTo(rolProyecto.getAbreviatura());
    Assertions.assertThat(responseData.getNombre()).as("getNombre()").isEqualTo(rolProyecto.getNombre());
    Assertions.assertThat(responseData.getDescripcion()).as("getDescripcion()").isEqualTo(rolProyecto.getDescripcion());
    Assertions.assertThat(responseData.getRolPrincipal()).as("getRolPrincipal()")
        .isEqualTo(rolProyecto.getRolPrincipal());
    Assertions.assertThat(responseData.getResponsableEconomico()).as("getResponsableEconomico()")
        .isEqualTo(rolProyecto.getResponsableEconomico());
    Assertions.assertThat(responseData.getEquipo()).as("getEquipo()").isEqualTo(rolProyecto.getEquipo());
    Assertions.assertThat(responseData.getActivo()).as("getActivo()").isEqualTo(Boolean.TRUE);

  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void update_ReturnsRolProyecto() throws Exception {

    // given: existing RolProyecto to be updated
    RolProyecto rolProyecto = generarMockRolProyecto(1L);
    rolProyecto.setDescripcion("descripcion-modificada");

    // when: update RolProyecto
    final ResponseEntity<RolProyecto> response = restTemplate.exchange(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID,
        HttpMethod.PUT, buildRequest(null, rolProyecto), RolProyecto.class, rolProyecto.getId());

    // then: RolProyecto is updated
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    RolProyecto responseData = response.getBody();
    Assertions.assertThat(responseData.getId()).as("getId()").isEqualTo(rolProyecto.getId());
    Assertions.assertThat(responseData.getAbreviatura()).as("getAbreviatura()").isEqualTo(rolProyecto.getAbreviatura());
    Assertions.assertThat(responseData.getNombre()).as("getNombre()").isEqualTo(rolProyecto.getNombre());
    Assertions.assertThat(responseData.getDescripcion()).as("getDescripcion()").isEqualTo(rolProyecto.getDescripcion());
    Assertions.assertThat(responseData.getRolPrincipal()).as("getRolPrincipal()")
        .isEqualTo(rolProyecto.getRolPrincipal());
    Assertions.assertThat(responseData.getResponsableEconomico()).as("getResponsableEconomico()")
        .isEqualTo(rolProyecto.getResponsableEconomico());
    Assertions.assertThat(responseData.getEquipo()).as("getEquipo()").isEqualTo(rolProyecto.getEquipo());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void enable_ReturnsRolProyecto() throws Exception {
    // given: existing RolProyecto to be enabled
    Long id = 1L;

    // when: disable RolProyecto
    final ResponseEntity<RolProyecto> response = restTemplate.exchange(
        CONTROLLER_BASE_PATH + PATH_PARAMETER_ID + PATH_PARAMETER_REACTIVAR, HttpMethod.PATCH, buildRequest(null, null),
        RolProyecto.class, id);

    // then: RolProyecto is disabled
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    RolProyecto rolProyecto = response.getBody();
    Assertions.assertThat(rolProyecto.getId()).as("getId()").isNotNull();
    Assertions.assertThat(rolProyecto.getId()).as("getId()").isEqualTo(id);
    Assertions.assertThat(rolProyecto.getActivo()).as("getActivo()").isEqualTo(Boolean.TRUE);

  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void disable_ReturnsRolProyecto() throws Exception {
    // given: existing RolProyecto to be disabled
    Long id = 1L;

    // when: disable RolProyecto
    final ResponseEntity<RolProyecto> response = restTemplate.exchange(
        CONTROLLER_BASE_PATH + PATH_PARAMETER_ID + PATH_PARAMETER_DESACTIVAR, HttpMethod.PATCH,
        buildRequest(null, null), RolProyecto.class, id);

    // then: RolProyecto is disabled
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    RolProyecto rolProyecto = response.getBody();
    Assertions.assertThat(rolProyecto.getId()).as("getId()").isNotNull();
    Assertions.assertThat(rolProyecto.getId()).as("getId()").isEqualTo(id);
    Assertions.assertThat(rolProyecto.getActivo()).as("getActivo()").isEqualTo(Boolean.FALSE);

  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void existsById_Returns200() throws Exception {
    // given: existing id
    Long id = 1L;
    // when: exists by id
    final ResponseEntity<RolProyecto> response = restTemplate.exchange(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID,
        HttpMethod.HEAD, buildRequest(null, null), RolProyecto.class, id);
    // then: 200 OK
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void existsById_Returns204() throws Exception {
    // given: no existing id
    Long id = 1L;
    // when: exists by id
    final ResponseEntity<RolProyecto> response = restTemplate.exchange(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID,
        HttpMethod.HEAD, buildRequest(null, null), RolProyecto.class, id);
    // then: 204 No Content
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void findById_ReturnsRolProyecto() throws Exception {
    Long id = 1L;

    final ResponseEntity<RolProyecto> response = restTemplate.exchange(CONTROLLER_BASE_PATH + PATH_PARAMETER_ID,
        HttpMethod.GET, buildRequest(null, null), RolProyecto.class, id);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    RolProyecto responseData = response.getBody();
    Assertions.assertThat(responseData.getId()).as("getId()").isEqualTo(id);
    Assertions.assertThat(responseData.getAbreviatura()).as("getAbreviatura()").isEqualTo("001");
    Assertions.assertThat(responseData.getNombre()).as("getNombre()").isEqualTo("nombre-001");
    Assertions.assertThat(responseData.getDescripcion()).as("getDescripcion()").isEqualTo("descripcion-001");
    Assertions.assertThat(responseData.getRolPrincipal()).as("getRolPrincipal()").isEqualTo(Boolean.FALSE);
    Assertions.assertThat(responseData.getResponsableEconomico()).as("getResponsableEconomico()")
        .isEqualTo(Boolean.FALSE);
    Assertions.assertThat(responseData.getEquipo()).as("getEquipo()").isEqualTo(RolProyecto.Equipo.INVESTIGACION);
    Assertions.assertThat(responseData.getActivo()).as("getActivo()").isEqualTo(Boolean.TRUE);
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void findAll_WithPagingSortingAndFiltering_ReturnsRolProyectoSubList() throws Exception {

    // given: data for RolProyecto

    // first page, 3 elements per page sorted by nombre desc
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", String.format("bearer %s", tokenBuilder.buildToken("user", "CSP-RPRO-V")));
    headers.add("X-Page", "0");
    headers.add("X-Page-Size", "3");
    String sort = "id,desc";
    String filter = "descripcion=ke=00";

    // when: find RolProyecto
    URI uri = UriComponentsBuilder.fromUriString(CONTROLLER_BASE_PATH).queryParam("s", sort).queryParam("q", filter)
        .build(false).toUri();
    final ResponseEntity<List<RolProyecto>> response = restTemplate.exchange(uri, HttpMethod.GET,
        buildRequest(headers, null), new ParameterizedTypeReference<List<RolProyecto>>() {
        });

    // given: RolProyecto data filtered and sorted
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    final List<RolProyecto> responseData = response.getBody();
    Assertions.assertThat(responseData.size()).isEqualTo(3);
    HttpHeaders responseHeaders = response.getHeaders();
    Assertions.assertThat(responseHeaders.getFirst("X-Page")).as("X-Page").isEqualTo("0");
    Assertions.assertThat(responseHeaders.getFirst("X-Page-Size")).as("X-Page-Size").isEqualTo("3");
    Assertions.assertThat(responseHeaders.getFirst("X-Total-Count")).as("X-Total-Count").isEqualTo("3");

    Assertions.assertThat(responseData.get(0).getDescripcion()).as("get(0).getDescripcion())")
        .isEqualTo("descripcion-" + String.format("%03d", 3));
    Assertions.assertThat(responseData.get(1).getDescripcion()).as("get(1).getDescripcion())")
        .isEqualTo("descripcion-" + String.format("%03d", 2));
    Assertions.assertThat(responseData.get(2).getDescripcion()).as("get(2).getDescripcion())")
        .isEqualTo("descripcion-" + String.format("%03d", 1));
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void findAllTodos_WithPagingSortingAndFiltering_ReturnsRolProyectoSubList() throws Exception {

    // given: data for RolProyecto

    // first page, 3 elements per page sorted by nombre desc
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", String.format("bearer %s", tokenBuilder.buildToken("user", "CSP-RPRO-V_OPE")));
    headers.add("X-Page", "0");
    headers.add("X-Page-Size", "3");
    String sort = "id,desc";
    String filter = "descripcion=ke=00";

    // when: find RolProyecto
    URI uri = UriComponentsBuilder.fromUriString(CONTROLLER_BASE_PATH + PATH_PARAMETER_TODOS).queryParam("s", sort)
        .queryParam("q", filter).build(false).toUri();
    final ResponseEntity<List<RolProyecto>> response = restTemplate.exchange(uri, HttpMethod.GET,
        buildRequest(headers, null), new ParameterizedTypeReference<List<RolProyecto>>() {
        });

    // given: RolProyecto data filtered and sorted
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    final List<RolProyecto> responseData = response.getBody();
    Assertions.assertThat(responseData.size()).isEqualTo(3);
    HttpHeaders responseHeaders = response.getHeaders();
    Assertions.assertThat(responseHeaders.getFirst("X-Page")).as("X-Page").isEqualTo("0");
    Assertions.assertThat(responseHeaders.getFirst("X-Page-Size")).as("X-Page-Size").isEqualTo("3");
    Assertions.assertThat(responseHeaders.getFirst("X-Total-Count")).as("X-Total-Count").isEqualTo("3");

    Assertions.assertThat(responseData.get(0).getDescripcion()).as("get(0).getDescripcion())")
        .isEqualTo("descripcion-" + String.format("%03d", 8));
    Assertions.assertThat(responseData.get(1).getDescripcion()).as("get(1).getDescripcion())")
        .isEqualTo("descripcion-" + String.format("%03d", 2));
    Assertions.assertThat(responseData.get(2).getDescripcion()).as("get(2).getDescripcion())")
        .isEqualTo("descripcion-" + String.format("%03d", 1));
  }

  /**
   * Función que genera RolProyecto
   * 
   * @param rolProyectoId
   * @return el rolProyecto
   */
  private RolProyecto generarMockRolProyecto(Long rolProyectoId) {

    String suffix = String.format("%03d", rolProyectoId);

    // @formatter:off
    RolProyecto rolProyecto = RolProyecto.builder()
        .id(rolProyectoId)
        .abreviatura(suffix)
        .nombre("nombre-" + suffix)
        .descripcion("descripcion-" + suffix)
        .rolPrincipal(Boolean.FALSE)
        .responsableEconomico(Boolean.FALSE)
        .equipo(RolProyecto.Equipo.INVESTIGACION)
        .activo(Boolean.TRUE)
        .build();
    // @formatter:on

    return rolProyecto;
  }

}

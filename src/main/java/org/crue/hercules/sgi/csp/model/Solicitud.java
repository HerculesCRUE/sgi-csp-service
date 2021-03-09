package org.crue.hercules.sgi.csp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.crue.hercules.sgi.csp.enums.FormularioSolicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitud")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud extends BaseEntity {

  /**
   * Serial version
   */
  private static final long serialVersionUID = 1L;

  /** Id */
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitud_seq")
  @SequenceGenerator(name = "solicitud_seq", sequenceName = "solicitud_seq", allocationSize = 1)
  private Long id;

  /** Codigo externo */
  @Column(name = "codigo_externo", length = 50, nullable = true)
  @Size(max = 50)
  private String codigoExterno;

  /** Codigo externo */
  @Column(name = "codigo_registro_interno", length = 50, nullable = true)
  @Size(max = 50)
  private String codigoRegistroInterno;

  /** Estado solicitud */
  @ManyToOne
  @JoinColumn(name = "estado_solicitud_id", nullable = true, foreignKey = @ForeignKey(name = "FK_SOLICITUD_ESTADO_SOLICITUD"))
  private EstadoSolicitud estado;

  /** Convocatoria */
  @ManyToOne
  @JoinColumn(name = "convocatoria_id", nullable = true, foreignKey = @ForeignKey(name = "FK_SOLICITUD_CONVOCATORIA"))
  private Convocatoria convocatoria;

  /** CreadorRef */
  @Column(name = "creador_ref", length = 50, nullable = false)
  @Size(max = 50)
  private String creadorRef;

  /** SolicitanteRef */
  @Column(name = "solicitante_ref", length = 50, nullable = false)
  @Size(max = 50)
  @NotNull
  private String solicitanteRef;

  /** Observaciones */
  @Column(name = "observaciones", length = 2000, nullable = true)
  @Size(max = 2000)
  private String observaciones;

  /** Convocatoria externa */
  @Column(name = "convocatoria_externa", length = 50, nullable = true)
  @Size(max = 50)
  private String convocatoriaExterna;

  /** Unidad gestion ref */
  @Column(name = "unidad_gestion_ref", length = 50, nullable = false)
  @Size(max = 50)
  @NotNull
  private String unidadGestionRef;

  /** Tipo formulario solicitud */
  @Column(name = "formulario_solicitud", length = 50, nullable = false)
  @Enumerated(EnumType.STRING)
  @NotNull
  private FormularioSolicitud formularioSolicitud;

  /** Activo */
  @Column(name = "activo", columnDefinition = "boolean default true", nullable = false)
  private Boolean activo;

}
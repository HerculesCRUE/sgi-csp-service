package org.crue.hercules.sgi.csp.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitud_hito")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudHito extends BaseEntity {

  /**
   * Serial version
   */
  private static final long serialVersionUID = 1L;

  /** Id */
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitud_hito_seq")
  @SequenceGenerator(name = "solicitud_hito_seq", sequenceName = "solicitud_hito_seq", allocationSize = 1)
  private Long id;

  /** Solicitud */
  @ManyToOne
  @JoinColumn(name = "solicitud_id", nullable = false, foreignKey = @ForeignKey(name = "FK_SOLICITUD_HITO_SOLICITUD"))
  @NotNull
  private Solicitud solicitud;

  /** Tipo hito */
  @ManyToOne
  @JoinColumn(name = "tipo_hito_id", nullable = false, foreignKey = @ForeignKey(name = "FK_SOLICITUD_HITO_TIPO_HITO"))
  @NotNull
  private TipoHito tipoHito;

  /** Fecha */
  @Column(name = "fecha", nullable = false)
  @NotNull
  private LocalDate fecha;

  /** Comentario */
  @Column(name = "comentario", length = 2000, nullable = true)
  @Size(max = 2000)
  private String comentario;

  /** Genera aviso */
  @Column(name = "genera_aviso", columnDefinition = "boolean default false", nullable = false)
  @NotNull
  private Boolean generaAviso;

}
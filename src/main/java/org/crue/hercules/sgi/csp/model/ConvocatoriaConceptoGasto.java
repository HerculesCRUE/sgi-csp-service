package org.crue.hercules.sgi.csp.model;

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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * ConvocatoriaConceptoGasto
 */

@Entity
@Table(name = "convocatoria_concepto_gasto")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ConvocatoriaConceptoGasto extends BaseEntity {

  /**
   * Serial version
   */
  private static final long serialVersionUID = 1L;

  /** Id */
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "convocatoria_concepto_gasto_seq")
  @SequenceGenerator(name = "convocatoria_concepto_gasto_seq", sequenceName = "convocatoria_concepto_gasto_seq", allocationSize = 1)
  private Long id;

  /** Convocatoria */
  @ManyToOne
  @JoinColumn(name = "convocatoria_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CONVOCATORIACONCEPTOGASTO_CONVOCATORIA"))
  @NotNull
  private Convocatoria convocatoria;

  /** ConceptoGasto */
  @ManyToOne
  @JoinColumn(name = "concepto_gasto_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CONVOCATORIACONCEPTOGASTO_CONCEPTOGASTO"))
  @NotNull
  private ConceptoGasto conceptoGasto;

  /** Observaciones */
  @Column(name = "observaciones", length = 250, nullable = true)
  @Size(max = 250)
  private String observaciones;

  /** Importe máximo */
  @Column(name = "importe_maximo", nullable = true)
  @Min(0)
  private Double importeMaximo;

  /** Permitido */
  @Column(name = "permitido", nullable = true)
  private Boolean permitido;

  /** Mes inicial */
  @Column(name = "mes_inicial", nullable = true)
  @Min(1)
  private Integer mesInicial;

  /** Mes final */
  @Column(name = "mes_final", nullable = true)
  @Min(1)
  private Integer mesFinal;

  /** Porcentaje coste indirecto */
  @Column(name = "porcentaje_coste_indirecto", nullable = true)
  @Min(0)
  private Integer porcentajeCosteIndirecto;

}
package org.crue.hercules.sgi.csp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_finalidad")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoFinalidad extends BaseEntity {

  /**
   * Serial version
   */
  private static final long serialVersionUID = 1L;

  /** Id */
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_finalidad_seq")
  @SequenceGenerator(name = "tipo_finalidad_seq", sequenceName = "tipo_finalidad_seq", allocationSize = 1)
  private Long id;

  /** Nombre */
  @Column(name = "nombre", length = 50, nullable = false)
  @NotEmpty
  @Size(max = 50)
  private String nombre;

  /** Descripción */
  @Column(name = "descripcion", length = 250)
  @Size(max = 250)
  private String descripcion;

  /** Activo */
  @Column(name = "activo", columnDefinition = "boolean default true", nullable = false)
  private Boolean activo;

}
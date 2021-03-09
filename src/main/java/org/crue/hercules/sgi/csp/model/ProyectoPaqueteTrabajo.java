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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proyecto_paquete_trabajo", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "proyecto_id", "nombre" }, name = "UK_PROYECTOPAQUETETRABAJO_PROYECTO_NOMBRE") })
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoPaqueteTrabajo extends BaseEntity {

  /**
   * Serial version
   */
  private static final long serialVersionUID = 1L;

  /** Id */
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proyecto_paquete_trabajo_seq")
  @SequenceGenerator(name = "proyecto_paquete_trabajo_seq", sequenceName = "proyecto_paquete_trabajo_seq", allocationSize = 1)
  private Long id;

  /** Proyecto */
  @ManyToOne
  @JoinColumn(name = "proyecto_id", nullable = false, foreignKey = @ForeignKey(name = "FK_PROYECTOPAQUETETRABAJO_PROYECTO"))
  @NotNull
  private Proyecto proyecto;

  /** Nombre */
  @Column(name = "nombre", length = 50, nullable = false)
  @NotEmpty
  @Size(max = 50)
  private String nombre;

  /** Fecha Inicio. */
  @Column(name = "fecha_inicio", nullable = false)
  @NotNull
  private LocalDate fechaInicio;

  /** Fecha Fin. */
  @Column(name = "fecha_fin", nullable = false)
  @NotNull
  private LocalDate fechaFin;

  /** Persona Mes. */
  @Column(name = "persona_mes", nullable = false)
  @NotNull
  @Min(value = 0L)
  private Double personaMes;

  /** Descripción. */
  @Column(name = "descripcion", length = 2000)
  private String descripcion;

}
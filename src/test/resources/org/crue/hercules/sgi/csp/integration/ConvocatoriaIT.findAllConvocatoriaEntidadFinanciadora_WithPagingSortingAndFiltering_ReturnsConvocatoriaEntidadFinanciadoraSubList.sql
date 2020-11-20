-- MODELO EJECUCION
INSERT INTO csp.modelo_ejecucion (id, nombre, descripcion, activo) VALUES (1, 'nombre-1', 'descripcion-1', true);
INSERT INTO csp.modelo_ejecucion (id, nombre, descripcion, activo) VALUES (2, 'nombre-2', 'descripcion-2', true);

-- MODELO UNIDAD
INSERT INTO csp.modelo_unidad (id, unidad_gestion_ref, modelo_ejecucion_id, activo) VALUES (1, 'unidad-001', 1, true);
INSERT INTO csp.modelo_unidad (id, unidad_gestion_ref, modelo_ejecucion_id, activo) VALUES (2, 'unidad-002', 2, true);

-- TIPO_FINALIDAD
INSERT INTO csp.tipo_finalidad (id,nombre,descripcion,activo) VALUES (1,'nombre-1','descripcion-1',true);
INSERT INTO csp.tipo_finalidad (id,nombre,descripcion,activo) VALUES (2,'nombre-2','descripcion-2',true);

-- MODELO TIPO FINALIDAD
INSERT INTO csp.modelo_tipo_finalidad (id, modelo_ejecucion_id, tipo_finalidad_id, activo) VALUES (1, 1, 1, true);
INSERT INTO csp.modelo_tipo_finalidad (id, modelo_ejecucion_id, tipo_finalidad_id, activo) VALUES (2, 2, 2, true);

-- TIPO_REGIMEN_CONCURRENCIA
INSERT INTO csp.tipo_regimen_concurrencia (id,nombre,activo) VALUES (1,'nombre-1',true);
INSERT INTO csp.tipo_regimen_concurrencia (id,nombre,activo) VALUES (2,'nombre-2',true);

-- TIPO AMBITO GEOGRAFICO
INSERT INTO csp.tipo_ambito_geografico (id, nombre, activo) VALUES (1, 'nombre-001', true);
INSERT INTO csp.tipo_ambito_geografico (id, nombre, activo) VALUES (2, 'nombre-002', true);

-- CONVOCATORIA
INSERT INTO csp.convocatoria
(id, unidad_gestion_ref, modelo_ejecucion_id, codigo, anio, titulo, objeto, observaciones, tipo_finalidad_id, tipo_regimen_concurrencia_id, destinatarios, colaborativos, estado_actual, duracion, tipo_ambito_geografico_id, clasificacion_cvn, activo)
VALUES(1, 'unidad-001', 1, 'codigo-001', 2020, 'titulo-001', 'objeto-001', 'observaciones-001', 1, 1, 'Individual', true, 'Registrada', 12, 1, 'Ayudas y becas', true);
INSERT INTO csp.convocatoria
(id, unidad_gestion_ref, modelo_ejecucion_id, codigo, anio, titulo, objeto, observaciones, tipo_finalidad_id, tipo_regimen_concurrencia_id, destinatarios, colaborativos, estado_actual, duracion, tipo_ambito_geografico_id, clasificacion_cvn, activo)
VALUES(2, 'unidad-002', 1, 'codigo-002', 2020, 'titulo-002', 'objeto-002', 'observaciones-002', 1, 1, 'Equipo de proyecto', true, 'Borrador', 12, 1, 'Proyectos competitivos', true);

-- TIPO ORIGEN FUENTE FINANCIACION
INSERT INTO csp.tipo_origen_fuente_financiacion (id, nombre, activo) VALUES (1, 'nombre-001', true);

-- FUENTE FINANCIACION
INSERT INTO csp.fuente_financiacion (id, nombre, descripcion, fondo_estructural, tipo_ambito_geografico_id, tipo_origen_fuente_financiacion_id, activo) 
  VALUES (1, 'nombre-001', 'descripcion-001', true, 1, 1, true);

-- TIPO FINANCIACION
INSERT INTO csp.tipo_financiacion (id, nombre, descripcion, activo) VALUES (1, 'nombre-001', 'descripcion-001', true);

-- CONVOCATORIA ENTIDAD FINANCIADORA
INSERT INTO csp.convocatoria_entidad_financiadora (id, convocatoria_id, entidad_ref, fuente_financiacion_id, tipo_financiacion_id, porcentaje_financiacion) 
  VALUES (1, 1, 'entidad-001', 1, 1, 20);
INSERT INTO csp.convocatoria_entidad_financiadora (id, convocatoria_id, entidad_ref, fuente_financiacion_id, tipo_financiacion_id, porcentaje_financiacion) 
  VALUES (2, 1, 'entidad-002', null, null, 30);
INSERT INTO csp.convocatoria_entidad_financiadora (id, convocatoria_id, entidad_ref, fuente_financiacion_id, tipo_financiacion_id, porcentaje_financiacion) 
  VALUES (3, 1, 'entidad-003', 1, null , 40);
INSERT INTO csp.convocatoria_entidad_financiadora (id, convocatoria_id, entidad_ref, fuente_financiacion_id, tipo_financiacion_id, porcentaje_financiacion) 
  VALUES (4, 1, 'entidad-4', 1, 1, 10);
INSERT INTO csp.convocatoria_entidad_financiadora (id, convocatoria_id, entidad_ref, fuente_financiacion_id, tipo_financiacion_id, porcentaje_financiacion) 
  VALUES (5, 2, 'entidad-001', 1, 1, 20);
INSERT INTO csp.convocatoria_entidad_financiadora (id, convocatoria_id, entidad_ref, fuente_financiacion_id, tipo_financiacion_id, porcentaje_financiacion)
  VALUES (6, 2, 'entidad-002', null, null, 30);
INSERT INTO csp.convocatoria_entidad_financiadora (id, convocatoria_id, entidad_ref, fuente_financiacion_id, tipo_financiacion_id, porcentaje_financiacion) 
  VALUES (7, 2, 'entidad-003', 1, null , 40);
INSERT INTO csp.convocatoria_entidad_financiadora (id, convocatoria_id, entidad_ref, fuente_financiacion_id, tipo_financiacion_id, porcentaje_financiacion) 
  VALUES (8, 2, 'entidad-004', 1, 1, 10);


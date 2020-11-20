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

--TIPO ENLACE
INSERT INTO csp.tipo_enlace (id,nombre,descripcion,activo) VALUES (1,'nombre-1','descripcion-1',true);

--CONVOCATORIA ENLACE
INSERT INTO  csp.convocatoria_enlace(id, convocatoria_id, url, descripcion, tipo_enlace_id) VALUES (1, 1, 'www.url1.com','descripcion-001', 1);
INSERT INTO  csp.convocatoria_enlace(id, convocatoria_id, url, descripcion, tipo_enlace_id) VALUES (2, 1, 'www.url1.com','descripcion-002', 1);
INSERT INTO  csp.convocatoria_enlace(id, convocatoria_id, url, descripcion, tipo_enlace_id) VALUES (3, 1, 'www.url1.com','descripcion-003', 1);
INSERT INTO  csp.convocatoria_enlace(id, convocatoria_id, url, descripcion, tipo_enlace_id) VALUES (4, 1, 'www.url1.com','descripcion-4', 1);
INSERT INTO  csp.convocatoria_enlace(id, convocatoria_id, url, descripcion, tipo_enlace_id) VALUES (5, 1, 'www.url1.com','descripcion-05', 1);
INSERT INTO  csp.convocatoria_enlace(id, convocatoria_id, url, descripcion, tipo_enlace_id) VALUES (6, 1, 'www.url1.com','descripcion-06', 1);
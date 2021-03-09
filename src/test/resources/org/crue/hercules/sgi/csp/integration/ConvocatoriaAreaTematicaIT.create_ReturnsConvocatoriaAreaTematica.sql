-- MODELO EJECUCION
INSERT INTO csp.modelo_ejecucion (id, nombre, descripcion, activo) VALUES (1, 'nombre-1', 'descripcion-1', true);

-- TIPO_FINALIDAD
INSERT INTO csp.tipo_finalidad (id,nombre,descripcion,activo) VALUES (1,'nombre-1','descripcion-1',true);

-- MODELO TIPO FINALIDAD
INSERT INTO csp.modelo_tipo_finalidad (id, modelo_ejecucion_id, tipo_finalidad_id, activo) VALUES (1, 1, 1, true);

-- TIPO_REGIMEN_CONCURRENCIA
INSERT INTO csp.tipo_regimen_concurrencia (id,nombre,activo) VALUES (1,'nombre-1',true);

-- TIPO AMBITO GEOGRAFICO
INSERT INTO csp.tipo_ambito_geografico (id, nombre, activo) VALUES (1, 'nombre-001', true);

-- CONVOCATORIA
INSERT INTO csp.convocatoria
(id, unidad_gestion_ref, modelo_ejecucion_id, codigo, anio, titulo, objeto, observaciones, tipo_finalidad_id, tipo_regimen_concurrencia_id, destinatarios, colaborativos, estado, duracion, tipo_ambito_geografico_id, clasificacion_cvn, activo)
VALUES(1, 'unidad-001', 1, 'codigo-001', 2020, 'titulo-001', 'objeto-001', 'observaciones-001', 1, 1, 'INDIVIDUAL', true, 'REGISTRADA', 12, 1, 'AYUDAS', true);

-- AREA TEMATICA
INSERT INTO csp.area_tematica (id, nombre, descripcion, area_tematica_padre_id, activo) VALUES (1, 'nombre-001', 'descripcion-001', null, true);
INSERT INTO csp.area_tematica (id, nombre, descripcion, area_tematica_padre_id, activo) VALUES (2, 'A-1', 'nombre-A-1', 1, true);
INSERT INTO csp.area_tematica (id, nombre, descripcion, area_tematica_padre_id, activo) VALUES (3, 'nombre-002', 'descripcion-002', null, true);
INSERT INTO csp.area_tematica (id, nombre, descripcion, area_tematica_padre_id, activo) VALUES (4, 'A-2', 'nombre-A-2', 2, true);

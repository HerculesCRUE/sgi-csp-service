
-- DEPENDENCIAS: estado_solicitud, convocatoria
/*
  scripts = { 
    // @formatter:off
    "classpath:scripts/modelo_ejecucion.sql",
    "classpath:scripts/tipo_finalidad.sql",
    "classpath:scripts/tipo_regimen_concurrencia.sql",
    "classpath:scripts/tipo_ambito_geografico.sql",
    "classpath:scripts/convocatoria.sql",
    "classpath:scripts/estado_solicitud.sql"
    // @formatter:on
  }
*/

INSERT INTO csp.solicitud 
  (id, codigo_externo, codigo_registro_interno, estado_solicitud_id, convocatoria_id, creador_ref, solicitante_ref, observaciones, convocatoria_externa, unidad_gestion_ref, formulario_solicitud, activo)
VALUES 
  (1, null, 'SGI_SLC1202011061027', null, 1, 'usr-001', 'usr-002', 'observaciones-001', null, 'OPE', 'ESTANDAR', true),
  (2, null, 'SGI_SLC2202011061027', null, 1, 'usr-001', 'usr-002', 'observaciones-002', null, 'OPE', 'ESTANDAR', true),
  (3, null, 'SGI_SLC3202011061027', null, 1, 'usr-001', 'usr-002', 'observaciones-003', null, 'OPE', 'ESTANDAR', false),
  (4, null, 'SGI_SLC4202011061027', null, 1, 'usr-001', 'usr-002', 'observaciones-004', null, 'OTRI', 'ESTANDAR', true),
  (5, null, 'SGI_SLC5202011061027', null, 1, 'usr-001', 'usr-002', 'observaciones-005', null, 'OTRI', 'ESTANDAR', true);

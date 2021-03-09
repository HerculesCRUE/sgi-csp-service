-- DEPENDENCIAS: proyecto
/*
  scripts = { 
    "classpath:scripts/modelo_ejecucion.sql",
    "classpath:scripts/modelo_unidad.sql",
    "classpath:scripts/tipo_finalidad.sql",
    "classpath:scripts/tipo_ambito_geografico.sql",
    "classpath:scripts/estado_proyecto.sql",    
    "classpath:scripts/proyecto.sql"
  }
*/

INSERT INTO csp.proyecto_prorroga
(id, fecha_concesion, fecha_fin, importe, num_prorroga, observaciones, tipo, proyecto_id)
VALUES(1, '2020-01-01', '2020-02-01', 123.45, 1, 'observaciones-proyecto-prorroga-001', 'TIEMPO_IMPORTE', 1);

INSERT INTO csp.proyecto_prorroga
(id, fecha_concesion, fecha_fin, importe, num_prorroga, observaciones, tipo, proyecto_id)
VALUES(2, '2020-02-01', '2020-03-01', 123.45, 2, 'observaciones-proyecto-prorroga-002', 'TIEMPO', 1);

INSERT INTO csp.proyecto_prorroga
(id, fecha_concesion, fecha_fin, importe, num_prorroga, observaciones, tipo, proyecto_id)
VALUES(3, '2020-03-01', '2020-04-01', 123.45, 3, 'observaciones-proyecto-prorroga-003', 'TIEMPO_IMPORTE', 1);

INSERT INTO csp.proyecto_prorroga
(id, fecha_concesion, fecha_fin, importe, num_prorroga, observaciones, tipo, proyecto_id)
VALUES(4, '2020-04-01', '2020-05-01', 543.21, 4, 'observaciones-proyecto-prorroga-4', 'IMPORTE', 1);

INSERT INTO csp.proyecto_prorroga
(id, fecha_concesion, fecha_fin, importe, num_prorroga, observaciones, tipo, proyecto_id)
VALUES(5, '2020-05-01', '2020-06-01', 543.21, 5, 'observaciones-proyecto-prorroga-5', 'TIEMPO_IMPORTE', 1);

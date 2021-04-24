-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'owner1','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner2','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'owner2','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner3','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'owner3','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner4','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'owner4','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner5','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'owner5','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner6','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (7,'owner6','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner7','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (8,'owner7','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner8','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (9,'owner8','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner9','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (10,'owner9','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner10','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (11,'owner10','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (12,'vet1','veterinarian');

INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiología');
INSERT INTO specialties VALUES (2, 'cirugía');
INSERT INTO specialties VALUES (3, 'odontología');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'gato');
INSERT INTO types VALUES (2, 'perro');
INSERT INTO types VALUES (3, 'lagartija');
INSERT INTO types VALUES (4, 'serpiente');
INSERT INTO types VALUES (5, 'pajaro');
INSERT INTO types VALUES (6, 'hamster');
INSERT INTO types VALUES (7, 'conejo');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner2');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner3');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner4');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner5');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner6');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner7');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner8');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner9');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner10');

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'vacuna de la rabia');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'vacuna de la rabia');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'castrado');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'esterilizado');

INSERT INTO habitacion(id, numero) VALUES (1,1);
INSERT INTO habitacion(id, numero) VALUES (2,2);
INSERT INTO habitacion(id, numero) VALUES (3,3);
INSERT INTO habitacion(id, numero) VALUES (4,4);
INSERT INTO habitacion(id, numero) VALUES (5,5);
INSERT INTO habitacion(id, numero) VALUES (6,6);
INSERT INTO habitacion(id, numero) VALUES (7,7);
INSERT INTO habitacion(id, numero) VALUES (8,8);
INSERT INTO habitacion(id, numero) VALUES (9,9);
INSERT INTO habitacion(id, numero) VALUES (10,10);
INSERT INTO habitacion(id, numero) VALUES (11,11);
INSERT INTO habitacion(id, numero) VALUES (12,12);
INSERT INTO habitacion(id, numero) VALUES (13,13);
INSERT INTO habitacion(id, numero) VALUES (14,14);
INSERT INTO habitacion(id, numero) VALUES (15,15);

INSERT INTO reserva(id, fecha_ini, fecha_fin, pet_id, habitacion_id) VALUES (1, '2021-03-19','2021-03-20', 1, 1);
INSERT INTO reserva(id, fecha_ini, fecha_fin, pet_id, habitacion_id) VALUES (2, '2021-03-19','2021-03-20', 2, 2);
INSERT INTO reserva(id, fecha_ini, fecha_fin, pet_id, habitacion_id) VALUES (3, '2021-03-21','2021-03-27', 1, 2);
INSERT INTO reserva(id, fecha_ini, fecha_fin, pet_id, habitacion_id) VALUES (4, CURDATE(), DATEADD(day, 14, CURDATE()), 4, 4);

INSERT INTO reserva(id, fecha_ini, fecha_fin, pet_id, habitacion_id) VALUES (5, '2021-03-21','2021-03-23', 3, 3);
                                                                             
INSERT INTO causas(id, nombre, organizacion, descripcion, recaudacion_objetivo) VALUES (1, 'ANIMALES SIN DUEÑO', 'PetClinic', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus in lectus non mi tristique dapibus. Donec eget justo dui. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Duis luctus sapien a nibh placerat, nec vestibulum eros mattis. Curabitur viverra at risus in volutpat. Duis ut orci felis. In iaculis metus sit amet nibh convallis, quis porta magna tincidunt. Donec ante nunc, accumsan a sapien sed, sodales feugiat velit. Integer suscipit tempor nibh. Ut interdum vestibulum lacus at dapibus. Cras fringilla malesuada fringilla. Cras viverra egestas bibendum. Duis sed sapien quis justo mattis porta.Mauris non sodales ipsum. Suspendisse sit amet mollis nisi. Suspendisse eget odio volutpat magna lacinia finibus. Suspendisse vestibulum ligula neque, at varius mi molestie ac. Nam nec mi ac libero condimentum mollis. Nunc eu nulla sed massa facilisis feugiat. Proin elementum metus eu ante malesuada maximus. Morbi sed mauris pulvinar, convallis risus non, imperdiet nisi. Quisque eleifend, tellus ut finibus scelerisque, sem mauris tristique ipsum, non sodales sapien sem in erat. Donec ornare, magna in sollicitudin blandit, dolor lectus molestie orci, eu mattis nunc sem ut diam. Nam vestibulum rhoncus nunc nec sollicitudin. In tempor urna sit amet mauris venenatis dapibus.', 50000);
INSERT INTO causas(id, nombre, organizacion, descripcion, recaudacion_objetivo) VALUES (2, 'INVESTIGACIÓN ENFERMERMEDAD TOXOPLASMOSIS', 'PetClinic', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus in lectus non mi tristique dapibus. Donec eget justo dui. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Duis luctus sapien a nibh placerat, nec vestibulum eros mattis. Curabitur viverra at risus in volutpat. Duis ut orci felis. In iaculis metus sit amet nibh convallis, quis porta magna tincidunt. Donec ante nunc, accumsan a sapien sed, sodales feugiat velit. Integer suscipit tempor nibh. Ut interdum vestibulum lacus at dapibus. Cras fringilla malesuada fringilla. Cras viverra egestas bibendum. Duis sed sapien quis justo mattis porta.', 50000);
INSERT INTO causas(id, nombre, organizacion, descripcion, recaudacion_objetivo) VALUES (3, 'COLECTA DE PIENSO', 'Hogar Animal Madrid', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus in lectus non mi tristique dapibus. Donec eget justo dui. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Duis luctus sapien a nibh placerat, nec vestibulum eros mattis. Curabitur viverra at risus in volutpat. Duis ut orci felis. In iaculis metus sit amet nibh convallis, quis porta magna tincidunt. Donec ante nunc, accumsan a sapien sed, sodales feugiat velit. Integer suscipit tempor nibh. Ut interdum vestibulum lacus at dapibus. Cras fringilla malesuada fringilla. Cras viverra egestas bibendum. Duis sed sapien quis justo mattis porta.', 2500);
INSERT INTO causas(id, nombre, organizacion, descripcion, recaudacion_objetivo) VALUES (4, 'RECAUDACIÓN PARA MATERIAL MEDICO', 'PetClinic', 'Donec vel sollicitudin nunc, gravida hendrerit metus. Pellentesque ex ipsum, imperdiet varius nisi vitae, vestibulum volutpat justo. Morbi tempus velit semper sem semper, a eleifend magna luctus. Morbi dictum eros nunc. Phasellus in dapibus nisl. Fusce quis nisi at erat ullamcorper rhoncus sit amet nec ipsum. Nunc aliquet ut dolor ac iaculis. Fusce pharetra, nibh id luctus ultricies, orci mauris malesuada ex, ut consectetur metus nibh in nulla. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nam egestas ligula quis viverra ullamcorper.', 10000);

INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (1, '2021-03-19', 50, 1, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (2, '2020-04-21', 80, 1, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (3, '2021-01-10', 20, 1, 2);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (4, '2019-07-04', 25, 2, 4);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (5, '2021-04-12', 32, 2, 5);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (6, '2021-03-29', 65, 2, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (7, '2021-04-19', 10, 3, 7);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (8, '2021-02-01', 15, 3, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (9, '2021-01-26', 30, 3, 6);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (10, '2021-03-09', 50, 2, 7);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (11, '2021-04-04', 20, 4, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (12, '2021-01-31', 105, 4, 5);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (13, '2021-01-20', 30, 4, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (14, '2021-03-17', 20, 2, 6);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (15, '2021-02-15', 10, 1, 9);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (16, '2021-03-19', 50, 1, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (17, '2020-01-21', 80, 1, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (18, '2021-01-10', 20, 1, 2);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (19, '2021-03-19', 50, 1, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (20, '2020-04-21', 80, 1, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (21, '2021-01-10', 20, 1, 2);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (22, '2021-03-19', 50, 1, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (23, '2020-02-21', 80, 1, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (24, '2021-01-10', 20, 1, 2);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (25, '2021-03-19', 50, 1, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (26, '2020-03-21', 80, 1, 1);
INSERT INTO donaciones(id, fecha_don, cantidad, id_causa, id_owner) VALUES (27, '2021-01-10', 20, 1, 2);




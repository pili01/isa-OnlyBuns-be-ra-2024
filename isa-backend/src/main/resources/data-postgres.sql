insert into student (index_number, first_name, last_name)
values ('5', 'Marko', 'Marković');
insert into student (index_number, first_name, last_name)
values ('ra2-2014', 'Milan', 'Milanović');
insert into student (index_number, first_name, last_name)
values ('ra3-2014', 'Ivana', 'Ivanović');
insert into student (index_number, first_name, last_name)
values ('ra4-2014', 'Bojan', 'Bojanović');
insert into student (index_number, first_name, last_name)
values ('ra5-2014', 'Pera', 'Perić');
insert into student (index_number, first_name, last_name)
values ('ra6-2014', 'Zoran', 'Zoranović');
insert into student (index_number, first_name, last_name)
values ('ra7-2014', 'Bojana', 'Bojanović');
insert into student (index_number, first_name, last_name)
values ('ra8-2014', 'Milana', 'Milanović');
insert into student (index_number, first_name, last_name)
values ('ra9-2014', 'Jovana', 'Jovanić');

insert into course (name)
values ('Matematika');
insert into course (name)
values ('Osnove programiranja');
insert into course (name)
values ('Objektno programiranje');

insert into teacher (first_name, last_name, deleted)
values ('Strahinja', 'Simić', false);
insert into teacher (first_name, last_name, deleted)
values ('Marina', 'Antić', false);
insert into teacher (first_name, last_name, deleted)
values ('Siniša', 'Branković', false);

insert into teaching (course_id, teacher_id)
values (1, 1);
insert into teaching (course_id, teacher_id)
values (1, 2);
insert into teaching (course_id, teacher_id)
values (2, 2);
insert into teaching (course_id, teacher_id)
values (3, 3);

insert into exam (student_id, course_id, date, grade)
values (1, 1, '2016-02-01', 9);
insert into exam (student_id, course_id, date, grade)
values (1, 2, '2016-04-19', 8);
insert into exam (student_id, course_id, date, grade)
values (2, 1, '2016-02-01', 10);
insert into exam (student_id, course_id, date, grade)
values (2, 2, '2016-04-19', 10);

INSERT INTO role (id, name) VALUES (1, 'NOT_AUTHENTICATED');
INSERT INTO role (id, name) VALUES (2, 'AUTHENTICATED');
INSERT INTO role (id, name) VALUES (3, 'ADMIN');

/*DIO ZA DODAVANJE USERA, POSTOVA I KOMENTARA U BAZU*/
INSERT INTO public.users(email, enabled, first_name, deleted, last_name, last_password_reset_date, password,
                         username)
VALUES ('user@gmail.com', true, 'User', 'false', 'Useric', '2016-04-19',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'user');

INSERT INTO public.user_role(user_id, role_id)
VALUES (1, 1);

INSERT INTO public.users(email, enabled, first_name, deleted, last_name, last_password_reset_date, password,
                         username)
VALUES ('admin@gmail.com', true, 'Admin', 'false', 'Adminic', '2016-04-19',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'admin');

INSERT INTO public.user_role(user_id, role_id)
VALUES (2, 2);

INSERT INTO public.users(email, enabled, first_name, deleted, last_name, last_password_reset_date, password,
                         username)
VALUES ('user1@gmail.com', true, 'User1', 'false', 'Useric', '2016-04-19',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'user1');

INSERT INTO public.user_role(user_id, role_id)
VALUES (3, 1);

INSERT INTO public.location(latitude, longitude)
VALUES (25, 16);

INSERT INTO public.posts(created_at, description, image_path, deleted, user_id, location_id)
VALUES ('2020-05-06', 'OPis komentara broj 1', 'putanja_slike.jpg', false, 1, 1);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku', '2023-02-02', false, 1, 1);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku 1', '2024-08-05', false, 3, 1);


INSERT INTO public.location(latitude, longitude)
VALUES (44.21, 25.64);

INSERT INTO public.posts(created_at, description, image_path, deleted, user_id, location_id)
VALUES ('2024-05-06', 'OPis komentara broj 2', 'putanja_slike2.jpg', false, 3, 2);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku 2', '2024-05-05', false, 1, 2);













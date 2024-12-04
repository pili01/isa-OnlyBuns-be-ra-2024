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
INSERT INTO public.users(address,email, enabled, first_name, deleted, last_name, password,
                         username, role_id,following_count,number_of_posts,creation_time,followers_count)
VALUES ('Petra Petrovica','user@gmail.com', true, 'Marko', false, 'Marković',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'user', 2,0,0,'2021-04-15',0);

INSERT INTO public.users(address,email, enabled, first_name, deleted, last_name, password,
                         username, role_id,following_count,number_of_posts,creation_time,followers_count)
VALUES ('Petra Petrovica','admin@gmail.com', true, 'Admin', false, 'Adminić',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'admin', 3,0,0,'2022-04-15',0);

INSERT INTO public.users(address,email, enabled, first_name, deleted, last_name, password,
                         username, role_id,following_count,number_of_posts,creation_time,followers_count)
VALUES ('Petra Petrovica','papovic@gmail.com', true, 'Ognjen', false, 'Papović',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'papovic', 2,0,2,'2021-04-09',0);

INSERT INTO public.users(address,email, enabled, first_name, deleted, last_name, password,
                         username, role_id,following_count,number_of_posts,creation_time,followers_count)
VALUES ('Dusko Petrovica','pilipovic@gmail.com', true, 'Duško', false, 'Pilipović',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'pilipovic', 2,0,3,'2021-04-07',0);

INSERT INTO public.users(address,email, enabled, first_name, deleted, last_name, password,
                         username, role_id,following_count,number_of_posts,creation_time,followers_count)
VALUES ('Petra Petrovica','medic@gmail.com', true, 'Miloš', false, 'Medić',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'medic', 2,0,2,'2020-04-08',0);

INSERT INTO public.users(address,email, enabled, first_name, deleted, last_name, password,
                         username, role_id,following_count,number_of_posts,creation_time,followers_count)
VALUES ('Petra Petrovica','user4@gmail.com', true, 'Petar', false, 'Petrović',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'petrovic', 2,0,0,'2022-04-25',0);

INSERT INTO public.users(address,email, enabled, first_name, deleted, last_name, password,
                         username, role_id,following_count,number_of_posts,creation_time,followers_count)
VALUES ('Petra Petrovica','user5@gmail.com', true, 'Gavrilo', false, 'Gavrilović',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'gavrilovic', 2,0,0,'2024-04-19',2);

INSERT INTO public.users(address,email, enabled, first_name, deleted, last_name, password,
                         username, role_id,following_count,number_of_posts,creation_time,followers_count)
VALUES ('Petra Petrovica','user6@gmail.com', true, 'Ana', false, 'Anić',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'ana', 2,1,0,'2022-04-25',0);

INSERT INTO public.users(address,email, enabled, first_name, deleted, last_name, password,
                         username, role_id,following_count,number_of_posts,creation_time,followers_count)
VALUES ('P','d@gmail.com', true, 'D', false, 'D',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'd', 2,1,0,'2024-09-25',0);

INSERT INTO public.users(address,email, enabled, first_name, deleted, last_name, password,
                         username, role_id,following_count,number_of_posts,creation_time,followers_count)
VALUES ('Petra Petrovicaa','user8@gmail.com', false, 'User8', false, 'Useric',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'user8', 1,0,0,'2024-11-02',0);

INSERT INTO public.users(address,email, enabled, first_name, deleted, last_name, password,
                         username, role_id,following_count,number_of_posts,creation_time,followers_count)
VALUES ('Petra Petrovicaa','user9@gmail.com', false, 'User9', false, 'Useric',
        '$2a$10$6TWEkn.KiuKNYOgd4UGYFO4ECeYHbszRSdjVXTw1cFpn9RV60s6Py', 'user9', 1,0,0,'2024-11-29',0);

INSERT INTO public.locations(latitude, longitude,deleted)
VALUES (25, 16,false);

INSERT INTO public.posts(created_at, description, image_path, deleted, user_id, location_id)
VALUES ('2024-09-06', 'OPis komentara broj za sliku broj 1 koji predstavlja malo duzi opis od standardnog 1', '1731399224385-381782.png', false, 3, 1);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku', '2023-02-02', false, 1, 1);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku 1', '2024-08-05', false, 3, 1);


INSERT INTO public.locations(latitude, longitude,deleted)
VALUES (44.21, 25.64,false);

INSERT INTO public.posts(created_at, description, image_path, deleted, user_id, location_id)
VALUES ('2023-05-06', 'OPis komentara broj 2', '1731398041004-logo1.png', false, 4, 2);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku 2', '2024-10-05', false, 5, 2);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku 2', '2023-01-05', false, 6, 2);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku 2', '2024-05-30', false, 3, 2);


INSERT INTO public.locations(latitude, longitude,deleted)
VALUES (44, 21,false);

INSERT INTO public.posts(created_at, description, image_path, deleted, user_id, location_id)
VALUES ('2024-11-04', 'OPis posta broj 3', '1731398041004-logo1.png', false, 5, 3);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku 2', '2024-11-05', false, 4, 3);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku 2', '2024-11-09', false, 3, 3);

INSERT INTO public.locations(latitude, longitude,deleted)
VALUES (44, 21,false);
INSERT INTO public.posts(created_at, description, image_path, deleted, user_id, location_id)
VALUES ('2024-11-03', 'OPis posta broj 31', '1731399263270-zenka-divljeg-zeca.jpg', false, 5, 4);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku 2', '2024-11-15', false, 6, 4);

INSERT INTO public.comments(comment, creation_time, deleted, user_id, post_id)
VALUES ('Komentar vezan za sliku 2', '2024-11-07', false, 7, 4);

INSERT INTO public.locations(latitude, longitude,deleted)
VALUES (14, 56,false);

INSERT INTO public.posts(created_at, description, image_path, deleted, user_id, location_id)
VALUES ('2010-08-06', 'OPis posta broj 4', '1731399251410-zec-slika-profil.jpg', false, 5, 5);

INSERT INTO public.locations(latitude, longitude,deleted)
VALUES (44, 21,false);

INSERT INTO public.posts(created_at, description, image_path, deleted, user_id, location_id)
VALUES ('2015-09-04', 'OPis posta broj 5', '1731399263270-zenka-divljeg-zeca.jpg', false, 3, 6);

INSERT INTO public.locations(latitude, longitude,deleted)
VALUES (14, 56,false);

INSERT INTO public.posts(created_at, description, image_path, deleted, user_id, location_id)
VALUES ('2017-08-06', 'OPis posta broj 6', '1731399276728-Znakovi-da-je-vas-zec-bolestan.jpg', false, 4, 7);

INSERT INTO public.post_likers(
    post_id, user_id)
VALUES (1, 1);

INSERT INTO public.post_likers(
    post_id, user_id)
VALUES (2, 3);

INSERT INTO public.post_likers(
    post_id, user_id)
VALUES (1, 3);

INSERT INTO public.locations(latitude, longitude,deleted)
VALUES (14, 56,false);
INSERT INTO public.posts(created_at, description, image_path, deleted, user_id, location_id)
VALUES ('2021-08-06', 'OPis posta broj 7', '1731399381518-1000_F_581886343_5UMhXzWzBdeecZz28z3ok5kiaHiCwt5v.jpg', false, 4, 8);

INSERT INTO public.post_likers(
    post_id, user_id)
VALUES (7, 1);

INSERT INTO public.followings(
    follower_id, followed_id)
VALUES (8, 7);

INSERT INTO public.followings(
    follower_id, followed_id)
VALUES (9, 7);

INSERT INTO public.followers(
    followed_id, follower_id)
VALUES (7, 8);

INSERT INTO public.followers(
    followed_id, follower_id)
VALUES (7, 9);
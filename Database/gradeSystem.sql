create database gradeSystem;

use gradeSystem;

create table user(
	id_user int not null auto_increment primary key,
	username varchar(100),
    email varchar(100) not null unique,
    user_password varchar(250) not null
);

create table collection(
	id_collection int not null auto_increment primary key,
    collection_name varchar(100),
    id_user_fk int not null,
    foreign key (id_user_fk) references user(id_user) on delete cascade on update cascade
);

create table artist(
	id_artist int not null auto_increment primary key,
    artist_name varchar(100)
);

create table album(
	id_album int not null auto_increment primary key,
    album_name varchar(100),
    album_year int,
	id_artist_fk int not null,
    foreign key (id_artist_fk) references artist(id_artist) on delete cascade on update cascade
);

create table collection_album(
	id_collection_album int not null auto_increment primary key,
    id_album_fk int not null,
    id_collection_fk int not null,
	foreign key (id_album_fk) references album(id_album) on delete cascade on update cascade,
 	foreign key (id_collection_fk) references collection(id_collection) on delete cascade on update cascade   
);

create table song(
	id_song int not null auto_increment primary key,
    song_name varchar(100),
    grade int,
	id_album_fk int not null,
	foreign key (id_album_fk) references album(id_album) on delete cascade on update cascade
);

create table feature(
	id_feature int not null auto_increment primary key,
    id_song_fk int not null,
    id_artist_fk int not null,
	foreign key (id_song_fk) references song(id_song) on delete cascade on update cascade,
	foreign key (id_artist_fk) references artist(id_artist) on delete cascade on update cascade
);

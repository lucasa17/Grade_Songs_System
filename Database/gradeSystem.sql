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
	id_collection_fk int not null,
	id_artist_fk int not null,
	foreign key (id_collection_fk) references collection(id_collection) on delete cascade on update cascade,
    foreign key (id_artist_fk) references artist(id_artist) on delete cascade on update cascade
);

create table song(
	id_song int not null auto_increment primary key,
    song_name varchar(100),
    grade int,
	feature_name varchar(255),
	id_album_fk int not null,
	foreign key (id_album_fk) references album(id_album) on delete cascade on update cascade
);

DELIMITER //
CREATE TRIGGER tr_delete_artist_after_album
AFTER DELETE ON album
FOR EACH ROW
BEGIN
    DECLARE album_count INT;
    SELECT COUNT(*) INTO album_count
    FROM album
    WHERE id_artist_fk = OLD.id_artist_fk;

    IF album_count = 0 THEN
        DELETE FROM artist
        WHERE id_artist = OLD.id_artist_fk;
    END IF;
END //
DELIMITER ;

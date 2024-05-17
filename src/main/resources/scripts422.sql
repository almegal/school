create table Person ( -- создание таблицы
    id serial primary key, -- автокинкремент уникального айди
    name varchar(100) not null unique, -- имя небольше 100 символов уникальное и не нал
    age SMALLINT not null, -- возраст не нал
    drive_license boolean -- лицензия водителя
)

create table Car ( -- создание таблицы
    id serial primary key, -- автокинкремент уникального айди
    mark varchar(100) not null unique, -- уникальная марка не больше 100 символов и не нал
    model varchar(100) not null unique,-- уникальная модель не больше 100 символов и не нал
    price int not null -- цена авто не нал
)

create table PersonCar ( -- таблица для связи многие к многим
    person_id int not null, -- ключ хранящий айди человека
    car_id int not null, -- ключ хранящий айди машины
    primary key(person_id, car_id), -- уникальный идентификатор состоящий из двух внешних ключей
    foreign key (person_id) references person(id), -- внешний ключ ссылающийся на айди человека
    foreign key (car_id) references car(id) -- внешний ключ ссылающийся на айди машины
)
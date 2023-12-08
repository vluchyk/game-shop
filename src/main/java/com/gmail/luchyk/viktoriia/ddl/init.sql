CREATE TABLE public.users
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    name character varying(255) NOT NULL,
    nickname character varying(32),
    birthday timestamp without time zone,
    password character varying(32),
    PRIMARY KEY (id),
    CONSTRAINT nickname_unique UNIQUE (nickname)
);

CREATE TABLE public.games
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    name character varying(50) NOT NULL,
    release_date timestamp without time zone,
    rating bigint,
    cost double precision,
    description character varying(255),
    PRIMARY KEY (id),
    CONSTRAINT name_unique UNIQUE (name)
);

CREATE TABLE public.aux_user_game
(
    user_id bigint NOT NULL,
    game_id bigint NOT NULL,
    CONSTRAINT user_id FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE,
    CONSTRAINT game_id FOREIGN KEY (game_id)
        REFERENCES public.games (id) MATCH SIMPLE
);

CREATE TABLE public.accounts
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    amount double precision,
    type character varying(32),
    user_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT user_id_unique UNIQUE (user_id),
    CONSTRAINT user_id FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
);
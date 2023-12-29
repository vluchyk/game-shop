CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    name character varying(255) NOT NULL,
    nickname character varying(32) UNIQUE,
    birthday timestamp without time zone,
    password character varying(32),
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS public.games
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    name character varying(50) NOT NULL UNIQUE,
    release_date timestamp without time zone,
    rating bigint,
    cost double precision,
    description character varying(255),
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS public.aux_user_game
(
    user_id bigint NOT NULL REFERENCES public.users (id),
    game_id bigint NOT NULL REFERENCES public.games (id)
);


CREATE TABLE IF NOT EXISTS public.accounts
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    amount double precision,
    type character varying(32),
    user_id bigint REFERENCES public.users (id) UNIQUE,
    PRIMARY KEY (id)
);
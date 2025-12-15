--
-- PostgreSQL database dump
--

\restrict aE3yB9v4XPaYxD37n0YFJ993GxDotLsgxz9512FGBFuXgX9il1oIdySmBg7NpRK

-- Dumped from database version 17.6
-- Dumped by pg_dump version 17.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: authors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authors
(
    user_id   integer,
    id        bigint                 NOT NULL,
    bio       character varying(255) NOT NULL,
    name      character varying(255) NOT NULL,
    pseudonym character varying(255) NOT NULL,
    surname   character varying(255) NOT NULL
);


ALTER TABLE public.authors
    OWNER TO postgres;

--
-- Name: authors_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.authors_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.authors_seq OWNER TO postgres;

--
-- Name: book_images; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_images
(
    book_id   bigint,
    id        bigint NOT NULL,
    public_id character varying(255),
    url       character varying(255)
);


ALTER TABLE public.book_images
    OWNER TO postgres;

--
-- Name: book_images_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_images_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.book_images_seq OWNER TO postgres;

--
-- Name: books; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.books
(
    date_posted date,
    price       double precision       NOT NULL,
    author_id   bigint,
    id          bigint                 NOT NULL,
    description character varying(255) NOT NULL,
    genre       character varying(255) NOT NULL,
    title       character varying(255) NOT NULL,
    CONSTRAINT books_genre_check CHECK (((genre)::text = ANY
                                         ((ARRAY ['FICTION'::character varying, 'NON_FICTION'::character varying, 'MYSTERY'::character varying, 'ROMANCE'::character varying, 'SCIENCE_FICTION'::character varying, 'FANTASY'::character varying, 'BIOGRAPHY'::character varying])::text[])))
);


ALTER TABLE public.books
    OWNER TO postgres;

--
-- Name: books_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.books_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.books_seq OWNER TO postgres;

--
-- Name: cart_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cart_item
(
    quantity integer NOT NULL,
    book_id  bigint,
    cart_id  bigint,
    id       bigint  NOT NULL
);


ALTER TABLE public.cart_item
    OWNER TO postgres;

--
-- Name: cart_item_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cart_item_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cart_item_seq OWNER TO postgres;

--
-- Name: carts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.carts
(
    user_id integer,
    id      bigint NOT NULL
);


ALTER TABLE public.carts
    OWNER TO postgres;

--
-- Name: carts_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.carts_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.carts_seq OWNER TO postgres;

--
-- Name: images; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.images
(
    id        integer NOT NULL,
    user_id   integer,
    public_id character varying(255),
    url       character varying(255)
);


ALTER TABLE public.images
    OWNER TO postgres;

--
-- Name: images_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.images_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.images_seq OWNER TO postgres;

--
-- Name: likes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.likes
(
    user_id integer NOT NULL,
    book_id bigint  NOT NULL,
    id      bigint  NOT NULL
);


ALTER TABLE public.likes
    OWNER TO postgres;

--
-- Name: likes_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.likes_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.likes_seq OWNER TO postgres;

--
-- Name: moderation_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.moderation_requests
(
    price           double precision       NOT NULL,
    author_id       bigint,
    created_at      timestamp(6) without time zone,
    id              bigint                 NOT NULL,
    description     character varying(255) NOT NULL,
    genre           character varying(255) NOT NULL,
    image_public_id character varying(255),
    image_url       character varying(255),
    reason          character varying(255),
    status          character varying(255) NOT NULL,
    title           character varying(255) NOT NULL,
    CONSTRAINT moderation_requests_genre_check CHECK (((genre)::text = ANY
                                                       ((ARRAY ['FICTION'::character varying, 'NON_FICTION'::character varying, 'MYSTERY'::character varying, 'ROMANCE'::character varying, 'SCIENCE_FICTION'::character varying, 'FANTASY'::character varying, 'BIOGRAPHY'::character varying])::text[]))),
    CONSTRAINT moderation_requests_status_check CHECK (((status)::text = ANY
                                                        ((ARRAY ['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.moderation_requests
    OWNER TO postgres;

--
-- Name: moderation_requests_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.moderation_requests_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.moderation_requests_seq OWNER TO postgres;

--
-- Name: refresh_tokens; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.refresh_tokens
(
    user_id     integer,
    expiry_date timestamp(6) with time zone NOT NULL,
    id          bigint                      NOT NULL,
    token       character varying(255)      NOT NULL
);


ALTER TABLE public.refresh_tokens
    OWNER TO postgres;

--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.refresh_tokens
    ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.refresh_tokens_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );


--
-- Name: user_verification; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_verification
(
    id                bigint                 NOT NULL,
    email             character varying(255) NOT NULL,
    verification_code character varying(255) NOT NULL
);


ALTER TABLE public.user_verification
    OWNER TO postgres;

--
-- Name: user_verification_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.user_verification
    ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.user_verification_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users
(
    active   boolean                NOT NULL,
    id       integer                NOT NULL,
    email    character varying(255),
    password character varying(255),
    roles    character varying(255),
    username character varying(255) NOT NULL
);


ALTER TABLE public.users
    OWNER TO postgres;

--
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_seq OWNER TO postgres;

SELECT pg_catalog.setval('public.authors_seq', 51, true);


--
-- Name: book_images_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.book_images_seq', 101, true);


--
-- Name: books_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.books_seq', 101, true);


--
-- Name: cart_item_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cart_item_seq', 101, true);


--
-- Name: carts_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.carts_seq', 51, true);


--
-- Name: images_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.images_seq', 1, true);


--
-- Name: likes_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.likes_seq', 1, false);


--
-- Name: moderation_requests_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.moderation_requests_seq', 101, true);


--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.refresh_tokens_id_seq', 27, true);


--
-- Name: user_verification_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_verification_id_seq', 6, true);


--
-- Name: users_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_seq', 201, true);


--
-- Name: authors authors_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors
    ADD CONSTRAINT authors_name_key UNIQUE (name);


--
-- Name: authors authors_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors
    ADD CONSTRAINT authors_pkey PRIMARY KEY (id);


--
-- Name: authors authors_pseudonym_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors
    ADD CONSTRAINT authors_pseudonym_key UNIQUE (pseudonym);


--
-- Name: authors authors_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors
    ADD CONSTRAINT authors_user_id_key UNIQUE (user_id);


--
-- Name: book_images book_images_book_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_images
    ADD CONSTRAINT book_images_book_id_key UNIQUE (book_id);


--
-- Name: book_images book_images_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_images
    ADD CONSTRAINT book_images_pkey PRIMARY KEY (id);


--
-- Name: books books_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);


--
-- Name: cart_item cart_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_item
    ADD CONSTRAINT cart_item_pkey PRIMARY KEY (id);


--
-- Name: carts carts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT carts_pkey PRIMARY KEY (id);


--
-- Name: carts carts_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT carts_user_id_key UNIQUE (user_id);


--
-- Name: images images_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT images_pkey PRIMARY KEY (id);


--
-- Name: images images_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT images_user_id_key UNIQUE (user_id);


--
-- Name: likes likes_book_id_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT likes_book_id_user_id_key UNIQUE (book_id, user_id);


--
-- Name: likes likes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT likes_pkey PRIMARY KEY (id);


--
-- Name: moderation_requests moderation_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.moderation_requests
    ADD CONSTRAINT moderation_requests_pkey PRIMARY KEY (id);


--
-- Name: refresh_tokens refresh_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT refresh_tokens_pkey PRIMARY KEY (id);


--
-- Name: refresh_tokens refresh_tokens_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT refresh_tokens_token_key UNIQUE (token);


--
-- Name: refresh_tokens refresh_tokens_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT refresh_tokens_user_id_key UNIQUE (user_id);


--
-- Name: likes uk7ill18wly8woh4amo7h4kvku6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT uk7ill18wly8woh4amo7h4kvku6 UNIQUE (book_id, user_id);


--
-- Name: authors uk9mhkwvnfaarcalo4noabrin5j; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors
    ADD CONSTRAINT uk9mhkwvnfaarcalo4noabrin5j UNIQUE (name);


--
-- Name: user_verification user_verification_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_verification
    ADD CONSTRAINT user_verification_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: images fk13ljqfrfwbyvnsdhihwta8cpr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT fk13ljqfrfwbyvnsdhihwta8cpr FOREIGN KEY (user_id) REFERENCES public.users (id);


--
-- Name: refresh_tokens fk1lih5y2npsf8u5o3vhdb9y0os; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT fk1lih5y2npsf8u5o3vhdb9y0os FOREIGN KEY (user_id) REFERENCES public.users (id) ON DELETE CASCADE;


--
-- Name: moderation_requests fk2uhhedkegcurgillp1an3y97y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.moderation_requests
    ADD CONSTRAINT fk2uhhedkegcurgillp1an3y97y FOREIGN KEY (author_id) REFERENCES public.authors (id);


--
-- Name: authors fk6g6ireq6qd4nxohq9ldidxfin; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors
    ADD CONSTRAINT fk6g6ireq6qd4nxohq9ldidxfin FOREIGN KEY (user_id) REFERENCES public.users (id);


--
-- Name: cart_item fkb58e5ca5nwhh6hm3sboyggghe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_item
    ADD CONSTRAINT fkb58e5ca5nwhh6hm3sboyggghe FOREIGN KEY (book_id) REFERENCES public.books (id);


--
-- Name: carts fkb5o626f86h46m4s7ms6ginnop; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT fkb5o626f86h46m4s7ms6ginnop FOREIGN KEY (user_id) REFERENCES public.users (id);


--
-- Name: book_images fkcnpy06tjmrsjisjf2bqpuvvbl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_images
    ADD CONSTRAINT fkcnpy06tjmrsjisjf2bqpuvvbl FOREIGN KEY (book_id) REFERENCES public.books (id);


--
-- Name: likes fkcs5o49xjjpot3n862l7mmeh26; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT fkcs5o49xjjpot3n862l7mmeh26 FOREIGN KEY (book_id) REFERENCES public.books (id);


--
-- Name: books fkfjixh2vym2cvfj3ufxj91jem7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT fkfjixh2vym2cvfj3ufxj91jem7 FOREIGN KEY (author_id) REFERENCES public.authors (id);


--
-- Name: cart_item fklqwuo55w1gm4779xcu3t4wnrd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_item
    ADD CONSTRAINT fklqwuo55w1gm4779xcu3t4wnrd FOREIGN KEY (cart_id) REFERENCES public.carts (id);


--
-- Name: likes fknvx9seeqqyy71bij291pwiwrg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT fknvx9seeqqyy71bij291pwiwrg FOREIGN KEY (user_id) REFERENCES public.users (id);


--
-- PostgreSQL database dump complete
--

\unrestrict aE3yB9v4XPaYxD37n0YFJ993GxDotLsgxz9512FGBFuXgX9il1oIdySmBg7NpRK


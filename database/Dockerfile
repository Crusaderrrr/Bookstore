FROM postgres:15-alpine

COPY dump.sql /docker-entrypoint-initdb.d/

ENV POSTGRES_DB=mydb
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres123
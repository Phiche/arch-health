CREATE TABLE users (
	id uuid primary key,
	mail varchar unique not null,
	username varchar unique not null,
	firstName varchar,
	lastName varchar
)
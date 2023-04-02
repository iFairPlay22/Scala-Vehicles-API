CREATE TABLE IF NOT EXISTS vehicles(
    date_bucket         date,
    id                  int,
    name                text,
    PRIMARY KEY((date_bucket), id)
) WITH CLUSTERING ORDER BY (id ASC);
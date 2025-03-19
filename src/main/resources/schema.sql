
CREATE TABLE IF NOT EXISTS seasons (
    id uuid PRIMARY KEY,
    season_year NUMERIC NOT NULL
);

CREATE TABLE IF NOT EXISTS teams (
    id uuid PRIMARY KEY,
    name VARCHAR (50) NOT NULL
);

CREATE TABLE IF NOT EXISTS players (
    id uuid PRIMARY KEY,
    name VARCHAR (50) NOT NULL,
    surname VARCHAR (50) NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams(id)
);

CREATE TABLE IF NOT EXISTS stats (
    id uuid PRIMARY KEY,
    player_id uuid NOT NULL,
    team_id uuid NOT NULL,
    season_id uuid NOT NULL,
    points NUMERIC NOT NULL,
    rebounds NUMERIC NOT NULL,
    assists NUMERIC NOT NULL,
    steals NUMERIC NOT NULL,
    blocks NUMERIC NOT NULL,
    fouls NUMERIC NOT NULL,
    turnovers NUMERIC NOT NULL,
    minutes_played FLOAT NOT NULL,
    FOREIGN KEY (player_id) REFERENCES players(id),
    FOREIGN KEY (team_id) REFERENCES teams(id),
    FOREIGN KEY (season_id) REFERENCES seasons(id)
)
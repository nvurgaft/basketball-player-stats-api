
CREATE TABLE IF NOT EXISTS teams (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR (50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS players (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR (50) NOT NULL,
    surname VARCHAR (50) NOT NULL
);

CREATE TABLE IF NOT EXISTS stats (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id uuid NOT NULL,
    team_id uuid NOT NULL,
    season NUMERIC NOT NULL,
    points NUMERIC NOT NULL,
    rebounds NUMERIC NOT NULL,
    assists NUMERIC NOT NULL,
    steals NUMERIC NOT NULL,
    blocks NUMERIC NOT NULL,
    fouls NUMERIC NOT NULL,
    turnovers NUMERIC NOT NULL,
    minutes_played FLOAT NOT NULL,
    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE
)

--CREATE INDEX players_id ON players(name);

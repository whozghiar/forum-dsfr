CREATE TABLE auteur (
                        id SERIAL PRIMARY KEY,
                        pseudo VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE forum (
                       id SERIAL PRIMARY KEY,
                       titre VARCHAR(255) NOT NULL,
                       description TEXT NOT NULL
);

CREATE TABLE sujet (
                       id SERIAL PRIMARY KEY,
                       titre VARCHAR(255) NOT NULL,
                       forum_id INTEGER NOT NULL,
                       CONSTRAINT fk_sujet_forum FOREIGN KEY (forum_id) REFERENCES forum(id) ON DELETE CASCADE
);

CREATE TABLE message (
                         id SERIAL PRIMARY KEY,
                         auteur_id INTEGER NOT NULL,
                         sujet_id INTEGER NOT NULL,
                         contenu TEXT,
                         date_creation TIMESTAMP,
                         CONSTRAINT fk_message_auteur FOREIGN KEY (auteur_id) REFERENCES auteur(id) ON DELETE CASCADE,
                         CONSTRAINT fk_message_sujet FOREIGN KEY (sujet_id) REFERENCES sujet(id) ON DELETE CASCADE
);

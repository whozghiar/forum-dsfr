-- AUTEURS
INSERT INTO auteur (id, pseudo) VALUES
  (1, 'Alice'),
  (2, 'Bob'),
  (3, 'Charlie'),
  (4, 'Diana'),
  (5, 'Ethan'),
  (6, 'Fiona'),
  (7, 'Gaspard'),
  (8, 'Hélène'),
  (9, 'Isaac'),
  (10, 'Jade');

-- FORUMS
INSERT INTO forum (id, titre, description) VALUES
  (1, 'Développement Java', 'Partage autour de Java, Spring Boot et JPA'),
  (2, 'Docker & DevOps', 'Conteneurisation, CI/CD, bonnes pratiques DevOps'),
  (3, 'VueJS & Frontend', 'Discussions sur Vue 3, DSFR et design frontend');

-- SUJETS
INSERT INTO sujet (id, titre, epingle, forum_id) VALUES
  (1, 'Problème de mapping JPA',true, 1),
  (2, 'Comment gérer les relations @OneToMany',false, 1),
  (3, 'Spring Boot et sécurité JWT',false, 1),
  (4, 'Tests unitaires avec Mockito',false, 1),
  (5, 'Utiliser Lombok intelligemment',false, 1),

  (6, 'Créer un Dockerfile propre',true, 2),
  (7, 'Exemples de docker-compose efficaces',false, 2),
  (8, 'CI/CD avec GitHub Actions',false, 2),
  (9, 'Déploiement sur Heroku ou Railway',false, 2),
  (10, 'Optimisation de build Maven dans Docker',false, 2),

  (11, 'Composants DSFR dans Vue 3',true, 3),
  (12, 'Routing avancé avec Vue Router',false, 3),
  (13, 'State management avec Pinia',false, 3),
  (14, 'Intégrer Axios et gérer les erreurs',false, 3),
  (15, 'Customisation du DSFR avec Sass',false, 3);

-- MESSAGES
DO $$
DECLARE
    message_id INT := 1;
    sujet_id INT := 1;
BEGIN
    FOR s IN 1..15 LOOP -- 15 sujets
        FOR m IN 1..10 LOOP -- 10 messages par sujet
            INSERT INTO message (id, auteur_id, sujet_id, contenu, date_creation)
            VALUES (
                message_id,
                (1 + random() * 9)::INT, -- auteur aléatoire de 1 à 10
                sujet_id,
                CONCAT('Message ', message_id, ' sur le sujet ', sujet_id),
                NOW() - ((random() * 1000)::INT || ' seconds')::INTERVAL
            );
            message_id := message_id + 1;
        END LOOP;
        sujet_id := sujet_id + 1;
    END LOOP;
END $$;

-- AUTEURS
INSERT INTO auteur ( pseudo) VALUES
  ( 'Alice'),
  ('Bob'),
  ( 'Charlie'),
  ( 'Diana'),
  ('Ethan'),
  ( 'Fiona'),
  ('Gaspard'),
  ('Hélène'),
  ('Isaac'),
  ( 'Jade');

-- FORUMS
INSERT INTO forum (titre, description) VALUES
  ( 'Développement Java', 'Partage autour de Java, Spring Boot et JPA'),
  ( 'Docker & DevOps', 'Conteneurisation, CI/CD, bonnes pratiques DevOps'),
  ( 'VueJS & Frontend', 'Discussions sur Vue 3, DSFR et design frontend');

-- SUJETS
INSERT INTO sujet ( titre, epingle, forum_id) VALUES
  ( 'Problème de mapping JPA',true, 1),
  ( 'Comment gérer les relations @OneToMany',false, 1),
  ( 'Spring Boot et sécurité JWT',false, 1),
  ( 'Tests unitaires avec Mockito',false, 1),
  ( 'Utiliser Lombok intelligemment',false, 1),

  ( 'Créer un Dockerfile propre',true, 2),
  ( 'Exemples de docker-compose efficaces',false, 2),
  ( 'CI/CD avec GitHub Actions',false, 2),
  ( 'Déploiement sur Heroku ou Railway',false, 2),
  ( 'Optimisation de build Maven dans Docker',false, 2),

  ( 'Composants DSFR dans Vue 3',true, 3),
  ( 'Routing avancé avec Vue Router',false, 3),
  ( 'State management avec Pinia',false, 3),
  ( 'Intégrer Axios et gérer les erreurs',false, 3),
  ( 'Customisation du DSFR avec Sass',false, 3);

-- MESSAGES
DO $$
DECLARE
    message_id INT := 1;
    sujet_id INT := 1;
BEGIN
    FOR s IN 1..15 LOOP -- 15 sujets
        FOR m IN 1..10 LOOP -- 10 messages par sujet
            INSERT INTO message ( auteur_id, sujet_id, contenu, date_creation)
            VALUES (
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
